package grails.plugin.rtdatasources

import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import org.apache.tomcat.jdbc.pool.DataSource as TomcatDataSource
import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class RuntimeDataSourceServiceTests extends GroovyTestCase implements ApplicationContextAware {

    RuntimeDataSourceService runtimeDataSourceService
    ApplicationContext applicationContext

    private static final URL = "jdbc:h2:mem:runtimeDb"
    private static final DRIVER = "org.h2.Driver"
    private static final USERNAME = "sa"
    private static final PASSWORD = ""

    void testDataSourceAdditionAndRemoval() {

        String beanName = 'newDataSource'
        TomcatDataSource dataSource = registerDefaultTomcatDataSource(beanName)

        // check a few properties
        def dataSourceProps = dataSource.poolProperties
        assertEquals USERNAME, dataSourceProps.username
        assertEquals PASSWORD, dataSourceProps.password
        assertEquals URL, dataSourceProps.url
        assertEquals DRIVER, dataSourceProps.driverClassName

        // now remove it twice, the second attempt should return false
        assertTrue runtimeDataSourceService.removeDataSource(beanName)
        assertFalse runtimeDataSourceService.removeDataSource(beanName)
    }

    void testDuplicateDataSourceRegistration() {

        String beanName = 'anotherDataSource'
        registerDefaultTomcatDataSource(beanName)

        shouldFail(BeanCreationException) {
            registerDefaultTomcatDataSource(beanName)
        }
    }

    void testSqlExecution() {

        String beanName = 'newDataSource'
        registerDefaultTomcatDataSource(beanName)
        Sql sql = runtimeDataSourceService.getSql(beanName)

        try {
            def createTableSql = 'create table test(id int primary key, name varchar(255))'
            sql.execute(createTableSql)

            GroovyRowResult queryResult = runtimeDataSourceService.doWithSql(beanName) { Sql queryExecutor ->
                queryExecutor.firstRow('select count(*) from test')
            }

            assertEquals 0, queryResult[0]

        } finally {
            sql.close()
            runtimeDataSourceService.removeDataSource(beanName)
        }
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
