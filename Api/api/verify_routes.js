import router from './src/routes/productosRoutes.js';

console.log('Successfully imported productosRoutes');
if (router && router.stack) {
    const methods = router.stack.map(r => r.route ? Object.keys(r.route.methods).join(',').toUpperCase() + ' ' + r.route.path : 'middleware');
    console.log('Registered routes:', methods);
} else {
    console.error('Router not exported correctly');
}
