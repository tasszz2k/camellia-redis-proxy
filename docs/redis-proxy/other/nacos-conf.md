## Using nacos to host proxy configuration

### maven
````
<dependency>
     <groupId>com.netease.nim</groupId>
     <artifactId>camellia-redis-proxy-nacos-spring-boot-starter</artifactId>
     <version>1.1.1</version>
</dependency>
````

### Configuration
````yaml
camellia-redis-proxy-nacos:
   enable: false #Whether to get the configuration file from nacos
   server-addr: 127.0.0.1:8848 #nacos address
   nacos-conf: #Other nacos configuration items
     k1: v1
     k2: v2
   conf-file-list:
     - file-name: camellia-redis-proxy.properties #file name
       data-id: camellia-redis-proxy.properties #nacos dataId
       group: camellia #nacos' group
     - file-name: logback.xml #file name
       data-id: logback.xml
       group: camellia
````
The above configuration means to host the two configuration files of proxy, camellia-redis-proxy.properties and logback.xml to nacos

### nacos installation
See: https://nacos.io/en-us/docs/quick-start.html