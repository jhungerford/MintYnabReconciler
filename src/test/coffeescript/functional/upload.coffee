define ['ember', 'app'], (Ember, App) ->
	afterEach: ->
		App.reset()
		Ember.$.mockjaxClear()

	run: ->
		test 'visit /, should redirect to upload', ->
			visit('/').then ->
				equal find('h1:eq(0)').text(), 'Mint CSV'
				equal find('h1:eq(1)').text(), 'Ynab CSV'
