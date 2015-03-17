define ['ember', 'mixins/comparable', 'utils/functions'], (Ember, Comparable, F) ->

	# The timezone math in this module doesn't work over a daylight savings time boundary.

	# Globally available (App.DATES) Date-related constants
	Dates = Ember.Object.create
		now: -> Day.create
			value: new Date().getTime()

		today: -> @morning(new Date().getTime())

		morning: (ms) -> Day.create
			value: Math.floor((ms - @get('localTimezoneMS')) / @intervals.days) * @intervals.days

		intervals:
			seconds: 1000
			minutes: 60*1000
			hours: 60*60*1000
			days: 24*60*60*1000
			weeks: 7*24*60*60*1000

		days: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']
		months: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']

		daysInMonth: (date) ->
			febDays = if @leapYear(date.get('year')) then 29 else 28
			[31, febDays, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][date.get('month') - 1]

		startOfMonth: (date) ->
			mutableDate = new Date(date.get('asMS'))
			mutableDate.setUTCDate(1)
			mutableDate.getUTCDay()

		leapYear: (year) ->
			switch
				when year % 4 isnt 0 then false
				when year % 100 isnt 0 then true
				when year % 400 isnt 0 then false
				else true

		localTimezoneMS: new Date().getTimezoneOffset() * 60*1000 # This doesn't cover DST changes

	Day = Ember.Object.extend Comparable,
		plus: (num, intervalName) ->
			if intervalName is 'months'
				date = new Date(@get('asMS'))
				newValue = date.setUTCMonth(date.getUTCMonth() + num)
			else
				ms = Dates.intervals[intervalName]
				if ms?
					newValue = @get('asMS') + num * ms
				else
					throw new Error(intervalName + ' is not an interval.')

			Day.create
				value: newValue

		minus: (num, intervalName) -> @plus(-1 * num, intervalName)

		asDate: (-> new Date(@get('asMS'))).property('asMS')
		asMS: Ember.computed.alias 'value'

		year: (-> @get('asDate').getUTCFullYear()).property('asDate')
		month: (-> @get('asDate').getUTCMonth() + 1).property('asDate')
		date: (-> @get('asDate').getUTCDate()).property('asDate')
		humanDay: (-> Dates.days[@get('asDate').getUTCDay()]).property('asDate')

		compare: (otherDay) -> F.sign(@get('asMS') - otherDay.get('asMS'))

	Dates
