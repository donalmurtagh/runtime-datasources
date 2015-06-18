// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    error 'org.codehaus.groovy.grails',
            'org.springframework',
            'org.hibernate'

    debug 'grails.plugin.rtdatasources'
}

dataSource {
    pooled = true
    driverClassName = 'org.h2.Driver'
    username = 'sa'
    password = ''
    dbCreate = 'update'
    url = 'jdbc:h2:mem:testDb'
}

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
}