import app from './src/app.js';
import http from 'http';

const port = 3001;

const server = http.createServer(app);

server.listen(port, () => {
    console.log(`Temp server running on port ${port}`);
});
