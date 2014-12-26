define (require) ->
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'components/upload-csv', require('text!/templates/components/upload-csv.hbs')

	App.UploadCsvComponent = Ember.Component.extend
		header: Ember.computed.defaultTo 'CSV'
		csv: ''

		actions:
			upload: -> @sendAction 'upload', @get('csv')
