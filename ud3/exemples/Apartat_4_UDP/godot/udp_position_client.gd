extends Node

signal snapshot_received(sequence: int, server_time_ms: int, players: Dictionary)

@export var server_host := "127.0.0.1"
@export var server_port := 9999
@export var player_id := "jugador_1"
@export var send_rate_hz := 20.0

var _udp := PacketPeerUDP.new()
var _send_accumulator := 0.0
var _last_sequence := -1
var local_position := Vector2.ZERO


func _ready() -> void:
    var error := _udp.bind(0)
    if error != OK:
        push_error("No s'ha pogut obrir el sòcol UDP: %s" % error_string(error))
        set_process(false)
        return
    error = _udp.set_dest_address(server_host, server_port)
    if error != OK:
        push_error("Adreça UDP no vàlida: %s" % error_string(error))
        set_process(false)
        return
    _send_text("REG|%s" % player_id)


func _process(delta: float) -> void:
    _send_accumulator += delta
    if _send_accumulator >= 1.0 / send_rate_hz:
        _send_accumulator = 0.0
        _send_text("POS|%s|%.3f|%.3f" % [
            player_id, local_position.x, local_position.y
        ])

    while _udp.get_available_packet_count() > 0:
        _process_packet(_udp.get_packet().get_string_from_utf8())


func _send_text(message: String) -> void:
    var error := _udp.put_packet(message.to_utf8_buffer())
    if error != OK:
        push_warning("No s'ha pogut enviar el datagrama: %s" % error_string(error))


func _process_packet(message: String) -> void:
    var fields := message.split("|", false)
    if fields.size() != 4 or fields[0] != "STATE":
        return

    var sequence := int(fields[1])
    if sequence <= _last_sequence:
        return # Descartem instantànies duplicades o que han arribat tard.
    _last_sequence = sequence

    var players := {}
    for encoded_player in fields[3].split(";", false):
        var values := encoded_player.split(",", false)
        if values.size() == 3:
            players[values[0]] = Vector2(float(values[1]), float(values[2]))
    snapshot_received.emit(sequence, int(fields[2]), players)
