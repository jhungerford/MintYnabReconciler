# ADD NEW TESTS TO THE REQUIRE ARRAY - THEY WILL AUTOMATICALLY RUN
require ['app', 'ember', 'qUnit',
	# Unit Tests
	'/test-web/js/unit/utils/handlebar-helpers.js'
], (App, Ember, QUnit, tests...) ->
	App.rootElement = '#qunit-fixture' # Hidden property - override css to make it visible

	App.setupForTesting()
	App.injectTestHelpers()

	for test in tests
		if test?.run?
			test.run()
		else
			throw new Error('Test must be an object with a run method.')

	QUnit.load()
	QUnit.start()
