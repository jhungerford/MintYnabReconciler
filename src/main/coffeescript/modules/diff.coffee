define (require) ->
	$ = require('jQuery')
	App = require('app')
	Ember = require('ember')
	Dates = require('utils/dates')

	App.registerTemplate 'diff', require('text!/templates/diff/diff.hbs')
	App.registerTemplate 'diff/view', require('text!/templates/diff/diff-view.hbs')
	App.registerTemplate 'diff/loading', require('text!/templates/diff/diff-loading.hbs')
	App.registerTemplate 'diff/error', require('text!/templates/diff/diff-error.hbs')

	App.DiffController = Ember.ObjectController.extend
		earliestMonth: null
		latestMonth: null

	App.DiffViewController = Ember.ObjectController.extend
		needs: ['diff']

	App.DiffTransactionController = Ember.ObjectController.extend
		isCorrect: (-> @get('differenceType') is 'Correct' ).property('differenceType')
		date: (->
			ynabDate = @get('ynabDate')
			if ynabDate? then ynabDate else @get('mintDate')
		).property('mintDate', 'ynabDate')
		payee: (->
			'Safeway'
		).property('mintTransaction', 'ynabTransaction')
		amount: (->
			'$3.50'
		).property('mintCents', 'ynabCents')
		description: (->
		).property('differenceType')

	App.DiffErrorRoute = Ember.Route.extend()

	App.DiffRoute = Ember.Route.extend
		actions:
			error: (error) ->
				console.log("Error loading diff view", error.message)
				@transitionTo 'diff.error'

		model: -> $.ajax('/api/v1/transactions/diff/range',
			type: 'GET'
			dataType: 'json')

		setupController: (controller, model) ->
			controller.set('earliestMonth', Dates.asMonth(Dates.parseYearMonthDay(model.earliestDate)))
			controller.set('latestMonth', Dates.asMonth(Dates.parseYearMonthDay(model.latestDate)))

		redirect: (model) ->
			console.log('redirect', model)
			@transitionTo('diff.view', model.latestDate[0], model.latestDate[1])

	App.DiffViewRoute = Ember.Route.extend
		actions:
			error: (error) ->
				console.log("Error loading diff view", error.message)
				@transitionTo 'diff.error'

		serialize: (obj) ->
			year: obj.year
			month: obj.month

		model: (params) ->
			$.ajax('/api/v1/transactions/diff/' + params.year + '/' + params.month,
				type: 'GET'
				dataType: 'json')
