import 'dart:io';
import 'dart:convert'; // Per realitzar conversions entre tipus de dades
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http; // Per realitzar peticions HTTP

Future<List<dynamic>> obteUsuaris() async {
  // Localhost en Genymotion és 10.0.3.2!
  String url = 'http://10.0.3.2:8080/api/users';
  //String url = 'http://192.168.20.250:8080/api/users';

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
  String url = 'http://10.0.3.2:8080/api/users';

  http.Response response = await http.put(
    Uri.parse(url),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
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
