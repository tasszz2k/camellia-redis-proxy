#camellia-core
## Introduction
A proxy framework developed based on cglib that supports **client multi-reading and multi-writing** and **client sharding**
Identify the read-write type of a method by adding annotations to the method
The sharding field is identified by adding annotations to the method parameters, and the sharding algorithm supports customization
Can be used with camellia-dashboard to dynamically update proxy configuration
## maven dependencies
````
<dependency>
  <groupId>com.netease.nim.camellia</groupId>
  <artifactId>camellia-core</artifactId>
  <version>a.b.c</version>
</dependency>
````
## example

### Business code
````
public class Cache {

    private Map<String, String> map = new HashMap<>();
    private Resource resource;

    public Cache(Resource resource) {
        this.resource = resource;
    }

    @ReadOp
    public String get(@Param String key) {
        System.out.println("get, resource = " + resource.getUrl() + ", key = " + key);
        return map.get(key);
    }

    @WriteOp
    public int set(@ShardingParam String key, String value) {
        System.out.println("set, resource = " + resource.getUrl() + ", key = " + key);
        map.put(key, value);
        return 1;
    }

    @WriteOp
    public int delete(@ShardingParam String key) {
        System.out.println("delete, resource = " + resource.getUrl() + ", key = " + key);
        map.remove(key);
        return 1;
    }

    @ReadOp
    public Map<String, String> getBulk(@ShardingParam(type = ShardingParam.Type.Collection) String... keys) {
        System.out.println("getBulk, resource = " + resource.getUrl() + ", keys = " + keys);
        Map<String, String> ret = new HashMap<>();
        for (String key : keys) {
            String value = map.get(key);
            if (value != null) {
                ret.put(key, value);
            }
        }
        return ret;
    }

    @WriteOp
    public int setBulk(@ShardingParam(type = ShardingParam.Type.Collection) Map<String, String> kvs) {
        System.out.println("setBulk, resource = " + resource.getUrl() + ", keys = " + JSONObject.toJSONString(kvs.keySet()));
        for (Map.Entry<String, String> entry : kvs.entrySet()) {
            this.map.put(entry.getKey(), entry.getValue());
        }
        return kvs.size();
    }
}

````
### Use local static configuration
````

Resource rw = new Resource("rw");
Resource w = new Resource("w");
ResourceTable resourceTable = ResourceTableUtil.simple2W1RTable(rw, rw, w);
StandardProxyGenerator<Cache> generator = new StandardProxyGenerator<>(Cache.class, resourceTable);

Cache proxy = generator.generate();

System.out.println(proxy.get("k1"));
System.out.println(proxy.set("k1", "v1"));
System.out.println(proxy.getBulk("k1", "k2"));
Map<String, String> kvs = new HashMap<>();
kvs.put("k2", "v2");
kvs.put("k3", "v3");
System.out.println(proxy.setBulk(kvs));

````
### Dynamic configuration using dashboard
````
ReloadableProxyFactory<Cache> factory = new ReloadableProxyFactory.Builder<Cache>()
                .service(CamelliaApiUtil.init("http://127.0.0.1:8080"))//dashboard address
                .bid(1L)//Business type
                .bgroup("default")//Business grouping
                .clazz(Cache.class)//Proxy object
                .monitorEnable(true)//Whether to report statistics
                .checkIntervalMillis(5000)//Configure the check interval, in ms
                .build();
                
Cache proxy = factory.getDynamicProxy();

System.out.println(proxy.get("k1"));
System.out.println(proxy.set("k1", "v1"));
System.out.println(proxy.getBulk("k1", "k2"));
Map<String, String> kvs = new HashMap<>();
kvs.put("k2", "v2");
kvs.put("k3", "v3");
System.out.println(proxy.setBulk(kvs));

````

### Sample source code
[Sample source](/camellia-samples/camellia-core-samples)