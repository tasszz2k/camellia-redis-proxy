## Integrate ProxyRouteConfUpdater to customize and manage multiple groups of dynamic configuration
After integrating camellia-dashboard, proxy has the ability of multi-tenant routing. If you do not want to use camellia-dashboard, then you can customize ProxyRouteConfUpdater to implement related logic
ProxyRouteConfUpdater is an abstract class. You need to implement a subclass yourself. Inside the ProxyRouteConfUpdater object instance, you need to implement at least the following methods:
````
public abstract ResourceTable getResourceTable(long bid, String bgroup);
````
The proxy will call this method by default when it starts to get the initial routing configuration. (Note: The configuration can be described by the json string mentioned above. You can use the ReadableResourceTableUtil.parseTable(String conf) method to convert it into a ResourceTable object)
In addition, when the routing configuration is changed, you can call the callback method provided by ProxyRouteConfUpdater to make real-time changes. The callback method is as follows:
````
public final void invokeUpdateResourceTable(long bid, String bgroup, ResourceTable resourceTable)
````
When you need to delete a route, you can call the invokeRemoveResourceTable callback method, as follows:
````
public void invokeRemoveResourceTable(long bid, String bgroup)
````
The example configuration to enable ProxyRouteConfUpdater is as follows:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: custom
    custom:
      proxy-route-conf-updater-class-name: com.netease.nim.camellia.redis.proxy.route.DynamicConfProxyRouteConfUpdater
      dynamic: true # indicates that multiple sets of configurations are supported, the default is true
      bid: 1 #The default bid, the bgroup used when the client does not declare its own bid and bgroup when requesting, can be the default, if the default, the request without bid/bgroup will be rejected
      bgroup: default #The default bgroup, the bgroup used when the client does not declare its own bid and bgroup when requesting, can be the default, if the default, the request without bid/bgroup will be rejected
      reload-interval-millis: 600000 #When using ProxyRouteConfUpdater, configuration changes will be automatically updated through callbacks. In order to prevent the loss of updates, there will be a round-robin polling mechanism. This configuration indicates the interval of round-robin polling, and the default is 10 minutes.
````
The above configuration indicates that we use DynamicConfProxyRouteConfUpdater, the implementation class of ProxyRouteConfUpdater. Under this implementation class, the configuration is hosted on ProxyDynamicConf (camellia-redis-proxy.properties)
The configuration method when using DynamicConfProxyRouteConfUpdater is in the form of k-v, as follows:
````
#Indicates the routing configuration of bid=1/bgroup=default
1.default.route.conf=redis://@127.0.0.1:6379
#Indicates the routing configuration of bid=2/bgroup=default
2.default.route.conf={"type": "simple","operation": {"read": "redis://passwd123@127.0.0.1:6379","type": "rw_separate","write ": "redis-sentinel://passwd2@127.0.0.1:6379,127.0.0.1:6378/master"}}
````
In addition to the DynamicConfProxyRouteConfUpdater provided by camellia, you can implement a custom ProxyRouteConfUpdater implementation yourself to connect to your configuration center. An example of a custom implementation is provided below:
````java
public class CustomProxyRouteConfUpdater extends ProxyRouteConfUpdater {

    private String url = "redis://@127.0.0.1:6379";

    public CustomProxyRouteConfUpdater() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::update, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public ResourceTable getResourceTable(long bid, String bgroup) {
        return ReadableResourceTableUtil.parseTable(url);
    }

    private void update() {
        String newUrl = "redis://@127.0.0.2:6379";
        if (!url.equals(newUrl)) {
            url = newUrl;
            invokeUpdateResourceTableJson(1, "default", url);
        }
    }
}
````
In the above example, the initial route of the proxy is redis://@127.0.0.1:6379, and after 10s, it is switched to redis://@127.0.0.2:6379