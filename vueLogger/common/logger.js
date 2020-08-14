require('winston-daily-rotate-file');

const { createLogger, format, transports } = require('winston');
const { combine, timestamp, prettyPrint, colorize, printf } = format;

const myFormat = printf(({ level, message, label, timestamp }) => {
	return `${timestamp} [${label}] ${level}: ${message}`;
});

const errorStackFormat = format(info => {
	if (info instanceof Error) {
		return Object.assign({}, info, {
			stack: info.stack,
			message: info.message
		});
	}
	return info;
});

var errorAppender = new (transports.DailyRotateFile)({
	filename: './logs/error-%DATE%.log',
	level: 'error',
	datePattern: 'YYYY-MM-DD',
	maxSize: '100k',
	format: combine(
		colorize(),
		timestamp(),
		errorStackFormat(),
		myFormat,
		prettyPrint()
	)
});
errorAppender.on('rotate', function(oldFilename, newFilename) {
	console.log('rotate ' + oldFilename + ' ---> ' + newFilename);
});

var combinedAppender = new (transports.DailyRotateFile)({
	filename: './logs/combined-%DATE%.log',
	level: 'info',
	datePattern: 'YYYY-MM-DD',
	maxSize: '100k',
	format: combine(
		colorize(),
		timestamp(),
		errorStackFormat(),
		myFormat,
		prettyPrint()
	)
});
combinedAppender.on('rotate', function(oldFilename, newFilename) {
	console.log('rotate ' + oldFilename + ' ---> ' + newFilename);
});

var debugAppender = new (transports.DailyRotateFile)({
	filename: './logs/debug-%DATE%.log',
	level: 'debug',
	datePattern: 'YYYY-MM-DD',
	maxSize: '100k',
	format: combine(
		colorize(),
		timestamp(),
		errorStackFormat(),
		myFormat,
		prettyPrint()
	)
});
debugAppender.on('rotate', function(oldFilename, newFilename) {
	console.log('rotate ' + oldFilename + ' ---> ' + newFilename);
});

const getLogger = (module, type) => {
	const modulePath = module.filename.split('/').slice(-2).join('/');
	const logger = createLogger({
		transports: [
			new (transports.Console)({
				colorize: true,
				level: 'error',
				label: modulePath
			})
		]
	});

	switch (type) {
	case 'error':
		logger.add(errorAppender);
		return logger;
	case 'info':
		logger.add(combinedAppender);
		return logger;
	case 'debug':
		logger.add(debugAppender);
		return logger;
	}
};
/*
logger.on('error', function (err) {
	getLogger(module, 'error').error('Error occurred', err);
});
*/
module.exports = module => ({
	error(err) {
		getLogger(module, 'error').error(err);
	},
	info(err) {
		getLogger(module, 'info').info(err);
	},
	debug(err) {
		getLogger(module, 'debug').debug(err);
	}
});

function handleError (err, filename, funcion) {
	getLogger(module, 'error').error({'level': 'error', 'message': err, 'label': filename + (funcion != null ? '.' + funcion : ''), 'stack': err.stack});
}
module.exports.handleError = handleError;
