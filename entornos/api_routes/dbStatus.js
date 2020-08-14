const express = require('express');
const router = new express.Router();
const dbStatus = require('../services/dbStatus.js');

const constants = require('../common/constants.js');

router.route('/dbStatus').get(async function (req, res, next) {
	let v = await dbStatus.get();
	res.status(constants.HTTP_OK).json(v);
});

module.exports = router;
