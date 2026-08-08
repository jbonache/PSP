// Importamos las bibliotecas ExpressJS y body-parser
import express from 'express';

// Importamos los controladores
import usuariosController from './controllers/usuariosController.js';


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

function DefaultController(req, res) {
    res.send('Error 404. Not Found');
}

// Definición del Router
const router = express.Router();

router.get('{/:nombre}', usuariosController.ObtenerNombresController);
router.post('/', usuariosController.ObtenerDatosUsuarioPost);
router.put('/', usuariosController.AnyadirUsuario);

// Configuración de la app
app.use("/api/users", router);
app.use('/{*splat}', DefaultController);