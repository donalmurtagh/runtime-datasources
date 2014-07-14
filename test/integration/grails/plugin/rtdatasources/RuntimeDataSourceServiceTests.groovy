package grails.plugin.rtdatasources

import org.apache.tomcat.jdbc.pool.DataSource as TomcatDataSource
import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class RuntimeDataSourceServiceTests extends GroovyTestCase implements ApplicationContextAware {

    RuntimeDataSourceService runtimeDataSourceService
    ApplicationContext applicationContext


    void testDataSourceRegistration() {

        String beanName = 'newDataSource'
        TomcatDataSource dataSource = registerDefaultTomcatDataSource(beanName)

        // check the few properties
        def dataSourceProps = dataSource.poolProperties
        assertEquals 'root', dataSourceProps.username
        assertEquals 'password', dataSourceProps.password
        assertEquals 'jdbc:mysql://localhost/example', dataSourceProps.url
        assertEquals 'com.mysql.jdbc.Driver', dataSourceProps.driverClassName
    }

    void testDuplicateDataSourceRegistration() {

        String beanName = 'anotherDataSource'
        registerDefaultTomcatDataSource(beanName)

        shouldFail(BeanCreationException) {
            registerDefaultTomcatDataSource(beanName)
        }
    }

    void testDataSourceUnregistration() {

        String beanName = 'yetAnotherDataSource'
        registerDefaultTomcatDataSource(beanName)
        assertTrue runtimeDataSourceService.unregisterDataSource(beanName)
        assertFalse runtimeDataSourceService.unregisterDataSource(beanName)
    }

    private TomcatDataSource registerDefaultTomcatDataSource(String beanName) {

        runtimeDataSourceService.registerDataSource(beanName, {
            driverClassName = 'com.mysql.jdbc.Driver'
            url = 'jdbc:mysql://localhost/example'
            username = 'root'
            password = 'password'
        })

        applicationContext.getBean(beanName, TomcatDataSource)
    }
}
