(async () => {
  try {
    const controller = await import('../src/controllers/pedidosController.js');
    const { agregarProductoAMesa } = controller;

    const req = { params: { mesaId: '12' }, body: { productoId: 2 } };

    const res = {
      _code: 200,
      status(code) { this._code = code; return this; },
      json(obj) { console.log('RESPONSE:', JSON.stringify({ status: this._code, body: obj }, null, 2)); }
    };

    const next = (err) => { console.error('NEXT ERROR:', err && err.message ? err.message : err); };

    await agregarProductoAMesa(req, res, next);
  } catch (err) {
    console.error('ERROR running add product script:', err);
    process.exitCode = 1;
  }
})();
