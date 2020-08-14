const database = require('../../common/database.js');

async function selectClientes() {
	let select = 'select * from entornos.cliente';

	let binds = {};

    let result = await database.simpleExecute(select, binds);
    console.error(result)

	return result.rows;
}
module.exports.selectClientes = selectClientes;
