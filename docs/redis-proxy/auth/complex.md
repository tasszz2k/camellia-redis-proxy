### Complex configuration (json-file configuration example)

#### Configure a single address
When using a separate configuration file for configuration, the file is generally a json file, but if you only write one address in your configuration file, it is also allowed, and the proxy will recognize this situation, as follows:
````
redis://passwd@127.0.0.1:6379
````
There is only one line of data in the configuration file, which is a back-end redis address, indicating that the proxy's routing forwarding rule is the simplest form, that is, it is directly forwarded to the redis instance
The configuration effect at this time is the same as directly configuring the resource address url in application.yml, but the difference is that when a separate configuration file is used, the address supports dynamic update

#### Configure read-write separation one
````json
{
  "type": "simple",
  "operation": {
    "read": "redis://passwd123@127.0.0.1:6379",
    "type": "rw_separate",
    "write": "redis-sentinel://passwd2@127.0.0.1:6379,127.0.0.1:6378/master"
  }
}
````
The configuration above means:
* Write commands will be proxied to redis-sentinel://passwd2@127.0.0.1:6379,127.0.0.1:6378/master
* The read command will be proxied to redis://passwd123@127.0.0.1:6379

You can see that redis, redis-sentinel, and redis-cluster can be mixed in json

#### Configure read-write separation 2
````json
{
  "type": "simple",
  "operation": {
    "read": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
    "type": "rw_separate",
    "write": "redis-sentinel://passwd123@127.0.0.1:26379/master"
  }
}
````
The configuration above means:
* Write commands will be proxied to redis-sentinel://passwd123@127.0.0.1:26379/master
* The read command will proxy to redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true, which is the master node of redis-sentinel://passwd123@127.0.0.1:26379/master and all slave node

#### Configure sharding (because of the previous naming error, 1.0.45 and earlier versions use shading, 1.0.46 and later versions are compatible with sharding/shading)
````json
{
  "type": "sharding",
  "operation": {
    "operationMap": {
      "0-2-4": "redis://password1@127.0.0.1:6379",
      "1-3-5": "redis-cluster://@127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381"
    },
    "bucketSize": 6
  }
}
````
The above configuration indicates that the key is divided into 6 shards, of which:
* Shard [0,2,4] proxy to redis://password1@127.0.0.1:6379
* Shard [1,3,5] proxy to redis-cluster://@127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381

#### Configure double (multi) write
````json
{
  "type": "simple",
  "operation": {
    "read": "redis://passwd1@127.0.0.1:6379",
    "type": "rw_separate",
    "write": {
      "resources": [
        "redis://passwd1@127.0.0.1:6379",
        "redis://passwd2@127.0.0.1:6380"
      ],
      "type": "multi"
    }
  }
}
````
The configuration above means:
* All write commands (such as setex/zadd/hset) are proxied to redis://passwd1@127.0.0.1:6379 and redis://passwd2@127.0.0.1:6380 (ie double write), in particular, the client's return The package is the first write address of the configuration seen
* All read commands (like get/zrange/mget) are proxied to redis://passwd1@127.0.0.1:6379

#### Configure read more
````json
{
  "type": "simple",
  "operation": {
    "read": {
      "resources": [
        "redis://password1@127.0.0.1:6379",
        "redis://password2@127.0.0.1:6380"
      ],
      "type": "random"
    },
    "type": "rw_separate",
    "write": "redis://passwd1@127.0.0.1:6379"
  }
}
````
The configuration above means:
* All write commands (such as setex/zadd/hset) are proxied to redis://passwd1@127.0.0.1:6379
* All read commands (like get/zrange/mget) are randomly proxied to redis://passwd1@127.0.0.1:6379 or redis://password2@127.0.0.1:6380

#### Mix various sharding and double write logic (because of the previous naming error, 1.0.45 and earlier versions use shading, 1.0.46 and later versions are compatible with sharding/shading)
````json
{
  "type": "sharding",
  "operation": {
    "operationMap": {
      "4": {
        "read": "redis://password1@127.0.0.1:6379",
        "type": "rw_separate",
        "write": {
          "resources": [
            "redis://password1@127.0.0.1:6379",
            "redis://password2@127.0.0.1:6380"
          ],
          "type": "multi"
        }
      },
      "5": {
        "read": {
          "resources": [
            "redis://password1@127.0.0.1:6379",
            "redis://password2@127.0.0.1:6380"
          ],
          "type": "random"
        },
        "type": "rw_separate",
        "write": {
          "resources": [
            "redis://password1@127.0.0.1:6379",
            "redis://password2@127.0.0.1:6380"
          ],
          "type": "multi"
        }
      },
      "0-2": "redis://password1@127.0.0.1:6379",
      "1-3": "redis://password2@127.0.0.1:6380"
    },
    "bucketSize": 6
  }
}
````
The above configuration indicates that the key is divided into 6 shards, of which shard 4 is configured with the logic of read-write separation and double-write, and shard 5 has the logic of read-write separation and double-write multi-read.