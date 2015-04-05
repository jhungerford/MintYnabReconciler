define (require) ->
	App = require('app')
	require('modules/application')
	require('modules/upload')
	require('modules/diff')
	require('utils/handlebar-helpers')

	App.Router.map( ->
		@resource 'upload'
		@resource 'diff', ->
			@route 'view', {path: '/view/:year/:month'}
			@route 'loading'
			@route 'error'
	)
