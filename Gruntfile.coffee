module.exports = (grunt) ->
	grunt.initConfig
		pkg: grunt.file.readJSON('package.json')

		coffee:
			glob_to_multiple:
				expand: true
				flatten: false
				cwd: 'src/main/coffeescript'
				src: ['**/*.coffee']
#				dest: 'target/classes/web/js/'
				dest: 'target/scala-2.10/classes/web/js' # Needs to match module output path when running out of IntelliJ
				ext: '.js'

		watch:
			files: 'src/main/coffeescript/**/*.coffee'
			tasks: ['coffee']

	grunt.loadNpmTasks 'grunt-contrib-coffee'
	grunt.loadNpmTasks 'grunt-contrib-watch'

	grunt.registerTask 'default', ['coffee']
