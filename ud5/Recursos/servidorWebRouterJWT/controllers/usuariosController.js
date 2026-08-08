// Importamos el modelo
import { usuarios, getUser, addUser } from '../models/users.js';

// Controladores (y vistas)

export default class usuariosController {

    static ObtenerNombresController(req, res) {
        let lista = []
        let response;
        let type = "application/json";
        let status;

        if (typeof(req.params.nombre) === typeof(undefined) && typeof(req.query.nombre) === typeof(undefined)) {
            for (let usuario of usuarios) { lista.push(usuario.nombre); }
            response = { "status": "ok", "data": lista };
            status = 200;
        } else {
            let nombre;
            if (typeof(req.params.nombre) !== typeof(undefined)) nombre = req.params.nombre;
            else nombre = req.query.nombre;

            response = getUser(nombre);

            if (!response) response = { "status": "error", "msg": "User not Found" };
        }

        res.status(status ?? 200).type(type).send(response);

    }

    static ObtenerDatosUsuarioPost(req, res) {

        let nombre = req.body.nombre || null;

        res.status(200);
        let response = getUser(nombre);
        if (!response) response = { "status": "error", "msg": "User not Found" };

        res.send(response);

    }

    static AnyadirUsuario(req, res) {

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

    }
}