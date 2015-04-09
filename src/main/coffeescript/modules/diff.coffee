define (require) ->
	$ = require('jQuery')
	App = require('app')
	Ember = require('ember')
	Dates = require('utils/dates')
	Helpers = require('utils/handlebar-helpers')
	require('components/date-paginator')

	App.registerTemplate 'diff', require('text!/templates/diff/diff.hbs')
	App.registerTemplate 'diff/view', require('text!/templates/diff/diff-view.hbs')
	App.registerTemplate 'diff/loading', require('text!/templates/diff/diff-loading.hbs')
	App.registerTemplate 'diff/error', require('text!/templates/diff/diff-error.hbs')

	App.TransactionDiff = Ember.Object.extend
		isCorrect: (-> @get('differenceType') is 'Correct' ).property('differenceType')

		date: (->
			ynabDate = @get('ynabDate')
			if ynabDate? then ynabDate else @get('mintDate')
		).property('mintDate', 'ynabDate')

		payee: (->
			ynabTransaction = @get('ynabTransaction')
			if ynabTransaction? then ynabTransaction else @get('mintTransaction')
		).property('mintTransaction', 'ynabTransaction')

		cents: (->
			ynabCents = @get('ynabCents')
			if ynabCents > 0 then ynabCents else @get('mintCents')
		).property('mintCents', 'ynabCents')

		description: (->
			switch @get('differenceType')
				when 'Correct' then ''
				when 'Incorrect' then 'Amount is ' + Helpers.formatCents(@get('mintCents')) + ' in mint and ' + Helpers.formatCents(@get('ynabCents')) + ' in ynab'
				when 'MintOnly' then 'Transaction is only in Mint'
				when 'YnabOnly' then 'Transaction is only in Ynab'
		).property('differenceType')

	App.DiffController = Ember.ObjectController.extend
		earliestMonth: null
		latestMonth: null
		currentMonth: null

	App.DiffViewController = Ember.ArrayController.extend
		needs: ['diff']
		sortProperties: ['date']
		sortAscending: false

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
			controller.set('currentMonth', Dates.asMonth(Dates.parseYearMonthDay(model.latestDate)))
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

			changeMonth: (newMonth) ->
				@controllerFor('diff').set('currentMonth', newMonth)
				@transitionTo('diff.view', newMonth.get('year'), newMonth.get('month'))

		serialize: (obj) ->
			year: obj.year
			month: obj.month

		model: (params) ->
			$.ajax('/api/v1/transactions/diff/' + params.year + '/' + params.month,
				type: 'GET'
				dataType: 'json')
			.then (response) ->
				(App.TransactionDiff.create($.extend(diff,
					mintDate: Dates.parseYearMonthDay(diff.mintDate)
					ynabDate: Dates.parseYearMonthDay(diff.ynabDate)
				)) for diff in response.diffs)
