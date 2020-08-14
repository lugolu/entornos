module.exports = {
	'env': {
		'commonjs': true,
		'es6': true,
		'node': true
	},
	'extends': 'eslint:recommended',
	'globals': {
		'Atomics': 'readonly',
		'SharedArrayBuffer': 'readonly',
		'after': 'readonly',
		'before': 'readonly',
		'describe': 'readonly',
		'files': 'readonly',
		'it': 'readonly',
		'next': 'readonly'
	},
	'parserOptions': {
		'ecmaVersion': 2018
	},
	'rules': {
		'indent': [
			'error',
			'tab'
		],
		'quotes': [
			'warn',
			'single'
		],
		'semi': [
			'warn',
			'always'
		],
		'no-console': 'warn',
		'global-require': 2,
		'no-shadow': 2,
		'no-process-env': 2,
		'no-param-reassign': 2,
		'no-magic-numbers': 2,
		'no-empty-function': 2,
		'no-duplicate-imports': 2,
		'no-trailing-spaces': 2,
		'no-unused-expressions': 2,
		'block-scoped-var': 2
	},
	'overrides': [
		{
			'files': ['common/database.js'],
			'rules': {
				'no-console': 0
			}
		},
		{
			'files': ['config/config.js'],
			'rules': {
				'no-magic-numbers': 0,
				'no-process-env': 0
			}
		},
		{
			'files': ['server.js'],
			'rules': {
				'no-console': 0,
				'no-magic-numbers': 0
			}
		},
		{
			'files': ['test/**/*.js'],
			'rules': {
				'no-magic-numbers': 0,
				'no-unused-expressions': 0
			}
		}
	],
};