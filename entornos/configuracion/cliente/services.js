const constants = require('../../common/constants.js');

const db = require('./db.js');

async function getClientes() {
	let param = null;
	await db.selectClientes()
		.then(async function(response) {
			param = response;
		}).catch(function(err){
			throw new Error(err)
		});
	return param
}
module.exports.getClientes = getClientes;
