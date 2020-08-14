const express = require('express');
const router = new express.Router();
const version = require('../services/version.js');

const constants = require('../common/constants.js');

router.route('/version').get(async function (req, res, next) {
	let v = await version.get();
	res.status(constants.HTTP_OK).json(v);
});

module.exports = router;
