const configuracion = require('../../configuracion/routes.js');

function init(app) {
	app.use('/api', configuracion);
}

module.exports.init = init;
