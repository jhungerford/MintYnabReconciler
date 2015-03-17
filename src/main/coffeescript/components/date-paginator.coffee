define (require) ->

	App = require 'app'
	Ember = require 'ember'
	Dates = require 'utils/dates'

	App.registerTemplate 'components/date-paginator', require('text!/templates/components/date-paginator.hbs')

	App.DatePaginatorComponent = Ember.Component.extend
		earliestMonth: null
		latestMonth: null

		currentMonth: Dates.asMonth(Dates.now())

		previousYear: ( -> @get('currentMonth').minus(1, 'years').get('year') ).property('currentMonth')
		nextYear: ( -> @get('currentMonth').plus(1, 'years').get('year') ).property('currentMonth')

		previousYearEnabled: ( -> @get('currentMonth').minus(1, 'years').gte(@get('earliestMonth')) ).property('currentMonth')
		nextYearEnabled: ( -> @get('currentMonth').plus(1, 'years').lte(@get('latestMonth')) ).property('currentMonth')

		months: ( ->
			currentMonth = @get('currentMonth')
			latestMonth = @get('latestMonth')
			earliestMonth = @get('earliestMonth')

			['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'].map (month, i) ->
				date = Dates.parseYearMonthDay([currentMonth.get('year'), i + 1, 1])
				{
					name: month
					current: date.eq(currentMonth)
					enabled: date.lte(latestMonth) and date.gte(earliestMonth)
				}
			, @
			).property 'currentMonth'
