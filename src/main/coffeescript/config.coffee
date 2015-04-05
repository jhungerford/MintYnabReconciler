require.config
	deps: ['app', 'routes', 'utils/handlebar-helpers', 'components/upload-csv', 'components/date-paginator']

	baseUrl: 'web/js'

	paths:
		ember: 'libs/ember/ember'
		emberData: 'libs/ember/ember-data'
		handlebars: 'libs/handlebars/handlebars'
		jQuery: 'libs/jquery/jquery'
		text: 'libs/require/text'

	shim:
		jQuery:
			exports: 'jQuery'
			init: -> @.jQuery.noConflict()

		ember:
			deps: ['jQuery', 'handlebars']
			exports: 'Ember'

		emberData:
			deps: ['ember']
			exports: 'DS'

		handlebars:
			exports: 'Handlebars'

