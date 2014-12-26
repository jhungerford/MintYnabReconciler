define (require) ->
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'components/upload-csv', require('text!/templates/components/upload-csv.hbs')

	App.UploadCsvComponent = Ember.Component.extend
		header: 'CSV'
		csv: ''
		uploadDisabled: ( -> @get('csv').length is 0).property('csv')

		actions:
			upload: -> @sendAction 'upload', @get('csv')
