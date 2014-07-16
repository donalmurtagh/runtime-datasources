class RuntimeDatasourcesGrailsPlugin {

    // the plugin version
    def version = "0.2"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/conf",
            "grails-app/controllers",
            "grails-app/domain",
            "grails-app/i18n",
            "grails-app/utils",
            "grails-app/views",
            "lib",
            "scripts",
            "web-app"
    ]

    def title = "Runtime Datasources Plugin" // Headline display name of the plugin
    def author = "Dónal Murtagh"
    def authorEmail = "domurtag@yahoo.co.uk "
    def description = 'Allows an application to add or remove JDBC datasources at runtime'

    // URL to the plugin's documentation
    def documentation = "https://github.com/domurtag/runtime-datasources"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Location of the plugin's issue tracker.
    def issueManagement = [system: "GitHub", url: "https://github.com/domurtag/runtime-datasources/issues"]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/domurtag/runtime-datasources" ]

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { ctx ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }

    def onShutdown = { event ->
    }
}
