module.exports = (grunt) ->
	grunt.initConfig
		pkg: grunt.file.readJSON('package.json')

		coffee:
			sources:
				expand: true
				flatten: false
				cwd: 'src/main/coffeescript'
				src: ['**/*.coffee']
				dest: 'target/scala-2.10/classes/web/js' # Needs to match module output path when running out of IntelliJ
				ext: '.js'

			tests:
				expand: true
				flatten: false
				cwd: 'src/test/coffeescript'
				src: ['**/*.coffee']
				dest: 'target/scala-2.10/test-classes/test-web/js'
				ext: '.js'

		watch:
			files: ['src/main/coffeescript/**/*.coffee', 'src/test/coffeescript/**/*.coffee']
			tasks: ['coffee:sources', 'coffee:tests']

		bower:
			install:
				copy: false

		copy:
			bower:
				files: [
					{src: 'bower_components/ember/ember.js', dest: 'src/main/resources/web/js/libs/ember/ember.js'}
					{src: 'bower_components/ember/ember-template-compiler.js', dest: 'src/main/resources/web/js/libs/ember/ember-template-compiler.js'}
					{src: 'bower_components/handlebars/handlebars.js', dest: 'src/main/resources/web/js/libs/handlebars/handlebars.js'}
					{src: 'bower_components/jquery/dist/jquery.js', dest: 'src/main/resources/web/js/libs/jquery/jquery.js'}
					{src: 'bower_components/requirejs/require.js', dest: 'src/main/resources/web/js/libs/requirejs/require.js'}
					{src: 'bower_components/requirejs-text/text.js', dest: 'src/main/resources/web/js/libs/requirejs/text.js'}
					{src: 'bower_components/foundation/css/foundation.css', dest: 'src/main/resources/web/css/foundation.css'}
					{src: 'bower_components/foundation/css/foundation.css.map', dest: 'src/main/resources/web/css/foundation.css.map'}
				]

	grunt.loadNpmTasks 'grunt-bower-task'
	grunt.loadNpmTasks 'grunt-contrib-copy'
	grunt.loadNpmTasks 'grunt-contrib-coffee'
	grunt.loadNpmTasks 'grunt-contrib-watch'

	grunt.registerTask 'default', ['coffee']
	grunt.registerTask 'bower', ['bower:install', 'copy:bower']
