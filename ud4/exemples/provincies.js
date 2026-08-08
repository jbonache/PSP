async function obtrenirProvincies() {
    let resposta = await fetch('https://node-comarques-rest-server-production.up.railway.app/api/provincies');
    let respostaJSon = await resposta.json();
    console.log(respostaJSon);
}

obtrenirProvincies();

/*
let resposta= await fetch('https://node-comarques-rest-server-production.up.railway.app/api/provincies');

    .then(function(response) {

        let resposta1 = response.clone();
        // Clonamos la respuesta (response)
        // como resposta1 y la consumimos como texto.
        resposta1.text().then(function(data) {
            console.log(data);
        });




/*fetch('https://node-comarques-rest-server-production.up.railway.app/api/provincies')
    .then(function(response) {

        let resposta1 = response.clone();
        // Clonamos la respuesta (response)
        // como resposta1 y la consumimos como texto.
        resposta1.text().then(function(data) {
            console.log(data);
        });

        let resposta2 = response.clone();
        // Volvemos a clonar response, ahora
        // como resposta2 y la consumimos como ko
        resposta2.json().then(function(data) {
            console.log(data);
        });

        // Ahora clonamos en resposta3 y 
        // la tratamos como un arrayBuffer.
        let resposta3 = response.clone();
        resposta3.arrayBuffer().then(function(data) {
            console.log(data);
        });

        // Y finalmente, resposta4 como blob.
        let resposta4 = response.clone();
        resposta4.blob().then(function(data) {
            console.log(data);
            console.log(URL.createObjectURL(data));
        });

    })
    .catch(function(err) {
        console.error(err);
    });*/