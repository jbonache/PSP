// Importamos las bibliotecas ExpressJS y body-parser
import express from 'express';

// La biblioteca bodyParser es un módulo commonJS y no
// soporta import { urlencoded, json } from 'body-parser';
// Debemos hacerlo así

// Definimos app como una aplicación express, utilizando el método 
// de factoría express().
const app = express();

// Configuramos la aplicación para que descodifique 
// las peticiones del cliente y las pase a JSON.
app.use(express.urlencoded({ extended: false }));
app.use(express.json({ limit: '100kb' }));

// Configuramos el servidor para escuchar por el puerto  8080
// Observad que el primer argumento es el puerto y el segundo
// es un callback que se lanza cuando se ha iniciado el servidor.
app.listen(8080, () => {
    console.log('Escuchando por el puerto 8080')
});

// Modelo de datos
let usuarios = [{ "nombre": "pepe", email: "pepe@gmail.com" }, ]

function getUser(nombre) {
    for (let usuario of usuarios) {
        if (usuario.nombre === nombre)
            return { "status": "ok", "data": usuario };
    }
    return null;
}

function addUser(nombre, email) {
    usuarios.push({ "nombre": nombre, email: email });
}

// Rutas

app.get('/api/users{/:nombre}', function(req, res) {

    let lista = []
    let response;
    let type = "application/json";
    let status;

    if (typeof(req.params.nombre) === typeof(undefined) && typeof(req.query.nombre) === typeof(undefined)) {
        for (let usuario of usuarios) { lista.push(usuario.nombre); }
        response = { "status": "ok", "data": lista };
        status = 201;
    } else {
        let nombre;
        if (typeof(req.params.nombre) !== typeof(undefined)) nombre = req.params.nombre;
        else nombre = req.query.nombre;
        status = 202;

        response = getUser(nombre);

        if (!response) {
            response = { "status": "error", "msg": "User not Found" };
            status = 404;
        }
    }

    res.status(status).type("application/json").send(response);

});


app.post('/api/users', function(req, res) {

    let nombre = req.body.nombre || null;

    res.statusCode = 200;
    let response = getUser(nombre);
    if (!response) response = { "status": "error", "msg": "User not Found" };

    res.send(response);

});


app.put('/api/users', function(req, res) {

    // Para peticiones POST debemos 
    let nombre = req.body.nombre || null;
    let email = req.body.email || null;

    console.log(nombre);
    console.log(email);

    let response;

    if (nombre && email) {
        addUser(nombre, email);
        response = { "status": "ok" }
    } else response = { "status": "error", "errorMsg": "Undefined Data" };

    res.send(response);

});


app.use('/{*splat}', function(req, res) {
    res.send('Error 404. Not Found');
});