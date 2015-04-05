require.config
	deps: ['/web/js/config.js', '/test-web/js/tests.js'] # testConfig will add test configuration to the base configuration

	paths:
		qUnit: '/test-web/js/libs/qunit/qunit'

	shim:
		qUnit:
			exports: 'QUnit'
			init: ->
				QUnit.config.autoload = false
				QUnit.config.autostart = false

