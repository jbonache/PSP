import 'package:flutter/material.dart';
import 'package:users_app/http_requests.dart';

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  static const String _title = 'Gestió d\'usuaris';

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: _title,
      home: MyStatelessWidget(),
    );
  }
}

class MyStatelessWidget extends StatelessWidget {
  const MyStatelessWidget({super.key});

  @override
  Widget build(BuildContext context) {
    /* 
    El giny comença amb un DefaultTabController, on indiquem
    el número de pestanyes (length) i l'índex de la pestanya
    predeterminada (initialIndex)
    */
    return DefaultTabController(
      initialIndex: 0,
      length: 2,
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Gestio d\'usuaris'),
          /*
          El component bottom de l'AppBar conté el 
          TabBar defineix les diferents pestanyes.
          Els tabs poden contindre un text i una icona, 
          però al menys una de les dos coses.

          */
          bottom: const TabBar(
            tabs: <Widget>[
              Tab(
                text: "Usuaris",
                icon: Icon(Icons.people),
              ),
              Tab(
                text: "Afig usuari",
                icon: Icon(Icons.person_pin_outlined),
              ),
            ],
          ),
        ),
        // El giny TabBarWidget defineix els diferents
        // contenidors per al contingut de cada pestanya
        body: const TabBarView(
          children: <Widget>[
            // Contingut de la primera pestanya
            UserList(),
            AddUser(),
          ],
        ),
      ),
    );
  }
}

class UserList extends StatefulWidget {
  const UserList({
    Key? key,
  }) : super(key: key);

  @override
  State<UserList> createState() => _UserListState();
}

class _UserListState extends State<UserList> {
  late Future<List<dynamic>> _userList;

  @override
  void initState() {
    super.initState();
    _userList = obteUsuaris();
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: _userList,
      builder: (BuildContext context, AsyncSnapshot snapshot) {
        if (snapshot.hasData) {
          List<dynamic> values = snapshot.data;
          return Padding(
            padding: const EdgeInsets.all(8.0),
            child: ListView.builder(
              itemCount: values.length,
              itemBuilder: (BuildContext context, int index) {
                if (values.isNotEmpty) {
                  return Card(
                    child: ListTile(
                      leading: const Icon(Icons.people),
                      title: Text(
                        values[index],
                      ),
                    ),
                  );
                } else {
                  return const Center(
                    child: Text("La llista és buida"),
                  );
                }
              },
            ),
          );
        } else if (snapshot.hasError) {
          return Text(snapshot.error.toString());
        }
        // Per a quan no té encara dades, mostrem un
        // giny indicador de progrés.
        return const Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }
}

class AddUser extends StatelessWidget {
  const AddUser({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const Padding(
      padding: EdgeInsets.all(8.0),
      child: addUserWidget(),
    );
  }
}

class addUserWidget extends StatefulWidget {
  const addUserWidget({super.key});

  @override
  State<addUserWidget> createState() => _addUserWidgetState();
}

class _addUserWidgetState extends State<addUserWidget> {
  final TextEditingController controladorNom = TextEditingController();
  final TextEditingController controladorEmail = TextEditingController();

  @override
  void initState() {
    super.initState();
    controladorEmail.text = "";
    controladorNom.text = "";
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          creaTextField(controlador: controladorNom, text: "Nom"),
          const SizedBox(height: 16),
          creaTextField(controlador: controladorEmail, text: "Correu"),
          const SizedBox(height: 16),
          creaBotoSubmit(),
        ],
      ),
    );
  }

  @override
  void dispose() {
    // Alliberem el controlador quan el giny
    // s'elimine de l'arbre de gints.
    controladorNom.dispose();
    controladorEmail.dispose();
    super.dispose();
  }

  TextField creaTextField(
      {required TextEditingController controlador, required String text}) {
    return TextField(
      // Associem el controlador al controller
      // del TextField

      controller: controlador,

      decoration: InputDecoration(
        labelText: text,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(10),
        ),
      ),
    );
  }

  ElevatedButton creaBotoSubmit() {
    return ElevatedButton(
      onPressed: () {
        afigUsuari(nom: controladorNom.text, email: controladorEmail.text);
        controladorNom.text = "";
        controladorEmail.text = "";
      },
      child: const Text("Registrar usuari"),
    );
  }
}
