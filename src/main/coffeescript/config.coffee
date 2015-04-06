require.config
	deps: ['routes']

	baseUrl: 'web/js'

	paths:
		ember: 'libs/ember/ember'
		handlebars: 'libs/handlebars/handlebars'
		jQuery: 'libs/jquery/jquery'
		text: 'libs/requirejs/text'

	shim:
		jQuery:
			exports: 'jQuery'
			init: -> @.jQuery.noConflict()

		ember:
			deps: ['jQuery', 'handlebars']
			exports: 'Ember'
