require.config
	deps: ['mockJax'] # testConfig will add test configuration to the base configuration

	baseUrl: 'web/js'

	paths:
		qUnit: '/web/js/test/libs/qunit/qunit'
		mockJax: '/web/js/test/libs/jquery/jquery.mockjax'

	shim:
		mockJax:
			deps: ['jQuery']

		qUnit:
			exports: 'QUnit'
			init: ->
				QUnit.config.autoload = false
				QUnit.config.autostart = false

