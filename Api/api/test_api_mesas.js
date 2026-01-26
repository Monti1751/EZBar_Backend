import fetch from 'node-fetch';

const BASE_URL = 'http://localhost:3000/api/mesas';

async function test() {
    try {
        console.log('Testing without filter...');
        const all = await fetch(BASE_URL).then(r => r.json());
        console.log(`All tables count: ${all.length}`);

        console.log('Testing with filter ubicacion=Terraza...');
        const terraza = await fetch(`${BASE_URL}?ubicacion=Terraza`).then(r => r.json());
        console.log(`Terraza tables count: ${terraza.length}`);
        if (terraza.length > 0) {
            console.log('Sample table ubicacion:', terraza[0].ubicacion);
        }

        console.log('Testing with filter ubicacion=INVALID_ZONE...');
        const invalid = await fetch(`${BASE_URL}?ubicacion=INVALID_ZONE`).then(r => r.json());
        console.log(`Invalid zone tables count: ${invalid.length}`);

    } catch (e) {
        console.error('Error:', e);
    }
}

test();
