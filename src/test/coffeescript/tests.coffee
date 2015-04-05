define ['app', 'ember', 'qUnit'], (App, Ember, QUnit) ->

	# ADD NEW TESTS HERE.
	# THIS CLASS WILL AUTOMATICALLY LOAD THEM, GROUP THEM INTO MODULES, AND RUN THEM.
	# TESTS MUST HAVE A 'run' METHOD, AND MAY HAVE 'beforeEach' AND/OR 'afterEach' METHODS.
	unitTests = [
		'utils/handlebar-helpers'
	]

	integrationTests = [
		'upload',
		'diff'
	]

	runTests = (type, paths) ->
		testPaths = ('test/' + type + '/' + path for path in paths)

		require testPaths, (tests...) ->
			for test, i in tests
				if test.run?
					config = {}
					config.beforeEach = test.beforeEach if test.beforeEach?
					config.afterEach = test.afterEach if test.afterEach?

					module type + ': ' + paths[i], config

					test.run()
				else
					throw new Error('Test ' + paths[i] + ' must be an object with a run method.')

	Ember.$.mockjaxSettings.logging = true
	Ember.$.mockjaxSettings.throwUnmocked = true
	Ember.$.mockjaxSettings.responseTime = 0

	App.rootElement = '#qunit-fixture' # Hidden property - override css to make it visible

	App.setupForTesting()
	App.injectTestHelpers()

	runTests('unit', unitTests)
	runTests('integration', integrationTests)

	QUnit.load()
	QUnit.start()
