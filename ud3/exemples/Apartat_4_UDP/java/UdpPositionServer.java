import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Exemple didàctic: rep REG/POS dels clients i difon instantànies STATE per UDP.
 * En un joc real, la identitat s'ha de vincular a una sessió autenticada per TCP.
 */
public final class UdpPositionServer implements AutoCloseable {
    private static final int MAX_PACKET_BYTES = 1_200;
    private static final long CLIENT_TIMEOUT_NANOS = Duration.ofSeconds(5).toNanos();

    private final DatagramSocket socket;
    private final Map<String, ClientState> clients = new ConcurrentHashMap<>();
    private final ScheduledExecutorService broadcaster =
            Executors.newSingleThreadScheduledExecutor();
    private final AtomicLong sequence = new AtomicLong();
    private volatile boolean running = true;

    private static final class ClientState {
        final String playerId;
        volatile SocketAddress endpoint;
        volatile float x;
        volatile float y;
        volatile long lastSeenNanos;

        ClientState(String playerId, SocketAddress endpoint) {
            this.playerId = playerId;
            this.endpoint = endpoint;
            this.lastSeenNanos = System.nanoTime();
        }
    }

    public UdpPositionServer(int port) throws IOException {
        socket = new DatagramSocket(new InetSocketAddress(port));
    }

    public void run() throws IOException {
        broadcaster.scheduleAtFixedRate(this::broadcastSnapshot, 0, 50,
                TimeUnit.MILLISECONDS); // 20 instantànies per segon

        byte[] buffer = new byte[MAX_PACKET_BYTES];
        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), packet.getOffset(),
                    packet.getLength(), StandardCharsets.UTF_8);
            processMessage(message, packet.getSocketAddress());
        }
    }

    private void processMessage(String message, SocketAddress endpoint) {
        String[] fields = message.strip().split("\\|", -1);
        try {
            switch (fields[0]) {
                case "REG" -> {
                    if (fields.length != 2 || !validPlayerId(fields[1])) return;
                    clients.compute(fields[1], (id, previous) -> {
                        ClientState state = previous == null
                                ? new ClientState(id, endpoint) : previous;
                        state.endpoint = endpoint;
                        state.lastSeenNanos = System.nanoTime();
                        return state;
                    });
                }
                case "POS" -> {
                    if (fields.length != 4) return;
                    ClientState state = clients.get(fields[1]);
                    if (state == null || !state.endpoint.equals(endpoint)) return;
                    float x = Float.parseFloat(fields[2]);
                    float y = Float.parseFloat(fields[3]);
                    if (!Float.isFinite(x) || !Float.isFinite(y)) return;
                    state.x = x;
                    state.y = y;
                    state.lastSeenNanos = System.nanoTime();
                }
                default -> { /* Ignorem missatges desconeguts. */ }
            }
        } catch (NumberFormatException ignored) {
            // Un datagrama mal format no ha de fer caure el servidor.
        }
    }

    private boolean validPlayerId(String id) {
        return id.matches("[A-Za-z0-9_-]{1,24}");
    }

    private void broadcastSnapshot() {
        long now = System.nanoTime();
        clients.values().removeIf(
                client -> now - client.lastSeenNanos > CLIENT_TIMEOUT_NANOS);
        if (clients.isEmpty()) return;

        StringBuilder text = new StringBuilder("STATE|")
                .append(sequence.incrementAndGet()).append('|')
                .append(System.currentTimeMillis()).append('|');
        clients.values().stream()
                .sorted(Comparator.comparing(client -> client.playerId))
                .forEach(client -> text.append(client.playerId).append(',')
                        .append(client.x).append(',').append(client.y).append(';'));

        byte[] data = text.toString().getBytes(StandardCharsets.UTF_8);
        if (data.length > MAX_PACKET_BYTES) return;
        for (ClientState client : clients.values()) {
            try {
                socket.send(new DatagramPacket(data, data.length, client.endpoint));
            } catch (IOException exception) {
                System.err.println("No s'ha pogut enviar a " + client.playerId
                        + ": " + exception.getMessage());
            }
        }
    }

    @Override
    public void close() {
        running = false;
        broadcaster.shutdownNow();
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        int port = args.length == 0 ? 9_999 : Integer.parseInt(args[0]);
        try (UdpPositionServer server = new UdpPositionServer(port)) {
            System.out.println("Servidor UDP escoltant en el port " + port);
            server.run();
        }
    }
}
