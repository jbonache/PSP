// Importamos las bibliotecas ExpressJS y body-parser
import express from 'express';

// Importamos los controladores
import usuariosController from './controllers/usuariosController.js';



// NOU: Accés al sistema de fitxers, per llegir les claus
import { readFileSync } from 'fs';
// NOU: Accés al servei per https
import { createServer } from 'https';

// Nou: CORS
import cors from 'cors';

// NOU: Importem la llibreria jsonwebtoken i 
//      el fitxer de configuració.
import jwt from 'jsonwebtoken';
const { sign, verify } = jwt;

import { config } from './config/config.js';


// Aplicación express
const app = express();

// NOU: Configurem la clau per encriptar les comunicacions
app.set('key', config.key);


// Configurem l'aplicació per a que ens decodifique
// les peticions del client i les passe a JSON.
app.use(express.urlencoded({ extended: false }));
app.use(express.json({ limit: '100kb' }));

// NOU: Configurem l'aplicació per utilitzar CORS
app.use(cors({ origin: process.env.CORS_ORIGIN?.split(',') ?? false }));


// PUERTO 8080 para HTTP
app.listen(8080, () => {
    console.log('Escuchando por el puerto 8080')
});

// Servidor HTTPS:
createServer({
    cert: readFileSync('certificate.crt'),
    key: readFileSync('certificate.key')
}, app).listen(8081, function () {
    console.log("Servidor https al port 8081");
});

function DefaultController(req, res) {
    res.send('Error 404. Not Found');
}


/* NOU *******************
 *  Configuració de JWT  *
 *************************/


// Endpoint (petició POST) per a l'autenticació
app.post('/auth', (req, res) => {
    if (req.body.user === config.user && req.body.pass === config.pass) {
        const payload = { sub: config.user, role: 'admin' };
        const token = sign(payload, app.get('key'), {
            algorithm: 'HS256',
            expiresIn: '15m',
            issuer: 'psp-api',
            audience: 'psp-client'
        });

        res.status(200).json({
            // En la resposta retornem el token
            error: 0,
            message: 'Autenticació correcta',
            token: token
        });
    } else {
        res.status(401).json({
            error: 1,
            message: "Error en l'autenticació"
        })
    }
})


// Middleware per comprovar el token

const routerAuth = express.Router();
routerAuth.use((req, res, next) => {
    const authorization = req.get('authorization');
    const token = authorization?.startsWith('Bearer ')
        ? authorization.slice(7)
        : null;

    if (token) {
        verify(token, app.get('key'), {
            algorithms: ['HS256'],
            issuer: 'psp-api',
            audience: 'psp-client'
        }, (err, decoded) => {
            if (err) {
                return res.status(401).json({
                    error: 1,
                    message: 'Token inválida'
                });
            } else {
                req.decoded = decoded;
                /*console.log(req.decoded);
                console.log("**********************");
                console.log(req.decoded.role);
                console.log("**********************");
                console.log(req.auth);
                console.log("**********************");
                console.log(req.user);
                console.log("**********************");*/
                // Si tot va bé, passem al següent middleware
                next();
            }
        });
    } else {
        res.status(401).json({
            error: 1,
            message: 'Token no proveída.'
        });
    }
});



// Definición del Router per a la miniaplicació d'usuaris
const routerUsers = express.Router();

routerUsers.get('{/:nombre}', usuariosController.ObtenerNombresController);
routerUsers.post('/', usuariosController.ObtenerDatosUsuarioPost);

// Restringim el PUT per als administradors

// Per afegir usuari, abans hem de passar per l'autenticació
routerUsers.put('/', routerAuth, usuariosController.AnyadirUsuario);

// Configuración de la app
app.use("/api/users", routerUsers);
app.use('/{*splat}', DefaultController);
