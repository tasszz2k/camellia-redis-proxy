## BigKeyProxyPlugin

### Illustrate
* A Plugin for monitoring large keys, which supports dynamic setting of thresholds
* For STRING, the number of bytes of value will be monitored
* For SET/HASH/ZSET/LIST, the size of the set will be monitored
* Because it is a monitoring plug-in, it is also controlled by the monitor-enable master switch

### Enable method
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  console-port: 16379 #console port, the default is 16379, if set to -16379, there will be a random available port, if set to 0, the console will not be started
  password: pass123 #proxy password, if a custom client-auth-provider-class-name is set, the password parameter is invalid
  monitor-enable: true #Whether to enable monitoring
  monitor-interval-seconds: 60 #Monitor callback interval
  plugins: #Use yml to configure plugins, built-in plugins can be enabled directly using aliases, custom plugins need to configure the full class name
    - bigKeyPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
#switch
big.key.monitor.enable=true
#Tenant level switch (bid=1, bgroup=default)
1.default.big.key.monitor.enable=true

#threshold
##Default 2M
big.key.monitor.string.threshold=2097152
##default 5000
big.key.monitor.hash.threshold=5000
big.key.monitor.set.threshold=5000
big.key.monitor.zset.threshold=5000
big.key.monitor.list.threshold=5000

#threshold (tenant level)
##Default 2M
1.default.big.key.monitor.string.threshold=2097152
##default 5000
1.default.big.key.monitor.hash.threshold=5000
1.default.big.key.monitor.set.threshold=5000
1.default.big.key.monitor.zset.threshold=5000
1.default.big.key.monitor.list.threshold=5000

##The monitoring data is exposed to the outside through /monitor by default (the data is refreshed every 60s by default). If you need to push it in real time, you can set a callback (you can implement the BigKeyMonitorCallback interface)
###The default callback does not do anything
big.key.monitor.callback.className=com.netease.nim.camellia.redis.proxy.plugin.bigkey.DummyBigKeyMonitorCallback
````