define ['ember', 'app', 'utils/dates'], (Ember, App, Dates) ->
	Helpers =
		formatDay: (day) ->
			day.get('month') + '/' + day.get('date') + '/' + day.get('year')

	Ember.Handlebars.helper 'formatDay', Helpers.formatDay

	Helpers
