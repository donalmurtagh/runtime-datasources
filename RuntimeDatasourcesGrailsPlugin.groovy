grails.plugins.runtime-datasources

class RuntimeDatasourcesGrailsPlugin {
    def grailsVersion = "3.0.0 > *"
    def pluginExcludes = [
            "grails-app/conf/**",
            "grails-app/controllers/**",
            "grails-app/domain/**",
            "grails-app/i18n/**",
            "grails-app/utils/**",
            "grails-app/views/**",
            "lib/**",
            "scripts/**",
            "web-app/**"
    ]
    def title = "Runtime Datasources Plugin"
    def author = "Dónal Murtagh"
    def authorEmail = "domurtag@yahoo.co.uk "
    def description = 'Allows an application to add or remove JDBC datasources at runtime'
    def documentation = "https://github.com/domurtag/runtime-datasources"
    def license = "APACHE"
    def issueManagement = [system: "GitHub", url: "https://github.com/domurtag/runtime-datasources/issues"]
    def scm = [ url: "https://github.com/domurtag/runtime-datasources" ]
}
