define ['ember', 'app', 'test/utils'], (Ember, App, TestUtils) ->
	afterEach: ->
		App.reset()
		Ember.$.mockjaxClear()

	run: ->
		test 'one transaction', 3, ->
			TestUtils.stubAjax('/api/v1/transactions/diff/range', 'GET', {
				earliestDate: [2010, 1, 10]
				latestDate: [2015, 3, 19]
			})

			TestUtils.stubAjax('/api/v1/transactions/diff/2015/3', 'GET', {diffs: [{
				differenceType: "Correct"
				mintCents: 12345
				mintDate: [2015, 3, 2]
				mintId: "mint-asdf"
				mintTransaction: "Income: employer"
				ynabCents: 12345
				ynabDate: [2015, 3, 2]
				ynabId: "abcd"
				ynabTransaction: "Paycheck"
			}]})

			visit('diff').then ->
				equal '3/2/2015', find('.diff-correct > .columns:eq(0)').text()
				equal 'Paycheck', find('.diff-correct > .columns:eq(1)').text()
				equal '$123.45', find('.diff-correct > .columns:eq(2)').text()
