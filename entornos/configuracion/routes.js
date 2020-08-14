const express = require('express');
const router = new express.Router();
const logger = require('../common/logger.js').logger;

const constants = require('../common/constants.js');

const configuracion = require('./services.js');

module.exports = router;
