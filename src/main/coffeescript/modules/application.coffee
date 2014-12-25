define ['app', 'ember', 'text!/templates/application.hbs'], (App, Ember, applicationTemplate) ->
	App.registerTemplate 'application', applicationTemplate

	App.IndexRoute = Ember.Route.extend
		redirect: -> @transitionTo 'upload'
