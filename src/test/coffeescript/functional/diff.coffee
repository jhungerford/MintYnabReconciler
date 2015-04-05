define ['ember', 'app', 'test/utils'], (Ember, App, TestUtils) ->
	afterEach: ->
		App.reset()
		Ember.$.mockjaxClear()

	run: ->
		test 'one transaction', 1, ->
			TestUtils.stubAjax('/api/v1/transactions/diff/range', 'GET', {
				earliestDate: [2010, 1, 10]
				latestDate: [2015, 3, 19]
			})

			TestUtils.stubAjax('/api/v1/transactions/diff/2015/3', 'GET', {diffs: [
				TestUtils.diff.correctTransaction(12345, [2015, 3, 2], 'Paycheck')
			]})

			visit('diff').then ->
				TestUtils.diff.verifyRow find('.diff-correct:eq(0)'), '3/2/2015', 'Paycheck', '$123.45', ''

		test 'Transactions are sorted in the UI', 3, ->
			TestUtils.stubAjax('/api/v1/transactions/diff/range', 'GET', {
				earliestDate: [2010, 1, 10]
				latestDate: [2015, 3, 19]
			})

			TestUtils.stubAjax('/api/v1/transactions/diff/2015/3', 'GET', {diffs: [
				TestUtils.diff.correctTransaction(200, [2015, 3, 2], 'Transaction 2')
				TestUtils.diff.correctTransaction(100, [2015, 3, 1], 'Transaction 1')
				TestUtils.diff.correctTransaction(300, [2015, 3, 3], 'Transaction 3')
			]})

			visit('diff').then ->
				TestUtils.diff.verifyRow find('.diff-correct:eq(0)'), '3/3/2015', 'Transaction 3', '$3.00', ''
				TestUtils.diff.verifyRow find('.diff-correct:eq(1)'), '3/2/2015', 'Transaction 2', '$2.00', ''
				TestUtils.diff.verifyRow find('.diff-correct:eq(2)'), '3/1/2015', 'Transaction 1', '$1.00', ''
