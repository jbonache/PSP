import 'dart:io';
import 'dart:convert'; // Per realitzar conversions entre tipus de dades
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // Per realitzar peticions HTTP

// Definim el token
String _token = "";

// Afegim el server com a variable
// Localhost en Genymotion és 10.0.3.2, en Android 10.0.2.2
String _server = "10.0.3.2";
String _httpServer = "http://$_server:8080";
String _httpsServer = "https://$_server:8081";

Future<List<dynamic>> obteUsuaris() async {
  String url = '$_httpServer/api/users';
  debugPrint(url);
  // Llancem una petició GET mitjançant el mètode http.get, i ens esperem a la resposta
  debugPrint(Uri.parse(url).toString());
  http.Response response = await http.get(Uri.parse(url));
  debugPrint(response.statusCode.toString());

  if (response.statusCode == HttpStatus.ok) {
    String body = utf8.decode(response.bodyBytes);
    final result = jsonDecode(body);

    debugPrint(result.toString());
    debugPrint(result.runtimeType.toString());
    return result["data"];
  } else {
    // Si no carrega, llancem una excepció
    throw Exception('No s\'ha pogut connectar');
  }
}

Future afigUsuari({required String nom, required String email}) async {
  // Ara va per HTTPS
  String url = '$_httpsServer/api/users';

  http.Response response = await http.put(
    Uri.parse(url),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
      // Afegim a la capçalera el token
      'access-token': _token,
    },
    body: jsonEncode(<String, String>{'nombre': nom, 'email': email}),
  );

  debugPrint(response.statusCode.toString());

  if (response.statusCode == HttpStatus.ok) {
    String body = utf8.decode(response.bodyBytes);
    final result = jsonDecode(body);

    return result["data"];
  } else {
    // Si no carrega, llancem una excepció
    throw Exception('No s\'ha pogut connectar');
  }
}

Future<bool> login({required String username, required String password}) async {
  String url = '$_httpsServer/auth';
  // Petició POST per a l'autenticació

  http.Response response = await http.post(
    Uri.parse(url),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, String>{'user': username, 'pass': password}),
  );

  debugPrint(response.statusCode.toString());

  if (response.statusCode == HttpStatus.ok) {
    String body = utf8.decode(response.bodyBytes);
    final result = jsonDecode(body);

    debugPrint(result.toString());
    if (result["error"] == 0) {
      _token = result["token"];
    } else {
      // Si no carrega, llancem una excepció
      throw Exception(
          'Error en el servidor: ${result["error"]}, ${result["message"]}');
    }
    //return result["data"];
    return true;
  } else {
    // Si no carrega, llancem una excepció
    throw Exception('No s\'ha pogut connectar');
  }
}
