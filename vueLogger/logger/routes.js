const express = require('express');
const router = new express.Router();
const logs = require('./services.js');

const constants = require('../common/constants.js');

var formidable = require('formidable');

const handleError = require('../common/logger').handleError;

router.route('/log/debug').post(function (req, res, next) {
	try {
		var form = new formidable.IncomingForm();

		form.parse(req, async function(err, fields, files) {
			if (err) {
				// Check for and handle any errors here.

				handleError(err, __filename, 'debug.parse.err');
				return;
			}

			logs.debug(fields);

			res.status(constants.HTTP_OK).json('ok');
		});
		res.status(constants.HTTP_OK);
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'debug');
		//next(err);
	}
});

router.route('/log/error').post(function (req, res, next) {
	try {
		var form = new formidable.IncomingForm();

		form.parse(req, async function(err, fields, files) {
			if (err) {
				// Check for and handle any errors here.

				handleError(err, __filename, 'error.parse.err');
				return;
			}

			logs.error(fields);

			res.status(constants.HTTP_OK).json('ok');
		});
		res.status(constants.HTTP_OK);
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'error');
		//next(err);
	}
});

router.route('/log/info').post(function (req, res, next) {
	try {
		var form = new formidable.IncomingForm();

		form.parse(req, async function(err, fields, files) {
			if (err) {
				// Check for and handle any errors here.

				handleError(err, __filename, 'info.parse.err');
				return;
			}

			logs.info(fields);

			res.status(constants.HTTP_OK).json('ok');
		});
		res.status(constants.HTTP_OK);
	} catch (err) {
		res.sendStatus(constants.HTTP_INTERNAL_SERVER_ERROR);
		handleError(err, __filename, 'info');
		//next(err);
	}
});

module.exports = router;
