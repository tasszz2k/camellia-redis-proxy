## HotKeyProxyPlugin

### illustrate
* A Plugin for monitoring hot keys, which supports dynamic setting of thresholds
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
    - hotKeyPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties

#switch
hot.key.monitor.enable=true
#Hot key monitors the capacity of the LRU counter, generally does not need to be configured
hot.key.monitor.cache.max.capacity=100000
#Time window for hot key monitoring statistics, default 1000ms
hot.key.monitor.counter.check.millis=1000
#Hot key monitoring statistics exceed how many thresholds in the time window, determine as hot key, default 500
hot.key.monitor.counter.check.threshold=500
#How many hot keys are reported at most in a single cycle, the default is 32 (top)
hot.key.monitor.max.hot.key.count=32

###Tenant level configuration (bid=1, bgroup=default)
#switch
1.default.hot.key.monitor.enable=true
#Hot key monitors the capacity of the LRU counter, generally does not need to be configured
1.default.hot.key.monitor.cache.max.capacity=100000
#Time window for hot key monitoring statistics, default 1000ms
1.default.hot.key.check.counter.check.millis=1000
#Hot key monitoring statistics exceed how many thresholds in the time window, determine as hot key, default 500
1.default.hot.key.monitor.counter.check.threshold=500
#How many hot keys are reported at most in a single cycle, the default is 32 (top)
1.default.hot.key.monitor.monitor.max.hot.key.count=32

##The monitoring data is exposed to the outside through /monitor by default (the data is refreshed every 60s by default). If you need to push it in real time, you can set a callback (just implement the HotKeyMonitorCallback interface)
###The default callback does not do anything
hot.key.monitor.callback.className=com.netease.nim.camellia.redis.proxy.plugin.hotkey.DummyHotKeyMonitorCallback
````