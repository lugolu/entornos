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
	return info
});

var errorAppender = new (transports.DailyRotateFile)({
	filename: './logs/error-%DATE%.log',
	level: 'error',
	datePattern: 'YYYY-MM-DD',
	maxSize: '100k'
});
errorAppender.on('rotate', function(oldFilename, newFilename) {
	console.log('rotate ' + oldFilename + ' ---> ' + newFilename);
});

var combinedAppender = new (transports.DailyRotateFile)({
	filename: './logs/combined-%DATE%.log',
	datePattern: 'YYYY-MM-DD',
	maxSize: '100k'
});
combinedAppender.on('rotate', function(oldFilename, newFilename) {
	console.log('rotate ' + oldFilename + ' ---> ' + newFilename);
});

const logger = createLogger({
	format: combine(
		colorize(),
		timestamp(),
		errorStackFormat(),
		myFormat,
		prettyPrint()
	),
	transports: [
		new transports.Console({ level: 'error' }),
		errorAppender,
		combinedAppender
	],
	exceptionHandlers: [
		new transports.File({ filename: './logs/exceptions.log', handleExceptions: true })
	]
});

logger.info('init logs');

logger.on('error', function (err) {
	console.error('Error occurred', err);
});

module.exports.logger = logger;

function handleError (err, filename, funcion) {
	logger.error({'level': 'error', 'message': err, 'label': filename + (funcion != null ? '.' + funcion : ''), 'stack': err.stack});
}

module.exports.logger.handleError = handleError;
