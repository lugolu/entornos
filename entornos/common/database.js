require('dotenv').config();

const config = require('../config/config.js');

const logger = require('./logger.js').logger;

const dbConfig = {
	user: 'ANUNCIADOR_TS',
	password: 'TS#3739423415',
	connectString: config.API_DB_SERVICE,
	poolMin: 10,
	poolMax: 10,
	poolIncrement: 0
};
module.exports.dbConfig = dbConfig;

async function initialize() {
	logger.info('database.initialize');

	console.log('base de datos iniciada');
	logger.info('database.initialized');
}
module.exports.initialize = initialize;

async function close() {
	logger.info('database.close');

	console.log('base de datos cerrada');
	logger.info('database.closed');
}
module.exports.close = close;

function simpleExecute(statement, binds = []) {
}
module.exports.simpleExecute = simpleExecute;

function simpleExecuteMulti(queries) {
}
module.exports.simpleExecuteMulti = simpleExecuteMulti;
