require.config
	deps: ['/web/js/config.js'] # testConfig will add test configuration to the base configuration

	paths:
		qUnit: '/test-web/js/libs/qunit/qunit.js'

	shim:
		qUnit:
			exports: 'QUnit'
			init: ->
				QUnit.config.autoload = false
				QUnit.config.autostart = false

