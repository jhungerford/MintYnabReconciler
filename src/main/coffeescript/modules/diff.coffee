define (require) ->
	$ = require('jQuery')
	App = require('app')
	Ember = require('ember')
	Dates = require('utils/dates')

	App.registerTemplate 'diff', require('text!/templates/diff/diff.hbs')
	App.registerTemplate 'diff/view', require('text!/templates/diff/diff-view.hbs')
	App.registerTemplate 'diff/loading', require('text!/templates/diff/diff-loading.hbs')
	App.registerTemplate 'diff/error', require('text!/templates/diff/diff-error.hbs')

	App.DiffViewController = Ember.ObjectController.extend
		earliestMonth: ( -> Dates.asMonth(Dates.parseYearMonthDay(@get('earliestDate'))) ).property 'earliestDate'
		latestMonth: ( -> Dates.asMonth(Dates.parseYearMonthDay(@get('latestDate'))) ).property 'latestDate'

	App.DiffIndexRoute = Ember.Route.extend
		redirect: -> @transitionTo 'diff.view'

	App.DiffErrorRoute = Ember.Route.extend()

	App.DiffViewRoute = Ember.Route.extend
		actions:
			error: (error, transition) ->
				console.log("Error loading diff view", error.message)
				@transitionTo 'diff.error'

		model: -> $.ajax('/api/v1/transactions/diff',
			type: 'GET'
			dataType: 'json')

