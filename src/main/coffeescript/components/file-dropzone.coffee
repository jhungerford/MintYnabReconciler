define (require) ->
	App = require('app')
	Ember = require('ember')
	Dropzone = require('dropzone')

	App.FileDropzoneComponent = Ember.Component.extend
		url: undefined

		classNames: ['dropzone']

		createDropzone: ( -> @set('dropzone', new Dropzone('#' + @elementId,
				url: @get('url')
				method: 'PUT'
				acceptedFiles: '.csv'
			))
		).on('didInsertElement')

