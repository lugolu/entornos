const configuracion = require('./configuracion/index.js');
const version = require('./version.js');

function init(app) {
	configuracion.init(app);
	app.use('/api', version);
}

module.exports.init = init;
