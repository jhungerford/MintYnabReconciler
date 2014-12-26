define (require) ->
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'diff', require('text!/templates/diff.hbs')
