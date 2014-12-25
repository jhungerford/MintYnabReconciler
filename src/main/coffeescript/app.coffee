define ['ember'], (Ember) ->
	App = Ember.Application.create
		LOG_TRANSITIONS: true
		LOG_TRANSITIONS_INTERNAL: true
		LOG_VIEW_LOOKUPS: true
		LOG_ACTIVE_GENERATION: true
		LOG_BINDINGS: true
		LOG_STACKTRACE_ON_DEPRECATION: true

	App.registerTemplate = (name, template) ->
		Ember.TEMPLATES[name] = Ember.Handlebars.compile(template)

	App
