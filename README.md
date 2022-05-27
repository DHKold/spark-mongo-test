# spark-mongo-test

Testing the spark mongodb connector with logging.

## Error when using logging LEVEL <= INFO

When using the info level (logback.xml) :
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="info">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

Running the application results in never-ending run with the following error:

```
Exception in thread "main" java.lang.UnsupportedOperationException: Unspecialised MongoConfig. Use `mongoConfig.toReadConfig()` or `mongoConfig.toWriteConfig()` to specialize
	at com.mongodb.spark.sql.connector.config.MongoConfig.getDatabaseName(MongoConfig.java:201)
	at com.mongodb.spark.sql.connector.config.MongoConfig.getNamespace(MongoConfig.java:196)
	at com.mongodb.spark.sql.connector.MongoTable.name(MongoTable.java:99)
	at org.apache.spark.sql.execution.datasources.v2.DataSourceV2Relation.name(DataSourceV2Relation.scala:66)
	at org.apache.spark.sql.execution.datasources.v2.V2ScanRelationPushDown$$anonfun$pushDownFilters$1.$anonfun$applyOrElse$2(V2ScanRelationPushDown.scala:65)
	at org.apache.spark.internal.Logging.logInfo(Logging.scala:57)
  [...]
```

It seems the logging fails because the default implementation of MongoConfig.getDatabaseName() throws this error.

## No error when using logging LEVEL > INFO

```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    <root level="error">
        <appender-ref ref="CONSOLE"/>
    </root>
</configuration>
```

No error, the application runs fine and output the expected joined results:

```
+--------------------+---+--------+--------------------+---+----+-------+-------+
|                 _id| id|    name|                 _id| id|kind|   name|ownerId|
+--------------------+---+--------+--------------------+---+----+-------+-------+
|6290aeef2dec7e204...|  1|  Albert|6290aef02dec7e204...|  1| dog|   Doug|      1|
|6290aeef2dec7e204...|  2| Bernard|6290aef02dec7e204...|  2| dog|  Frank|      2|
|6290aeef2dec7e204...|  2| Bernard|6290aef02dec7e204...|  3| cat|Mr Meow|      2|
|6290aeef2dec7e204...|  3|Caroline|6290aef02dec7e204...|  4|bird|  Pauly|      3|
|6290aeef2dec7e204...|  4|   David|6290aef02dec7e204...|  5|rock| Dwayne|      4|
+--------------------+---+--------+--------------------+---+----+-------+-------+
```