define [], ->
	Functions =

	# Enumerable Utilities - (object[A<Enumerable], ?) -> ?

	#	Given:
	#		enumerable: Sorted enumerable to group
	#		propFunction: Function to extract the comparison property from each item in the enumerable
	#		compareFunction: Comparison function
	#
	# Example:
	#		arrController = Ember.ArrayController.create({model: [1, 3, 2, 4, 5, 6]});
	#		foldSorted(arrController, identity, sameParity)
	#	Result: [[1, 3], [2, 4], [5], [6]]
		groupSorted: (enumerable, propFunction, compareFunction) ->
			enumerable.reduce((prevAndList, item) ->
				prev = prevAndList.prev
				list = prevAndList.list

				if prev? and compareFunction(propFunction(prev), propFunction(item))
					groupedItems = list[list.length-1]
				else
					groupedItems = list[list.length] = []

				groupedItems[groupedItems.length] = item

				prevAndList.prev = item
				prevAndList
			, {prev: undefined, list: []}).list

	# Given:
	#		enumerable: Enumerable to sum
	# 	propFunction: function to get the numeric value of each item
	#
	# Returns:
	# 	Sum of the items in the enumerable
	#
	# Example:
	# 	arrController = Ember.ArrayController.create({model: [1, 2, 3]});
	#		sum(arrController, identity)
	# Result: 6
		sum: (enumerable, propFunction) ->
			add = (sum, item) -> sum + propFunction(item)
			enumerable.reduce(add, 0)

	# Reduce Functions - (?) -> (object[A], object[A]) -> object[A]

	# Given:
	#		propFunction: function to get the comparable value of each item
	#		compareFunction: function used to compare values that propFunction returns
	#
	#	Returns:
	#		function for reduce that finds the value that compares true against the other elements
	#
	# Example:
	# 	arrController = Ember.ArrayController.create({model: [1, 2, 3]});
	#		arrController.filter(winner(identity, gt), 0)
	#	Result: 3
		winner: (propFunction, compareFunction) ->
			(currentWinner, item) ->
				if compareFunction(propFunction(item), propFunction(currentWinner)) then item else currentWinner

	# Filter functions - (?) -> (object[A]) -> boolean

	# Given:
	#		propFunction: function to extract the comparable value of each item
	#		compareFunction: single-parameter function that compares
	#
	# Example:
	# 	arrController = Ember.ArrayController.create({model: [1, 2, 3]});
	#		arrController.filter(compareFilter(identity, odd));
	#	Result: [1, 3]
		compareFilter: (propFunction, filterFunction) -> (item) -> filterFunction(propFunction(item))

	# Filtering functions - (?) -> (object[A]) -> boolean

	# Given:
	# 	compareFunction: function to compare each item against value
	#		value: value to compare each item against
	#
	#	Returns:
	#		function that takes a single item and compares the item against value
		compareValue: (compareFunction, value) -> (item) -> compareFunction(item, value)

		even: (num) -> (num % 2) is 0
		odd: (num) -> (num % 2) is 1

	# Comparing Functions - (object[A], object[A]) -> boolean
		lt: (item, other) -> item < other
		gt: (item, other) -> item > other
		lte: (item, other) -> item <= other
		gte: (item, other) -> item >= other
		eq: (item, other) -> item is other

		sameParity: (item, other) -> (item % 2) is (other % 2)

		compareLt: (item, other) -> item.lt(other)
		compareGt: (item, other) -> item.gt(other)
		compareLte: (item, other) -> item.lte(other)
		compareGte: (item, other) -> item.gte(other)
		compareEq: (item, other) -> item.eq(other)

		and: (item, other) -> item and other
		or: (item, other) -> item or other

	# Property functions: (object[A]) -> object[B]
		identity: (item) -> item
		prop: (property) -> (item) -> item.get(property)

	# Math functions
		sign: (i) -> if (i is 0) then 0 else (i / Math.abs(i))

	Functions
