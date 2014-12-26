define (require) ->
	$ = require('jQuery')
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'diff', require('text!/templates/diff.hbs')

	App.DiffRoute = Ember.Route.extend
		model: -> $.ajax('/api/v1/transactions/diff',
			type: 'GET'
			dataType: 'json')

