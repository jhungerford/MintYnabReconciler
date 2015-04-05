define ['ember'], (Ember) ->
	Helpers =
		formatDay: (day) ->
			day.get('month') + '/' + day.get('date') + '/' + day.get('year')

		formatCents: (cents) -> '$' + (cents / 100).toFixed(2)

	Ember.Handlebars.helper 'formatDay', Helpers.formatDay
	Ember.Handlebars.helper 'formatCents', Helpers.formatCents

	Helpers
