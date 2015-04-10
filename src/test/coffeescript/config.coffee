require.config
	deps: ['mockjax'] # testConfig will add test configuration to the base configuration

	baseUrl: 'web/js'

	paths:
		qunit: '/web/js/test/libs/qunit/qunit'
		mockjax: '/web/js/test/libs/jquery.mockjax'

	shim:
		mockjax:
			deps: ['jquery']

		qunit:
			exports: 'QUnit'
			init: ->
				QUnit.config.autoload = false
				QUnit.config.autostart = false

