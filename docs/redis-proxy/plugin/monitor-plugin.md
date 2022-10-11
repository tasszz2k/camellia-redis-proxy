## MonitorProxyPlugin

### illustrate
* A plugin for statistics of client command distribution and response time for accessing proxy
* In particular, slow queries can be monitored
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
    - monitorPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
#Slow query monitoring threshold, default 2000ms
slow.command.threshold.millis=2000
##Slow query monitoring data is exposed to the public through /monitor by default (refresh data every 60s by default). If you need to push it in real time, you can set a callback (just implement the SlowCommandMonitorCallback interface)
slow.command.monitor.callback.className=com.netease.nim.camellia.redis.proxy.plugin.monitor.DummySlowCommandMonitorCallback

#Other monitoring data (such as the number of requests, rt, etc., are uniformly exposed through the /monitor interface)
#Specially, for rt monitoring, there is a sub-switch, which is enabled by default. If it is disabled, only tps will be counted, and rt will not be counted.
command.spend.time.monitor.enable=true
````