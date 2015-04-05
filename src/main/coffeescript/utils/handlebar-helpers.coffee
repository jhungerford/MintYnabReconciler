define ['ember', 'app', 'utils/dates'], (Ember, App, Dates) ->
	Helpers =
		formatDay: (day) -> '2015-04-05'

	Ember.Handlebars.helper 'formatDay', Helpers.formatDay

	Helpers
