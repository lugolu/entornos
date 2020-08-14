require('dotenv').config();

const config = require('./config/config.js');

const express = require('express');
const http = require('http');

const app = express();
let server = http.createServer(app);

const routers = require('./api_routes/index.js');

const logger = require('./common/logger')(module);

const handleError = require('./common/logger').handleError;

app.use(express.json());

// DECLARACION DE LOS HEADERS
app.use(function (req, res, next) {
	// Website you wish to allow to connect
	res.setHeader('Access-Control-Allow-Origin', '*');

	// Request methods you wish to allow
	res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

	// Request headers you wish to allow
	res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

	// Set to true if you need the website to include cookies in the requests sent
	// to the API (e.g. in case you use sessions)
	res.setHeader('Access-Control-Allow-Credentials', true);

	// Pass to next layer of middleware
	next();
});

routers.init(app);

// Constants
const PORT = config.API_PORT;
const HOST = '0.0.0.0';

logger.info('Starting application');
console.error('Starting application');

server.listen(PORT, HOST);
logger.info(`Running vue logger on http://${HOST}:${PORT}`);
console.error(`Running vue logger on http://${HOST}:${PORT}`);

// FUNCION PARA DAR DE BAJA EL SERVIDOR
async function shutdown(e) {
	let err = e;

	logger.info('Shutting down application');

	logger.info('Exiting process');

	if (err) {
		process.exit(1); // Non-zero failure code
	}
	else {
		process.exit(0);
	}
}

process.on('SIGTERM', () => {
	logger.info('Received SIGTERM');
	shutdown();
});

process.on('SIGINT', () => {
	logger.info('Received SIGINT');
	shutdown();
});

process.on('uncaughtException', err => {
	logger.info('Uncaught exception');
	handleError(err, __filename, 'uncaughtException');
	shutdown(err);
});
