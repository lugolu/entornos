const logs = require('../logger/routes.js');
const version = require('./version.js');

function init(app) {
	app.use('/api', logs);
	app.use('/api', version);
}

module.exports.init = init;
