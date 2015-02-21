module.exports = (grunt) ->
	grunt.initConfig
		pkg: grunt.file.readJSON('package.json')

		coffee:
			glob_to_multiple:
				expand: true
				flatten: true
				cwd: 'src/main/coffeescript'
				src: ['*.coffee']
				dest: '/target/classes/web/js/'
				ext: '.js'

		watch:
			files: 'src/main/coffeescript/*.coffee'
			tasks: ['coffee']

	grunt.loadNpmTasks 'grunt-contrib-coffee'
	grunt.loadNpmTasks 'grunt-contrib-watch'

	grunt.registerTask 'default', ['coffee']
