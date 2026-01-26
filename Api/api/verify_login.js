import axios from 'axios';

const testLogin = async () => {
    const url = 'http://localhost:3000/api/auth/login';

    console.log('--- Probando Login ---');

    // 1. Login correcto
    try {
        console.log('1. Intentando login correcto (admin / 1234)...');
        const res = await axios.post(url, {
            username: 'admin',
            password: 'password123'
        });
        console.log('✅ Login exitoso:', res.data.message);
        if (res.data.data.token) console.log('✅ Token recibido');
    } catch (error) {
        console.error('❌ Falló login correcto:', error.response ? error.response.data : error.message);
    }

    // 2. Login incorrecto
    try {
        console.log('\n2. Intentando login incorrecto (admin / wrongpass)...');
        await axios.post(url, {
            username: 'admin',
            password: 'wrongpass'
        });
        console.error('❌ Error: El login incorrecto debería haber fallado pero tuvo éxito.');
    } catch (error) {
        if (error.response && error.response.status === 401) {
            console.log('✅ Login incorrecto rechazado correctamente (401).');
        } else {
            console.error('❌ Error inesperado en login incorrecto:', error.message);
        }
    }
};

testLogin();
