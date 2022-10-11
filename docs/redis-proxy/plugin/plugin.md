## Plugin system
* Starting from version 1.1.x, functions such as monitoring, big key, hot key, etc. have been reconstructed and unified as part of the plug-in system. Users can introduce built-in plug-ins as needed through simple configuration
* Plugins use a unified interface to intercept and control requests and responses
* The proxy has many built-in plug-ins, which can be used directly after simple configuration
* You can also implement custom plugins

### Plugin interface
````java
public interface ProxyPlugin {

    /**
     * initialization method
     */
    default void init(ProxyBeanFactory factory) {
    }

    /**
     * Execute in order from large to small, request and response can be defined separately
     * @return priority
     */
    default ProxyPluginOrder order() {
        return ProxyPluginOrder.DEFAULT;
    }

    /**
     * Request (the command has just arrived at the proxy at this time, but not yet at the backend redis)
     * @param request the context of the request command
     */
    default ProxyPluginResponse executeRequest(ProxyRequest request) {
        return ProxyPluginResponse.SUCCESS;
    }

    /**
     * Response (at this time the command has been responded from the backend redis and will be returned to the client)
     * @param reply response reply context
     */
    default ProxyPluginResponse executeReply(ProxyReply reply) {
        return ProxyPluginResponse.SUCCESS;
    }

}
````

### How to enable plugins
* application.yml
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  console-port: 16379 #console port, the default is 16379, if set to -16379, there will be a random available port, if set to 0, the console will not be started
  password: pass123 #proxy password, if a custom client-auth-provider-class-name is set, the password parameter is invalid
  plugins: #Use yml to configure plugins, built-in plugins can be enabled directly using aliases, custom plugins need to configure the full class name
    - monitorPlugin
    - bigKeyPlugin
    - hotKeyPlugin
    - com.xxx.xxx.CustomProxyPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````
* camellia-redis-proxy.properties
````
#Plugins configured using properties can be customized to increase or decrease at runtime
proxy.plugin.list=monitorPlugin,bigKeyPlugin,com.xxx.xxx.CustomProxyPlugin
````

### Built-in plugins
````java
public enum BuildInProxyPluginEnum {
    //For monitoring, mainly monitoring request volume and response time and slow query
    MONITOR_PLUGIN("monitorPlugin", MonitorProxyPlugin.class, Integer.MAX_VALUE, Integer.MIN_VALUE),
    //Control access rights, ip black and white list
    IP_CHECKER_PLUGIN("ipCheckerPlugin", IPCheckProxyPlugin.class, Integer.MAX_VALUE - 10000, 0),
    // used to control the request rate
    RATE_LIMIT_PLUGIN("rateLimitPlugin", RateLimitProxyPlugin.class, Integer.MAX_VALUE - 20000, 0),
    //Used to intercept illegal keys, fail directly and quickly
    TROUBLE_TRICK_KEYS_PLUGIN("troubleTrickKeys", TroubleTrickKeysProxyPlugin.class, Integer.MAX_VALUE - 30000, 0),

    //For hot key cache (only GET command is supported)
    HOT_KEY_CACHE_PLUGIN("hotKeyCachePlugin", HotKeyCacheProxyPlugin.class, 20000, Integer.MIN_VALUE + 10000),
    //Used to monitor hot keys
    HOT_KEY_PLUGIN("hotKeyPlugin", HotKeyProxyPlugin.class, 10000, 0),

    //Used to monitor the big key
    BIG_KEY_PLUGIN("bigKeyPlugin", BigKeyProxyPlugin.class, 0, 0),
    //Used for cache double delete (only intercept DELETE command)
    DELAY_DOUBLE_DELETE_PLUGIN("delayDoubleDeletePlugin", DelayDoubleDeleteProxyPlugin.class, 0, 0),
    //Used for custom double write rules (key dimension)
    MULTI_WRITE_PLUGIN("multiWritePlugin", MultiWriteProxyPlugin.class, 0, 0),

    //Used for key/value conversion
    CONVERTER_PLUGIN("converterPlugin", ConverterProxyPlugin.class, Integer.MIN_VALUE, Integer.MAX_VALUE),
    ;
    private final String alias;
    private final Class<? extends ProxyPlugin> clazz;
    private final int requestOrder;
    private final int replyOrder;

    BuildInProxyPluginEnum(String alias, Class<? extends ProxyPlugin> clazz, int requestOrder, int replyOrder) {
        this.alias = alias;
        this.clazz = clazz;
        this.requestOrder = requestOrder;
        this.replyOrder = replyOrder;
    }
}
````
List of built-in plugins:
* MonitorProxyPlugin, used to monitor the request qps and response of the command, see: [monitor-plugin](monitor-plugin.md)
* BigKeyProxyPlugin, used to monitor big keys, see: [big-key](big-key.md)
* HotKeyProxyPlugin, used to monitor hot keys, see: [hot-key](hot-key.md)
* HotKeyCacheProxyPlugin, used for hot key cache, supports GET command, see: [hot-key-cache](hot-key-cache.md)
* ConverterProxyPlugin, used for key/value conversion, such as encryption and decryption, key namespace, etc. For details, see: [converter](converter.md)
* MultiWriteProxyPlugin, used to customize double write (up to key level), see: [multi-write](multi-write.md)
* DelayDoubleDeleteProxyPlugin, used to be transparent to cache double delete (only intercept DEL commands), see: [delay-double-delete](delay-double-delete.md)
* TroubleTrickKeysProxyPlugin, used to temporarily intercept some commands of the problem key, see: [trouble-trick-keys](trouble-trick-keys.md)
* RateLimitProxyPlugin, used for frequency control, supports tenant level control, see: [rate-limit](rate-limit.md)
* IPCheckProxyPlugin, used to control client access, supports ip black and white list, see: [ip-checker](ip-checker.md)

### Other plugins provided by camellia (not built-in, need to introduce additional maven dependencies)
* MqMultiWriteProducerProxyPlugin, used for asynchronous double write using mq, see: [mq-multi-write](mq-multi-write.md)

### Custom plugin
Implement the ProxyPlugin interface and configure the full class name of the implementation class into application.yml or camellia-redis-proxy.properties