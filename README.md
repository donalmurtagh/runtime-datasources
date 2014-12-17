[![Build Status](https://travis-ci.org/domurtag/runtime-datasources.svg?branch=master)](https://travis-ci.org/domurtag/runtime-datasources)


## Introduction

Grails plugin that enables an application to add or remove JDBC datasources at runtime, and provides convenience methods
for executing SQL statements against these datasources
 
## Limitations
 
GORM/Hibernate cannot be used with datasources added at runtime, because GORM requires [the mapping between a domain
class and datasource](http://grails.org/doc/latest/guide/conf.html#multipleDatasources) to be defined at compile-time

## Add a DataSource

Dependency-inject the `runtimeDataSourceService` service provided by the plugin and call it like so:

````groovy
import javax.sql.DataSource
import org.apache.tomcat.jdbc.pool.DataSource as JdbcDataSource

DataSource runtimeDataSource = runtimeDataSourceService.addDataSource('myDataSource', JdbcDataSource) {
    driverClassName = 'com.mysql.jdbc.Driver'
    url = 'jdbc:mysql://localhost/example'
    username = 'root'
    password = 'password'
}
````

If successful, the method returns the created datasource. 

### Arguments

1. The name of the Spring bean that will be registered for this datasource. If a Spring bean with this name already
exists, an exception will be thrown
2. Defines the implementation class of the Spring bean. This class must implement the `javax.sql.DataSource` interface. 
3. A closure that defines the properties of this datasource. At a minimum, the properties shown in the example above
should be provided. This closure supports the same properties as the closure that is used to set datasource properties 
at compile-time in <tt>DataSource.groovy</tt>

## Remove a DataSource

The same service that is used to add datasources can also remove them:

````groovy 
runtimeDataSourceService.removeDataSource('myDataSource')
````

The argument should be the name of the datasource's Spring bean. The method returns `true` if the datasource was successfully
removed, or `false` if a datasource Spring bean with this name could not be found.

## Using a Runtime DataSource

A reference to a `DataSource` instance added at runtime can be obtained in one of the following methods

1. The `DataSource` instance is returned upon creation (see examples above)
2. The `DataSource` instance can also be retrieved from the Spring application context, e.g.

````groovy
class MyService implements ApplicationContextAware {

    ApplicationContext applicationContext
    
    private DataSource getRuntimeDataSource(String beanName) {
        
        // the second parameter can be omitted
        applicationContext.getBean(beanName, DataSource)
    }
}
````

### Executing SQL Against a Runtime DataSource

Once you have obtained a reference to a `DataSource` using one of the methods outlined in the previous section, you
can construct a [groovy.sql.Sql](http://groovy.codehaus.org/api/groovy/sql/Sql.html) instance and use that to query/update
the datasource, e.g.

````groovy
class MyService implements ApplicationContextAware {

    ApplicationContext applicationContext
    
    private executeSqlAgainstRuntimeDataSource(String beanName) {
        
        DataSource runtimeDataSource = applicationContext.getBean(beanName, DataSource)
        Sql sql = new Sql(runtimeDataSource)
        
        try {
            // use the Sql instance to execute a query, update data, etc.
        } finally {
            sql.close()
        }
    }
}
````

Alternatively, the aforementioned `runtimeDataSourceService` also provides a couple of convenience methods which makes
the process slightly simpler, e.g.

### Create Sql Instance

The `getSql` method of the service slightly simplifies the process of creating the `Sql` instance for a runtime datasource. 

````groovy
class MyService {

    RuntimeDataSourceService runtimeDataSourceService
    
    private executeSqlAgainstRuntimeDataSource(String beanName) {
        
        Sql sql = runtimeDataSourceService.getSql(beanName)
        
        try {
            // use the Sql instance to execute a query, update data, etc.
        } finally {
            sql.close()
        }
    }
}
```

### Execute Query

The `doWithSql` method of the service simplifies the process of executing SQL statements against a datasource, e.g.

````groovy
class MyService {

    RuntimeDataSourceService runtimeDataSourceService
    
    private executeSqlAgainstRuntimeDataSource(String beanName) {
        
        Integer rowCount = runtimeDataSourceService.doWithSql(beanName) { Sql sql ->
            def queryResult = sql.firstRow('select count(*) from my_table')	  
            queryResult[0]
        }
    }
}
````

Notice that the caller is not responsible for closing the `Sql` instance that is passed to the closure. The value returned
by the closure is also the return value of `doWithSql`.

## Credits

The core of this plugin is based on [this stackoverflow answer](http://stackoverflow.com/a/20634968/2648) posted by 
[Tim Yates](https://github.com/timyates).
