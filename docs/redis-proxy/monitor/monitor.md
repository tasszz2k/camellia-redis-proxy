## monitoring
camellia-redis-proxy provides rich monitoring capabilities, including:
* Provided monitoring items
* Monitoring data acquisition method
* Get server-related information through the info command
* Treat proxy as a platform for monitoring the status of redis cluster (exposed through http interface)

## Monitoring items
### Big key monitoring
Implemented using BigKeyProxyPlugin, see: [big-key](../plugin/big-key.md)

### Hot key monitoring
Implemented using HotKeyProxyPlugin, see: [hot-key](../plugin/hot-key.md)

### Hot key cache monitoring
Mainly to monitor the hits of the hot key cache, see: [hot-key-cache](../plugin/hot-key-cache.md)

### Number of requests/rt/slow query
Implemented using MonitorProxyPlugin, see: [hot-key](../plugin/monitor-plugin.md)

### Other monitoring data
* Number of client connections
* Number of backend redis connections
* Backend redis response time
* routing information
  *....

## Monitoring data acquisition
### custom callback
You can configure custom callbacks in application.yml. The default callback implementation is to print logs, as follows:
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
  monitor-callback-class-name: com.netease.nim.camellia.redis.proxy.monitor.LoggingMonitorCallback #Monitor callback class
  plugins: #Use yml to configure plugins, built-in plugins can be enabled directly using aliases, custom plugins need to configure the full class name
    - hotKeyPlugin
    - monitorPlugin
    - bigKeyPlugin
    - hotKeyCachePlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````
The callback class can get all the monitoring data, refer to the definition of com.netease.nim.camellia.redis.proxy.monitor.model.Stats class

## Get monitoring data through httpAPI
In addition to obtaining monitoring data through callbacks, you can also obtain monitoring data directly through http-api (json format). For details, see: [Monitoring Data](monitor-data.md)

### Get server-related information through the info command
The proxy implements the info command and supports returning the following information: Server/Clients/Route/Upstream/Memory/GC/Stats/Upstream-Info
For details, see [info command](info.md)

### Treat proxy as a platform for monitoring the status of redis cluster (exposed through http interface)
You can use the http interface to request the proxy, and pass the redis address to be probed to the proxy, and the proxy will return the information of the target redis cluster in json format
See [detect](detect.md) for details