/* Modelo de datos */

// Añadimos `export` delante de los datos o funciones que deseamos exportar

export let usuarios = [{ "nombre": "pepe", email: "pepe@gmail.com" }, ]

export function getUser(nombre) {
    for (let usuario of usuarios) {
        if (usuario.nombre === nombre)
            return { "status": "ok", "data": usuario };
    }
    return null;
}

export function addUser(nombre, email) {
    usuarios.push({ "nombre": nombre, email: email });
}