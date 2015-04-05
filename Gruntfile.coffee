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

	grunt.loadNpmTasks 'grunt-contrib-coffee'
	grunt.loadNpmTasks 'grunt-contrib-watch'

	grunt.registerTask 'default', ['coffee']
