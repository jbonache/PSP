function Exercici1() {
    fetch('https://node-comarques-rest-server-production.up.railway.app/api/provincies')
        .then((response) => { return response.json(); })
        .then((provincies) => {
            for (let provincia of provincies) {
                fetch('https://node-comarques-rest-server-production.up.railway.app/api/comarques/' + provincia)
                    .then((response) => { return response.json(); })
                    .then((comarques) => {
                        for (let comarca of comarques) {
                            console.log(comarca.comarca + "(" + comarca.poblacio + " hab)" + ", capital " + comarca.capital);
                        }
                    })
            }
        });
}

Exercici1();


/*


            async function obtrenirProvincies() {
                let resposta = await fetch('https://node-comarques-rest-server-production.up.railway.app/api/provincies');
                let respostaJSon = await resposta.json();
                console.log(respostaJSon);
            }

            obtrenirProvincies();
            */