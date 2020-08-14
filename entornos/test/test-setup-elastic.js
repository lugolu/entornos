const logger = require('../common/logger').logger;

const database = require('../common/database.js');

before(async function () {
	logger.info('before all tests');
	try {
		logger.info('Initializing database module');
		await database.initialize();
		logger.info('Initialized database module');
	} catch (err) {
		logger.handleError(err, __filename);
	}
});