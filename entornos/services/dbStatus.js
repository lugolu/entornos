const config = require('../config/config.js');

const database = require('../common/database.js');

function get() {
	let ret = database.status()
	return ret
}
module.exports.get = get;
