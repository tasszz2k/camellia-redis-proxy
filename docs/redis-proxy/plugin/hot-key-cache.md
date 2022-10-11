## HotKeyCacheProxyPlugin

### illustrate
* A Plugin to support hot key caching
* Only GET requests are supported. The proxy will monitor the tps of the GET request. If it exceeds the threshold, the result will be cached and returned directly in the next request.
* During the cache period, the proxy will periodically penetrate a request to the backend to update the cache value
* For the GET command, it is supported to determine whether to enable the cache mechanism according to the prefix of the key, and you can also customize the implementation rules

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
    - hotKeyCachePlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
#Which keys need the hot key cache function, the default implementation is PrefixMatchHotKeyCacheKeyChecker, which can be configured based on the prefix of the key, and you can customize the implementation (implement the HotKeyCacheKeyChecker interface)
hot.key.cache.key.checker.className=com.netease.nim.camellia.redis.proxy.plugin.hotkeycache.PrefixMatchHotKeyCacheKeyChecker

#Prefix configuration method when using PrefixMatchHotKeyCacheKeyChecker, if you want to configure all keys to enable the hot key cache function, you can set an empty string, and all keys will not take effect by default
hot.key.cache.key.prefix=["dao_c", "kkk"]
#Prefix configuration method when using PrefixMatchHotKeyCacheKeyChecker (tenant level)
1.default.hot.key.cache.key.prefix=["dao_c", "kkk"]

##Hot key cache related configuration
#The switch of the hot key cache function, the default is true
hot.key.cache.enable=true
#The capacity of the LRU counter used to determine whether it is a hot key
hot.key.cache.counter.capacity=100000
#The time window of the LRU counter used to judge whether it is a hot key, the default is 1000ms
hot.key.cache.counter.check.millis=1000
#The threshold for determining the hot key, the default is 100
hot.key.cache.check.threshold=100
#Whether to cache null value, default true
hot.key.cache.null=true
#The duration of the hot key cache, the default is 10s, when half expired, it will penetrate a GET request to the backend
hot.key.cache.expire.millis=10000
# Maximum number of cached hot keys, default 1000
hot.key.cache.max.capacity=1000

##Hot key cache related configuration (tenant level, bid=1, bgroup=default)
#The switch of the hot key cache function, the default is true
1.default.hot.key.cache.enable=true
#The capacity of the LRU counter used to determine whether it is a hot key
1.default.hot.key.cache.counter.capacity=100000
#The time window of the LRU counter used to judge whether it is a hot key, the default is 1000ms
1.default.hot.key.cache.counter.check.millis=1000
#The threshold for determining the hot key, the default is 100
1.default.hot.key.cache.check.threshold=100
#Whether to cache null value, default true
1.default.hot.key.cache.null=true
#The duration of the hot key cache, the default is 10s, when half expired, it will penetrate a GET request to the backend
1.default.hot.key.cache.expire.millis=10000
# Maximum number of cached hot keys, default 1000
1.default.hot.key.cache.max.capacity=1000


##The monitoring data is exposed to the public through /monitor by default (the data is refreshed every 60s by default). If you need to push it in real time, you can set a callback (implement the HotKeyCacheStatsCallback interface)
###The default callback does not do anything
hot.key.cache.stats.callback.className=com.netease.nim.camellia.redis.proxy.plugin.hotkeycache.DummyHotKeyCacheStatsCallback
#Interval for real-time push of hot key cache hits, default 10s
hot.key.cache.stats.callback.interval.seconds=10
````