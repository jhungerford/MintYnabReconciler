require.config
	deps: ['/web/js/config.js', 'mockJax', 'tests'] # testConfig will add test configuration to the base configuration

	paths:
		qUnit: '/test-web/js/libs/qunit/qunit'
		mockJax: '/test-web/js/libs/jquery/jquery.mockjax'

	shim:
		mockJax:
			deps: ['jQuery']

		qUnit:
			exports: 'QUnit'
			init: ->
				QUnit.config.autoload = false
				QUnit.config.autostart = false

