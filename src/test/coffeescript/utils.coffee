define ['ember'], (Ember) ->
	TestUtils =
		stubAjax: (url, method, json) ->
			Ember.$.mockjax
				url: url
				responseText: JSON.stringify(json)

	TestUtils
