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

		test '$0.01 cents', -> equal Helpers.formatCents(1), '$0.01'
		test '$0.10 cents', -> equal Helpers.formatCents(10), '$0.10'
		test '$3.50 cents', -> equal Helpers.formatCents(350), '$3.50'
		test '$1000.57 cents', -> equal Helpers.formatCents(100057), '$1000.57'
