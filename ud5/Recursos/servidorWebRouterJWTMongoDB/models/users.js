// Importem la biblioteca Mongoose
import mongoose from 'mongoose';

const mongoUri = process.env.MONGODB_URI ?? 'mongodb://127.0.0.1:27017/test';
await mongoose.connect(mongoUri, { serverSelectionTimeoutMS: 5000 });
mongoose.connection.on('error', error => console.error(error));

// Definició de l'esquema (equivalent a un bean/xml de mapat),
// a partir de la classe mongoose.Schema.
const Usuarios = new mongoose.Schema({
    nombre: { type: String, required: true, trim: true, unique: true },
    email: { type: String, required: true, trim: true, lowercase: true },
    telefono: { type: String, trim: true }
});

// Defnim el model a partir de l'esquema i 
// l'associem a la col·lecció
const users = mongoose.model('usuario', Usuarios);

// Definim els mètodes pe accedir a la base de dades i 
// obtenir-ne la informació.

// Obtenció de la llista d'usuaris
export async function getUsers() {
    const res = await users.find({}, { nombre: 1, _id: 0 }).lean().exec();
    if (res) {
        let llista = [];
        for (let usuari of res) {
            llista.push(usuari["nombre"]);
        }

        return llista;
    } else {
        return null;
    }
}

// Obtenció de la informació d'un usuari
export async function getUser(nombre) {
    const res = await users.findOne(
        { nombre },
        { nombre: 1, email: 1, _id: 0 }
    ).lean().exec();
    if (res) {
        return res;
    } else {
        return null;
    }
}

// Afegir un usuari
export async function addUser(nombre, email) {
    return users.create({ nombre, email });
}
