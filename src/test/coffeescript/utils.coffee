define ['ember', 'jquery'], (Ember, $) ->
	TestUtils =
		stubAjax: (url, method, json) ->
			Ember.$.mockjax
				url: url
				responseText: JSON.stringify(json)

		diff:
			correctTransaction: (cents, date, name) ->
				differenceType: "Correct"
				mintCents: cents
				mintDate: date
				mintId: "mint-asdf"
				mintTransaction: name
				ynabCents: cents
				ynabDate: date
				ynabId: "abcd"
				ynabTransaction: name

			verifyRow: (row, expectedDate, expectedPayee, expectedAmount, expectedDescription) ->
				expected = [expectedDate, expectedPayee, expectedAmount, expectedDescription]
				actual = ($(column).text() for column in row.find('.columns'))
				deepEqual expected, actual

	TestUtils
