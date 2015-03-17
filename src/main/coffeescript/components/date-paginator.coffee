define (require) ->

	App = require 'app'
	Ember = require 'ember'
	Dates = require 'utils/dates'

	App.registerTemplate 'components/date-paginator', require('text!/templates/components/date-paginator.hbs')

	App.DataPaginator = Ember.Component.extend
		earliestDate: null
		latestDate: null
