#camellia-dashboard
## Introduction
The proxy configuration of camellia-core can be managed and issued, so that the proxy configuration can be updated dynamically
Provides a spring-boot-starter to quickly build a dashboard

## noun description
bid represents the business type, and bgroup represents the business grouping
bid+bgroup only determines a configuration

## steps
* Depends on mysql for storage and redis for cache
* Refer to samples to start camellia-dashboard and visit http://127.0.0.1:8080/swagger-ui.html
* First call /camellia/admin/createResourceTable to create a configuration and get the configuration tid (the configuration can be a json or a single redis address, see: [Configuration Example](samples.md))
* Then call /camellia/admin/createOrUpdateTableRef to create a bid+bgroup to tid reference
* When the client is initialized, it will request the dashboard to obtain the configuration and MD5 value according to bid+bgroup, and use MD5 to make regular requests to check whether the configuration has been updated (default 5s)

## maven dependencies
````
<dependency>
  <groupId>com.netease.nim</groupId>
  <artifactId>camellia-dashboard-spring-boot-starter</artifactId>
  <version>a.b.c</version>
</dependency>
````
## example
[Database table creation statement](table.sql)
[Sample source](/camellia-samples/camellia-dashboard-samples)
[Configuration example](samples.md)