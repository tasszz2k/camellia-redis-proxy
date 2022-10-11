##MultiWriteProxyPlugin

### illustrate
* A plugin for additional custom double-write strategy (key), such as: some keys need to be double-written, some keys do not need to be double-written, some keys are double-written to redisA, and some keys are double-written to redisB
* Remark 1: Only the write commands in the command set fully supported by proxy support this mode. For those limited supported commands (such as blocking commands, publish and subscribe commands, etc.), the use of MultiWriteProxyPlugin to double write is not supported.
* Remark 2: When using MultiWriteCommandInterceptor to double write the write command wrapped by the redis transaction, the main route may fail to execute but the double write succeeds.

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
    - multiWritePlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
#A function method for judging the double write strategy (just implement the MultiWriteFunc interface)
multi.write.func.class.name=com.xxx.xxx.CustomMultiWriteFunc
````