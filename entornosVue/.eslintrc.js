module.exports = {
	root: true,
	env: {
		node: true
	},
	'extends': [
		'plugin:vue/essential',
		'eslint:recommended'
	],
	rules: {
		'no-console': 'warn',
		'no-debugger': 'warn',
		'no-process-env': 'error',
		'no-trailing-spaces': 'error'
	},
	parserOptions: {
		parser: 'babel-eslint'
	},
	'overrides': [
		{
			'files': ['config/configuration.js'],
			'rules': {
				'no-console': 0,
				'no-process-env': 0
			}
		},
		{
			'files': ['src/service/LoggerService.js'],
			'rules': {
				'no-console': 0
			}
		},
	]
}
