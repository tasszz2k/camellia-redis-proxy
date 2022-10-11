## IPCheckProxyPlugin

### illustrate
* A plugin for ip black and white list restrictions on clients accessing proxy
* Supports blacklist mode, also supports whitelist mode, configuration supports dynamic change

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
    - ipCheckerPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

### Dynamic configuration switch (camellia-redis-proxy.properties)
````properties
#Blacklist example (support ip, also supports network segment, comma separated):
#ip.check.mode=1
#ip.black.list=2.2.2.2,5.5.5.5,3.3.3.0/24,6.6.0.0/16

#Whitelist example (support ip, also supports network segment, comma separated):
#ip.check.mode=2
#ip.white.list=2.2.2.2,5.5.5.5,3.3.3.0/24,6.6.0.0/16

#Set different strategies according to bid/bgroup:
#Blacklist example (indicates the blacklist configuration of bid=1, bgroup=default):
#1.default.ip.check.mode=1
#1.default.ip.black.list=2.2.2.2,5.5.5.5,3.3.3.0/24,6.6.0.0/16

#Whitelist example (indicates the whitelist configuration of bid=1, bgroup=default):
#1.default.ip.check.mode=2
#1.default.ip.white.list=2.2.2.2,5.5.5.5,3.3.3.0/24,6.6.0.0/16
````