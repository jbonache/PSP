import 'package:flutter/material.dart';
import 'http_requests.dart';
import 'main.dart';

class LoginForm extends StatefulWidget {
  const LoginForm({super.key});

  @override
  State<LoginForm> createState() => _LoginFormState();
}

class _LoginFormState extends State<LoginForm> {
  // Controladors dels textos
  final TextEditingController controladorNom = TextEditingController();
  final TextEditingController controladorPass = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView(
        padding: const EdgeInsets.symmetric(vertical: 100, horizontal: 32),
        children: [
          TextFormField(
            controller: controladorNom,
            decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                icon: const Icon(Icons.login),
                labelText: "Usuari"),
          ),
          const SizedBox(height: 16),
          TextFormField(
            controller: controladorPass,
            decoration: InputDecoration(
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                icon: const Icon(Icons.password),
                labelText: "Contrassenya"),
          ),
          const SizedBox(height: 16),
          ElevatedButton(
            onPressed: () {
              var resposta = login(
                  username: controladorNom.text,
                  password: controladorPass.text);
              resposta.then(
                (value) {
                  // Si la resposta és correcta, iniciem l'aplicació
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                      builder: ((context) => const MainAppView()),
                    ),
                  );
                },
              );
            },
            child: const Text("Login"),
          )
        ],
      ),
    );
  }
}
