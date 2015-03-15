define (require) ->

	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'components/date-paginator', require('text!/templates/components/date-paginator.hbs')

	App.DataPaginator = Ember.Component.extend()
