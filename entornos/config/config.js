module.exports = {
	DB_USER: process.env.DB_USER || 'demo',
	DB_HOST: process.env.DB_HOST || 'localhost',
	DB_DATABASE: process.env.DB_DATABASE || 'db',
	DB_PASS: process.env.DB_PASS || 'example',
	DB_PORT: process.env.DB_PORT || 5432,
	/**
	 * Los siguientes parametros solo son modificables a nivel de desarrollo
	 */
	API_PORT: process.env.API_PORT || 12345
};