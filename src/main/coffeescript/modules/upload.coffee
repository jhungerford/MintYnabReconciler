define (require) ->
	App = require('app')
	Ember = require('ember')
	require('components/upload-csv')
	require('components/file-dropzone')

	App.registerTemplate 'upload', require('text!/templates/upload.hbs')

	App.UploadController = Ember.Controller.extend
		mintUrl: 'api/v1/transactions/mint'
		ynabUrl: 'api/v1/transactions/ynab'

		mintUploaded: false
		ynabUploaded: false

		diffWhenBothUploaded: (->
			@transitionToRoute('diff') if @get('mintUploaded') and @get('ynabUploaded')
		).observes('mintUploaded', 'ynabUploaded')

		actions:
			uploadedMint: -> @set('mintUploaded', true)
			uploadedYnab: -> @set('ynabUploaded', true)
