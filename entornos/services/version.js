const config = require('../config/config.js');

const { version } = require('../package.json');

const { FECHA } = require('../generated.json');

async function get() {
	let json = {'CLIENTE': config.CLIENTE, 'FECHA': FECHA, 'VERSION': version};
	return json;
}

module.exports.get = get;
