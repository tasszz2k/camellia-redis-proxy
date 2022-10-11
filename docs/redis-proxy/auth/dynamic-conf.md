### Dynamic configuration
If you want the routing configuration of your proxy to be dynamically changed, such as originally routed to redisA, and then dynamically switched to redisB, then you need an additional configuration file and reference it in application.yml, as follows:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: complex
      dynamic: true
      check-interval-millis: 3000
      json-file: resource-table.json
````
The configuration above means:
* The routing and forwarding rules of the proxy come from a configuration file (because the file can be customized to configure double writing, fragmentation and various combinations, so it is called a complex complex), called resource-table.json
* dynamic=true means that the configuration is dynamically updated. At this time, the proxy will regularly check whether the resource-table.json file has changed (the default interval is 5s, and the above figure is configured with 3s). If there is a change, it will be reloaded again.
* By default, the proxy will first go to the classpath to find a configuration file named resource-table.json
* In addition, you can also directly configure an absolute path, the proxy will automatically recognize this situation, as follows:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: complex
      dynamic: true
      check-interval-millis: 3000
      json-file: /home/xxx/resource-table.json
````