const express = require('express');
const router = new express.Router();
const logger = require('../../common/logger.js').logger;

const constants = require('../../common/constants.js');

const cliente = require('./services.js');

router.route('/clientes').get(async function (req, res, next) {
    try {
        let v = await cliente.getClientes()
        res.status(constants.HTTP_OK).json(v)
    } catch (err) {
        logger.handleError(err.message, __filename, 'getClientes', err.stack);
        let v = {'error:': err.message};
        res.status(constants.HTTP_INTERNAL_SERVER_ERROR).json(v);
        next(err);
    }
});

module.exports = router;
