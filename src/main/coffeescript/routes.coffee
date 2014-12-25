define (require) ->
	App = require('app')
	require('modules/application')
	require('modules/upload')

	App.Router.map( ->
		@resource 'upload'
	)
