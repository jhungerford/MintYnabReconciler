require.config
	deps: ['routes']

	baseUrl: 'web/js'

	paths:
		dropzone: 'libs/dropzone/dropzone'
		ember: 'libs/ember/ember'
		emberTemplateCompiler: 'libs/ember/ember-template-compiler'
		handlebars: 'libs/handlebars/handlebars'
		jquery: 'libs/jquery/jquery'
		text: 'libs/requirejs/text'

	shim:
		jquery:
			exports: 'jquery'
			init: -> @.jQuery.noConflict()

		ember:
			deps: ['jquery', 'emberTemplateCompiler', 'handlebars']
			exports: 'Ember'
