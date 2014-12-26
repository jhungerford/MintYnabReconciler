define ['app', 'ember', 'jQuery', 'text!/templates/upload.hbs'], (App, Ember, $, uploadTemplate) ->
	App.registerTemplate 'upload', uploadTemplate

	App.UploadController = Ember.Controller.extend
		actions:
			uploadMint: (csv) -> console.log("Upload mint")
			uploadYnab: (csv) -> console.log("Upload ynab")
#				$.ajax('/api/v1/transactions/mint',
#					type: 'POST'
#					contentType: 'text/csv; charset=UTF-8'
#					data: @get('mintcsv')
#					dataType: 'json'
#				).done (response) => console.log("Uploaded")
