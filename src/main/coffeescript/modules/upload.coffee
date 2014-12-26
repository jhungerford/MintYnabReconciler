define (require) ->
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'upload', require('text!/templates/upload.hbs')

	App.UploadController = Ember.Controller.extend
		mintUrl: 'api/v1/transactions/mint'
		ynabUrl: 'api/v1/transactions/ynab'

		actions:
			uploadedMint: -> console.log("Uploaded mint"); false
			uploadedYnab: -> console.log("Uploaded ynab"); false
