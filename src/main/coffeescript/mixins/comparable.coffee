define ['ember'], (Ember) ->
	ComparableMixin = Ember.Mixin.create
	# OVERRIDE THIS FUNCTION TO DEFINE YOUR COMPARISON
	# Return -1, 0, or 1 if this object is less than, equal to, or greater than other, respectively.
#		compare: (other) -> 0

		gt: (other) -> @compare(other) > 0
		lt: (other) -> @compare(other) < 0
		gte: (other) -> @compare(other) >= 0
		lte: (other) -> @compare(other) <= 0
		eq: (other) -> @compare(other) is 0

	ComparableMixin
