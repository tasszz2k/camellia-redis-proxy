## custom sharding function
camellia-redis-proxy supports sharding. You can customize the sharding function. The sharding function will calculate the hash value of a key and take the remainder of the shard size (bucketSize) to get the shard to which the key belongs.
The default sharding function is:
````
com.netease.nim.camellia.core.client.env.DefaultShardingFunc
````
The default sharding function does not support HashTag. If you want to use HashTag, you can use the following two sharding functions:
````
com.netease.nim.camellia.core.client.env.CRC16HashTagShardingFunc
com.netease.nim.camellia.core.client.env.DefaultHashTagShardingFunc
````
In addition, you can also inherit com.netease.nim.camellia.core.client.env.AbstractSimpleShardingFunc to implement your own desired sharding function, similar to this:

````java
package com.netease.nim.camellia.redis.proxy.samples;

import com.netease.nim.camellia.core.client.env.AbstractSimpleShardingFunc;

public class CustomShardingFunc extends AbstractSimpleShardingFunc {
    
    @Override
    public int shardingCode(byte[] key) {
        if (key == null) return 0;
        if (key.length == 0) return 0;
        int h = 0;
        for (byte d : key) {
            h = 31 * h + d;
        }
        return (h < 0) ? -h : h;
    }
}
````
Then configure it in application.yml, similar to this:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: complex
      json-file: resource-table.json
    redis-conf:
      sharding-func: com.xxx.CustomShardingFunc
````