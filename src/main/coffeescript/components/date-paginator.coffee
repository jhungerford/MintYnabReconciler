define (require) ->

	App = require 'app'
	Ember = require 'ember'
	Dates = require 'utils/dates'

	App.registerTemplate 'components/date-paginator', require('text!/templates/components/date-paginator.hbs')

	App.DatePaginatorComponent = Ember.Component.extend
		earliestMonth: null
		latestMonth: null

		currentMonth: Ember.computed.oneWay 'latestMonth'

		previousYear: ( -> @get('currentMonth').minus(1, 'years').get('year') ).property('currentMonth')
		nextYear: ( -> @get('currentMonth').plus(1, 'years').get('year') ).property('currentMonth')

		previousYearEnabled: ( -> @get('currentMonth').minus(1, 'years').gte(@get('earliestMonth')) ).property('currentMonth') # TODO: bug - should check if the last month of the previous year is gte earliestMonth
		nextYearEnabled: ( -> @get('currentMonth').plus(1, 'years').lte(@get('latestMonth')) ).property('currentMonth') # TODO: bug - should check if the first month of the next year is lte latestMonth

		months: ( ->
			currentMonth = @get('currentMonth')
			latestMonth = @get('latestMonth')
			earliestMonth = @get('earliestMonth')

			['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'].map (month, i) ->
				date = Dates.parseYearMonthDay([currentMonth.get('year'), i + 1, 1])
				{
					name: month
					month: date
					current: date.eq(currentMonth)
					enabled: date.lte(latestMonth) and date.gte(earliestMonth)
				}
			, @
			).property 'currentMonth'

		actions:
			selectMonth: (month) ->
				@set('currentMonth', month.month) if month.enabled
				@sendAction 'select', @get('currentMonth')
				false

			# TODO: should previous and next year actually update the selected month?  or should they change the year and make the current month go off the paginator?
			previousYear: ->
				@set('currentMonth', @get('currentMonth').minus(1, 'years')) if @get('previousYearEnabled')
				false

			nextYear: ->
				@set('currentMonth', @get('currentMonth').plus(1, 'years')) if @get('nextYearEnabled')
				false
