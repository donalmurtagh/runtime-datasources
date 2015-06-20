package grails.plugin.rtdatasources

import grails.test.mixin.integration.Integration
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.apache.tomcat.jdbc.pool.DataSource as TomcatDataSource
import org.springframework.beans.factory.BeanCreationException
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
class RuntimeDataSourceServiceTests extends Specification {

    @Autowired
    RuntimeDataSourceService runtimeDataSourceService

    private static final URL = "jdbc:h2:mem:runtimeDb"
    private static final DRIVER = "org.h2.Driver"
    private static final USERNAME = "sa"
    private static final PASSWORD = ""

    def 'dataSource addition and removal'() {

        String beanName = 'newDataSource'

        when:
        TomcatDataSource dataSource = registerDefaultTomcatDataSource(beanName)

        then:
        // check a few properties
        def dataSourceProps = dataSource.poolProperties
        USERNAME == dataSourceProps.username
        PASSWORD == dataSourceProps.password
        URL == dataSourceProps.url
        DRIVER == dataSourceProps.driverClassName

        // now remove it twice, the second attempt should return false
        runtimeDataSourceService.removeDataSource(beanName)
        !runtimeDataSourceService.removeDataSource(beanName)
    }

    def 'duplicate dataSource registration causes exception'() {

        String beanName = 'anotherDataSource'
        registerDefaultTomcatDataSource(beanName)

        when:
        registerDefaultTomcatDataSource(beanName)

        then:
        thrown BeanCreationException
    }

    def 'SQL execution'() {

        String beanName = 'newDataSource'
        registerDefaultTomcatDataSource(beanName)
        Sql sql = runtimeDataSourceService.getSql(beanName)

        when:
        def createTableSql = 'create table test(id int primary key, name varchar(255))'
        sql.execute(createTableSql)

        GroovyRowResult queryResult = runtimeDataSourceService.doWithSql(beanName) { Sql queryExecutor ->
            queryExecutor.firstRow('select count(*) from test')
        }

        then:
        0 == queryResult[0]

        cleanup:
        sql.close()
        runtimeDataSourceService.removeDataSource(beanName)
    }

    private TomcatDataSource registerDefaultTomcatDataSource(String beanName) {

        runtimeDataSourceService.addDataSource(beanName, TomcatDataSource) {
            driverClassName = DRIVER
            url = URL
            username = USERNAME
            password = PASSWORD
        }
    }
}
