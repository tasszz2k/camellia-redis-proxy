##DelayDoubleDeleteProxyPlugin

### illustrate
* A plugin for transparently deferred cache double deletion to ensure db/cache coherency
* Only intercept DEL commands for delayed secondary deletion

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
    - delayDoubleDeletePlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
###Configuration
#First to open, the default is false
delay.double.del.enable=true
#Secondly, you need to configure the number of seconds to delay double deletion. If <=0, it will not take effect. The default is -1
double.del.delay.seconds=5
#Finally, it is necessary to configure which keys to match for delayed deletion. It is a json array. If it is not configured, it will not take effect.
##If the key in all DEL commands should be delayed and double deleted, the configuration prefix is ​​an empty string
double.del.key.prefix=[""]
##If only some commands, such as only dao_cache and cache prefix keys are delayed and double deleted, you can configure as follows
#double.del.key.prefix=["dao_cache", "cache"]

###Configuration (tenant level)
#First to open, the default is false
1.default.delay.double.del.enable=true
#Secondly, you need to configure the number of seconds to delay double deletion. If <=0, it will not take effect. The default is -1
1.default.double.del.delay.seconds=5
#Finally, it is necessary to configure which keys to match for delayed deletion. It is a json array. If it is not configured, it will not take effect.
##If the key in all DEL commands should be delayed and double deleted, the configuration prefix is ​​an empty string
1.default.double.del.key.prefix=[""]
##If only some commands, such as only dao_cache and cache prefix keys are delayed and double deleted, you can configure as follows
#1.default.double.del.key.prefix=["dao_cache", "cache"]

````