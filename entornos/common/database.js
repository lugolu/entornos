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
		max: 20,
	})
	await pool.query('SELECT NOW()')
		.then(res => console.log(res.rows))
		.catch(e => {throw new Error(e)})

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

async function simpleExecute(statement, binds = []) {
	let ret = null
	await pool.query(statement)
		.then(res => ret = res)
		.catch(e => {throw new Error(e)})
	return ret
}
module.exports.simpleExecute = simpleExecute;

function simpleExecuteMulti(queries) {
}
module.exports.simpleExecuteMulti = simpleExecuteMulti;

function status(queries) {
	let ret = {
		totalCount: pool.totalCount,
		idleCount: pool.idleCount,
		waitingCount: pool.waitingCount
	}
	return ret
}
module.exports.status = status;
