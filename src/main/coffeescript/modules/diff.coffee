define (require) ->
	$ = require('jquery')
	App = require('app')
	Ember = require('ember')
	Dates = require('utils/dates')
	require('components/date-paginator')

	App.registerTemplate 'diff', require('text!/templates/diff/diff.hbs')
	App.registerTemplate 'diff/view', require('text!/templates/diff/diff-view.hbs')
	App.registerTemplate 'diff/loading', require('text!/templates/diff/diff-loading.hbs')
	App.registerTemplate 'diff/error', require('text!/templates/diff/diff-error.hbs')

	App.DiffResponseParser = Ember.Object.extend
		parse: (response) ->
			mintOnly: @parseMintOnly(response.mintOnly)
			ynabOnly: @parseMintOnly(response.ynabOnly)
			incorrect: @parseIncorrect(response.incorrect)
			correct: @parseCorrect(response.correct)

		parseMintOnly: (mintOnly) ->
			($.extend(true, transaction,
				date: Dates.parseYearMonthDay(transaction.date)
			) for transaction in mintOnly)

		parseYnabOnly: (ynabOnly) ->
			($.extend(true, transaction,
				date: Dates.parseYearMonthDay(transaction.date)
			) for transaction in ynabOnly)

		parseIncorrect: (incorrect) ->
			($.extend(true, transaction,
				ynab: { date: Dates.parseYearMonthDay(transaction.ynab.date) },
				closestMints: (
					{ date: Dates.parseYearMonthDay(mint.date) }
				) for mint in transaction.closestMints
			) for transaction in incorrect)

		parseCorrect: (correct) ->
			($.extend(true, transaction,
				mint: { date: Dates.parseYearMonthDay(transaction.mint.date) }
				ynab: { date: Dates.parseYearMonthDay(transaction.ynab.date) }
			) for transaction in correct)

	App.DiffController = Ember.Controller.extend
		earliestMonth: null
		latestMonth: null

	App.DiffViewController = Ember.Controller.extend
		needs: ['diff']

	App.DiffErrorRoute = Ember.Route.extend()

	App.DiffRoute = Ember.Route.extend
		actions:
			error: (error) ->
				console.log("Error loading diff view", error.message, error)
				@transitionTo 'diff.error'

		model: -> $.ajax('/api/v1/transactions/diff/range',
			type: 'GET'
			dataType: 'json')

		setupController: (controller, model) ->
			controller.set('earliestMonth', Dates.asMonth(Dates.parseYearMonthDay(model.earliestDate)))
			controller.set('latestMonth', Dates.asMonth(Dates.parseYearMonthDay(model.latestDate)))

		redirect: (model, transition) ->
			@transitionTo('diff.view', model.latestDate[0], model.latestDate[1]) if transition.targetName is "diff.index"

	App.DiffViewRoute = Ember.Route.extend
		actions:
			error: (error) ->
				console.log("Error loading diff view", error.message)
				@transitionTo 'diff.error'

			changeMonth: (newMonth) ->
				@transitionTo('diff.view', newMonth.get('year'), newMonth.get('month'))

		serialize: (obj) ->
			year: obj.year
			month: obj.month

		model: (params) ->
			@set('currentMonth', Dates.yearMonth(params.year, params.month))
			$.ajax('/api/v1/transactions/diff/' + params.year + '/' + params.month,
				type: 'GET'
				dataType: 'json')
			.then (response) -> App.DiffResponseParser.create().parse(response)

		setupController: (controller, model) ->
			@_super(controller, model)
			controller.set('currentMonth', @get('currentMonth'))

