## RateLimitProxyPlugin

### illustrate
* It is used to control the client's request tps, if it exceeds, it will return an error directly instead of penetrating to the backend redis

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
    - rateLimitPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
##inspection cycle
rate.limit.check.millis=1000
##Maximum number of requests, if it is less than 0, there is no limit, if it is equal to 0, all requests will be intercepted
rate.limit.max.count=100000

#bid/bgroup-level rate control (the following example indicates a request with bid=1, bgroup=default, a maximum of 10w requests within 1000ms are allowed, and an error will be returned if exceeded)
##inspection cycle
1.default.rate.limit.check.millis=1000
##Maximum number of requests, if it is less than 0, there is no limit, if it is equal to 0, all requests will be intercepted
1.default.rate.limit.max.count=100000
````