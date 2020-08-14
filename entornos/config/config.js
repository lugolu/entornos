module.exports = {
	API_DB_SERVICE: process.env.API_DB_SERVICE || '192.168.150.25/HOSDESA2',
	/**
	 * Los siguientes parametros solo son modificables a nivel de desarrollo
	 */
	API_PORT: process.env.API_PORT || 12345
};