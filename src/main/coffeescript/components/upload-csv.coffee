define (require) ->
	$ = require('jquery')
	App = require('app')
	Ember = require('ember')

	App.registerTemplate 'components/upload-csv', require('text!/templates/components/upload-csv.hbs')

	uploadTo = (url, csv) ->
		$.ajax(url,
			type: 'PUT'
			contentType: 'text/csv; charset=UTF-8'
			data: csv
			dataType: 'json')

	getErrorReason = (xhr) ->
		if xhr? and xhr.responseJSON? and xhr.responseJSON.reason?
			xhr.responseJSON.reason
		else
			"No response from server"

	App.UploadCsvComponent = Ember.Component.extend
		isInput: true
		isUploading: false
		isUploaded: false

		hasError: false
		errorMessage: null

		header: 'CSV'
		csv: ''
		url: ''
		numRecords: 0

		uploadDisabled: ( -> @get('csv').length is 0).property('csv')

		actions:
			upload: ->
				return false if @get('uploadDisabled')

				@setProperties
					isInput: false
					isUploading: true
					hasError: false

				uploadTo(@get('url'), @get('csv'))
					.done (response) =>
						@setProperties
							numRecords: response.numRecords
							isUploading: false
							isUploaded: true
							hasError: false
						@sendAction "uploaded"
					.fail (xhr) =>
						@setProperties
							errorMessage: getErrorReason(xhr)
							hasError: true
							isUploading: false
							isInput: true
