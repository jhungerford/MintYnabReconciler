define ['utils/handlebar-helpers', 'utils/dates'], (Helpers, Dates) ->

	run: ->
		test '4/5/2015', ->
			day = Dates.parseYearMonthDay([2013, 12, 5])
			equal Helpers.formatDay(day), '12/5/2013'

		test 'today', ->
			todayDate = new Date()
			expected = (todayDate.getUTCMonth() + 1) + '/' + todayDate.getUTCDate() + '/' + todayDate.getUTCFullYear()

			day = Dates.today()
			equal Helpers.formatDay(day), expected
