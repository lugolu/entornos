require('dotenv').config();

const config = require('../config/config.js');

const logger = require('./logger.js').logger;

const { Pool } = require('pg')

let pool

async function initialize() {
	logger.info('database.initialize');

	pool = new Pool({
		user: config.DB_USER,
		host: config.DB_HOST,
		database: config.DB_DATABASE,
		password: config.DB_PASS,
		port: config.DB_PORT,
	})
	pool.query('SELECT NOW()', (err, res) => {
		logger.info(res.rows)
		logger.info(err)
	})

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
