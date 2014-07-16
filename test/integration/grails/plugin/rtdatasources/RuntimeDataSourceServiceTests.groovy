package grails.plugin.rtdatasources

import org.apache.tomcat.jdbc.pool.DataSource as TomcatDataSource
import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class RuntimeDataSourceServiceTests extends GroovyTestCase implements ApplicationContextAware {

    RuntimeDataSourceService runtimeDataSourceService
    ApplicationContext applicationContext

    private static final URL = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
    private final DRIVER = "org.h2.Driver"
    private final USERNAME = "sa"
    private final PASSWORD = ""

    void testDataSourceRegistration() {

        String beanName = 'newDataSource'
        TomcatDataSource dataSource = registerDefaultTomcatDataSource(beanName)

        // check the few properties
        def dataSourceProps = dataSource.poolProperties
        assertEquals USERNAME, dataSourceProps.username
        assertEquals PASSWORD, dataSourceProps.password
        assertEquals URL, dataSourceProps.url
        assertEquals DRIVER, dataSourceProps.driverClassName
    }

    void testDuplicateDataSourceRegistration() {

        String beanName = 'anotherDataSource'
        registerDefaultTomcatDataSource(beanName)

        shouldFail(BeanCreationException) {
            registerDefaultTomcatDataSource(beanName)
        }
    }

    void testDataSourceRemoval() {

        String beanName = 'yetAnotherDataSource'
        registerDefaultTomcatDataSource(beanName)
        assertTrue runtimeDataSourceService.removeDataSource(beanName)
        assertFalse runtimeDataSourceService.removeDataSource(beanName)
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
