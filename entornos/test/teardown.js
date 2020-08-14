const database = require('../common/database.js');
const logger = require('../common/logger').logger;

after(function () {
	logger.info('after all tests');
	try {
		logger.info('Closing database module');
		database.close();
		logger.info('Closed database module');
	} catch (err) {
		logger.handleError(err, __filename);
	}
});