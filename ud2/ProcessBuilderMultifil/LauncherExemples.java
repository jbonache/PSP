import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

class Launcher implements Runnable {

    String ordre; // Los objetos de tipo MiRunnable tendrán una propiedad nombre
    String color;

    Launcher(String ordre, String color) {
        this.ordre = ordre;
        this.color = color;
    }

    @Override
    public void run() {

        try {
            ProcessBuilder pb = new ProcessBuilder(ordre.split(" "));
            Process p = pb.start();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            p.getInputStream()));

            // Leemos del stream y escribimos en el fichero

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(this.color+line+"\u001B[0m");
            }

        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}

public class LauncherExemples {
    public static void main(String[] args) {
        try {
            // Creamos algunos objetos de ejemplo
            Launcher l1 = new Launcher("ps aux", "\u001B[31m");
            Launcher l2 = new Launcher("ls -l /", "\u001B[32m");

            // Y los hilos correspondientes
            Thread hilo1 = new Thread(l1);
            Thread hilo2 = new Thread(l2);
            // Thread hilo2 = new Thread(runnable2);
            // Thread hilo3 = new Thread(runnable3);

            // Lanzamos los hilos
            hilo1.start();
            hilo2.start();
             
            // hilo3.start();

            // Y los juntamos con el principal cuando acaben cuando acaban
            hilo1.join();
            hilo2.join();
            // hilo3.join();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
