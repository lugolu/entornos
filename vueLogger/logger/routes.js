const express = require('express');
const router = new express.Router();
const logs = require('./services.js');

const constants = require('../common/constants.js');

const handleError = require('../common/logger').handleError;

router.route('/log/debug').post(function (req, res, next) {
	try {
		logs.debug(req.body);

		res.status(constants.HTTP_OK).json('ok');
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'debug');
		//next(err);
	}
});

router.route('/log/error').post(function (req, res, next) {
	try {
		logs.error(req.body);

		res.status(constants.HTTP_OK).json('ok');
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'error');
		//next(err);
	}
});

router.route('/log/info').post(function (req, res, next) {
	try {
		logs.info(req.body);

		res.status(constants.HTTP_OK).json('ok');
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'info');
		//next(err);
	}
});

module.exports = router;
