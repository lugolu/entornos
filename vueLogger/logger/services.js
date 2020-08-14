const logger = require('../common/logger')(module);

async function debug(fields) {
	console.debug(fields);
	console.debug('');
	logger.debug({'level': 'debug', 'message': fields.mensaje, 'label': fields.application, 'stack': fields.stack});
}
module.exports.debug = debug;

async function error(fields) {
	console.error(fields);
	console.error('');
	logger.error({'level': 'error', 'message': fields.mensaje, 'label': fields.application, 'stack': fields.stack});
}
module.exports.error = error;

async function info(fields) {
	console.info(fields);
	console.info('');
	logger.info({'level': 'info', 'message': fields.mensaje, 'label': fields.application, 'stack': fields.stack});
}
module.exports.info = info;
