// Importem les funcionalitats del model
import { getUsers, getUser, addUser } from '../models/users.js';

// Controladores (y vistas)
export default class usuariosController {

    static async ObtenerNombresController(req, res) {
        let lista = []
        let response;
        let type = "application/json";
        let status;

        if (typeof(req.params.nombre) === typeof(undefined) &&
            typeof(req.query.nombre) === typeof(undefined)) {

            // Fem ús directament del mètode getUSers,
            // que ja retorna la llista d'usuaris com un vector

            let llista = await getUsers(req.params.nombre);
            response = { "status": "ok", "data": llista };
            status = 200;
        } else {
            let nombre;
            if (typeof(req.params.nombre) !== typeof(undefined)) nombre = req.params.nombre;
            else nombre = req.query.nombre;

            response = await getUser(nombre);

            if (!response) response = { "status": "error", "msg": "User not Found" };
        }

        res.status(status ?? 200).type(type).send(response);

    }

    static async ObtenerDatosUsuarioPost(req, res) {

        let nombre = req.body.nombre || null;

        res.status(200);
        let response = await getUser(nombre);
        if (!response) response = { "status": "error", "msg": "User not Found" };

        res.send(response);

    }


    static async AnyadirUsuario(req, res) {

        let nombre = req.body.nombre || null;
        let email = req.body.email || null;

        console.log(nombre);
        console.log(email);

        let response;

        if (nombre && email) {
            await addUser(nombre, email);
            response = { "status": "ok" }
        } else response = { "status": "error", "errorMsg": "Undefined Data" };

        res.send(response);

    }
}
