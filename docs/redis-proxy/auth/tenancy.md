## Multi-tenancy
camellia-redis-proxy supports multi-tenancy. Tenants are uniquely identified by bid (number) and bgroup (string). For different tenants, you can configure different backend redis and different monitoring parameters (such as key threshold)

### How to identify which tenant a client connection belongs to

#### By default, clientname is used to identify
This example shows the tenant using bid=10 and bgroup=default
````
➜ ~ ./redis-cli -h 127.0.0.1 -p 6380 -a pass123
127.0.0.1:6379> client setname camellia_10_default
OK
127.0.0.1:6380> set k1 v1
OK
127.0.0.1:6380> get k1
"v1"
127.0.0.1:6380 > mget k1 k2 k3
1) "v1"
2) (nil)
3) (nil)
````
Client example:
If the backend is Java and Jedis is used, it can be called like this:
````java
public class Test {
    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6380,
                2000, "pass123", 0, "camellia_10_default");
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex("k1", 10, "v1");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
````

#### Customize the tenant selection logic using the ClientAuthProvider interface
ClientAuthProvider uses the login password to distinguish different tenants. Camellia has a built-in DynamicConfClientAuthProvider implementation, which configures the multi-tenant mapping relationship through camellia-redis-proxy.properties
How to enable:
````yaml
camellia-redis-proxy:
  monitor-enable: true #Whether to enable monitoring
  monitor-interval-seconds: 60 #Monitor callback interval
  client-auth-provider-class-name: com.netease.nim.camellia.redis.proxy.auth.DynamicConfClientAuthProvider
  transpond:
    type: remote
    remote:
      bid: 1
      bgroup: default
      url: http://xxx:8080
      monitor: true
      check-interval-millis: 5000
````
Then you can configure it here in camellia-redis-proxy.properties:
````
pass123.auth.conf=1|default
pass456.auth.conf=2|default
````
The above example means:
* When using the password pass123 to log in to the proxy, use the route of bid=1, bgroup=default
* When using the password pass456 to log in to the proxy, use the route of bid=2, bgroup=default


Of course, you can also implement a ClientAuthProvider yourself, as follows:
A simple example:
````java
public class MockClientAuthProvider implements ClientAuthProvider {

    @Override
    public ClientIdentity auth(String userName, String password) {
        ClientIdentity clientIdentity = new ClientIdentity();
        if (password.equals("pass1")) {
            clientIdentity.setPass(true);
            clientIdentity.setBid(1L);
            clientIdentity.setBgroup("default");
        } else if (password.equals("pass2")) {
            clientIdentity.setPass(true);
            clientIdentity.setBid(2L);
            clientIdentity.setBgroup("default");
        } else if (password.equals("pass3")) {
            clientIdentity.setPass(true);
        }
        return clientIdentity;
    }

    @Override
    public boolean isPasswordRequired() {
        return true;
    }
}
````
The above example means:
* When using the password pass1 to log in to the proxy, use the route of bid=1, bgroup=default
* When using the password pass2 to log in to the proxy, use the route of bid=2, bgroup=default
* When using the password pass3 to log in to the proxy, use the default route, which is bid=1, bgroup=default


Thanks to [@yangxb2010000](https://github.com/yangxb2010000) for providing the above functions