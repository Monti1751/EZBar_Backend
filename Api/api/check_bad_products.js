const http = require('http');

http.get('http://localhost:3000/api/productos', (resp) => {
    let data = '';

    // A chunk of data has been received.
    resp.on('data', (chunk) => {
        data += chunk;
    });

    // The whole response has been received.
    resp.on('end', () => {
        const products = JSON.parse(data);
        const badProducts = products.filter(p => p.categoria_id === 4);
        console.log('Products with category 4:', badProducts.length);
        badProducts.forEach(p => console.log(p.producto_id, p.nombre));
    });

}).on("error", (err) => {
    console.log("Error: " + err.message);
});
