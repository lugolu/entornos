require('dotenv').config();

const config = require('./config/config.js');

const express = require('express');
const http = require('http');

const app = express();
let server = http.createServer(app);

const routers = require('./api_routes/index.js');

const logger = require('./common/logger').logger;

const constants = require('./common/constants.js');

/* ******************************************************************************************************* */
/* DATABASE */
const database = require('./common/database.js');
/* ******************************************************************************************************* */

/* ******************************************************************************************************* */
/* Loggeo requests en consola y archivos                                                                   */
const morgan = require('morgan');
var path = require('path');
var rfs = require('rotating-file-stream');

const pad = num => (num > 9 ? '' : '0') + num;

const generator = (time, index) => {
	if (!time) return 'file.log';

	var month = time.getFullYear() + pad(time.getMonth() + 1);
	var day = pad(time.getDate());
	var hour = pad(time.getHours());
	var minute = pad(time.getMinutes());

	return `${month}/access_${day}-${hour}${minute}-${index}.log`;
};

var accessLogStream = rfs.createStream(generator, {
	interval: '1d', // rotate daily,
	path: path.join(__dirname, 'logs')
});

// Se loggean en la consola todos los requests donde el statusCode sea >= 400
app.use(morgan('combined', {
	skip: function (req, res) { return res.statusCode < constants.HTTP_BAD_REQUEST; }
}));

// Se loggean en archivo todos los requests sin importar el statusCode
app.use(morgan('combined', { stream: accessLogStream }));

/* ******************************************************************************************************* */

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
logger.info('iniciando conexion a la base de datos ' + config.API_DB_SERVICE);
console.error('iniciando conexion a la base de datos ' + config.API_DB_SERVICE);
// INICIO DE LA CONEXION A LA BASE DE DATOS Y LUEGO LEVANTA EL SERVIDOR//
database.initialize().then(()=>{
	server.listen(PORT, HOST);
	logger.info(`Running general on http://${HOST}:${PORT}`);
	console.error(`Running general on http://${HOST}:${PORT}`);
}).catch((err)=>{
	logger.handleError(err, __filename, 'database init');
	process.exit(1);
});

// FUNCION PARA DAR DE BAJA EL SERVIDOR
async function shutdown(e) {
	let err = e;

	logger.info('Shutting down application');

	try {
		logger.info('Closing database module');
		await database.close();
	} catch (error) {
		err = err || error;
		logger.handleError(err, __filename, 'shutdown');
		next(err);
	}

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
	logger.handleError(err, __filename, 'uncaughtException');
	shutdown(err);
});