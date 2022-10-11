## quick start (not based on spring-boot-starter)

First, import dependencies:
````
<dependency>
    <groupId>com.netease.nim</groupId>
    <artifactId>camellia-redis-proxy</artifactId>
    <version>1.1.1</version>
</dependency>
````

Second, as follows:
````java
/**
 * Manually start a proxy without using spring-boot-starter
 * Created by caojiajun on 2021/8/3
 */
public class SimpleTest {

    public static void main(String[] args) {
        //set related parameters
        CamelliaRedisProxyStarter.updatePort(6380);//Set the port of proxy
        CamelliaRedisProxyStarter.updatePassword("pass123");//Set the proxy password
        CamelliaRedisProxyStarter.updateRouteConf("redis://@127.0.0.1:6379");//You can set a single address, or you can set a json to configure double write/sharding, etc.

        CamelliaServerProperties serverProperties = CamelliaRedisProxyStarter.getServerProperties();
        serverProperties.setMonitorEnable(true);//Enable monitoring
        List<String> plugins = serverProperties.getPlugins();
        //add plugin
        plugins.add(BuildInProxyPluginEnum.MONITOR_PLUGIN.getAlias());
        plugins.add(BuildInProxyPluginEnum.BIG_KEY_PLUGIN.getAlias());
        plugins.add(BuildInProxyPluginEnum.HOT_KEY_PLUGIN.getAlias());
        //Other parameter settings....

        //start up
        CamelliaRedisProxyStarter.start();
    }
}
````