## Treat proxy as a platform for monitoring the status of redis cluster (exposed through http interface)
You can use the http interface to request the proxy, and pass the redis address to be probed to the proxy, and the proxy will return the information of the target redis cluster in json format

### Request example
````
http://127.0.0.1:16379/detect?url=redis-cluster://@127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002
````
Supports monitoring of three redis clusters: redis-standalone, redis-sentinel, and redis-cluster

### return example
Include the following information:
* Cluster status
* Cluster memory situation (total memory, used memory)
* Number of cluster keys (total keys, keys with ttl)
* Cluster qps
* Node information, including the memory of each node, the number of keys, tps and other information
````json
{
    "info":
      [
        {
          "used_memory_human": "609.05M",
          "maxmemory_hum": "12.00G",
          "qps": 361,
          "expire_key_count": 196393,
          "maxmemory": 12884901888,
          "used_memory": 638635736,
          "key_count": 200906,
          "type": "cluster",
          "memory_used_rate": 0.04956465649108092,
          "memory_used_rate_human": "4.96%",
          "url": "redis-cluster://@127.0.0.1:7000,127.0.0.1:7001,127.0.0.1:7002",
          "status": "ok"
        }
    ],
    "nodeInfo":
    [
        {
            "used_memory_human": "152.41M",
            "role": "master",
            "connected_slaves": 1,
            "slave0": "ip=127.0.0.1,port=7003,state=online,offset=10001397878,lag=1",
            "expire_key_count": 49002,
            "maxmemory": 3221225472,
            "maxmemory_human": "3.00G",
            "node_url": "@127.0.0.1:7000",
            "used_memory": 159808936,
            "key_count": 50120,
            "memory_used_rate_human": "4.96%",
            "avg_ttl": 141492103,
            "redis_version": "4.0.9",
            "hz": 10,
            "qps": 98,
            "memory_used_rate": 0.04961122324069341,
            "maxmemory_policy": "noeviction"
        },
        {
            "used_memory_human": "152.37M",
            "role": "master",
            "connected_slaves": 1,
            "slave0": "ip=127.0.0.1,port=7004,state=online,offset=11558147522,lag=1",
            "expire_key_count": 49186,
            "maxmemory": 3221225472,
            "maxmemory_human": "3.00G",
            "node_url": "@127.0.0.1:7002",
            "used_memory": 159768192,
            "key_count": 50349,
            "memory_used_rate_human": "4.96%",
            "avg_ttl": 150670909,
            "redis_version": "4.0.9",
            "hz": 10,
            "qps": 97,
            "memory_used_rate": 0.0495985746383667,
            "maxmemory_policy": "noeviction"
        },
        {
            "used_memory_human": "152.13M",
            "role": "master",
            "connected_slaves": 1,
            "slave0": "ip=127.0.0.1,port=7005,state=online,offset=12976183697,lag=1",
            "expire_key_count": 49106,
            "maxmemory": 3221225472,
            "maxmemory_human": "3.00G",
            "node_url": "@127.0.0.1:7003",
            "used_memory": 159517808,
            "key_count": 50208,
            "memory_used_rate_human": "4.95%",
            "avg_ttl": 100739796,
            "redis_version": "4.0.9",
            "hz": 10,
            "qps": 70,
            "memory_used_rate": 0.049520845214525856,
            "maxmemory_policy": "noeviction"
        },
        {
            "used_memory_human": "152.15M",
            "role": "master",
            "connected_slaves": 1,
            "slave0": "ip=10.189.7.217,port=6380,state=online,offset=9792844650,lag=1",
            "expire_key_count": 49099,
            "maxmemory": 3221225472,
            "maxmemory_human": "3.00G",
            "node_url": "@127.0.0.1:7003",
            "used_memory": 159540800,
            "key_count": 50229,
            "memory_used_rate_human": "4.95%",
            "avg_ttl": 98326773,
            "redis_version": "4.0.9",
            "hz": 10,
            "qps": 96,
            "memory_used_rate": 0.04952798287073771,
            "maxmemory_policy": "noeviction"
        }
    ],
    "otherInfo":
    [
        {
          "value": "8",
          "key": "cluster_known_nodes"
        },
        {
          "value": "4",
          "key": "cluster_size"
        },
        {
          "value": "0",
          "key": "cluster_slots_pfail"
        },
        {
          "value": "ok",
          "key": "cluster_state"
        },
        {
          "value": "16384",
          "key": "cluster_slots_assigned"
        },
        {
          "value": "16384",
          "key": "cluster_slots_ok"
        },
        {
          "value": "0",
          "key": "cluster_slots_fail"
        },
        {
          "value": "yes",
          "key": "cluster_safety"
        }
    ]
}
````

### Remark
When the proxy is used as a monitoring platform, the proxy may not know to access the back-end redis address when it starts. In order to avoid the failure of the proxy to start, it can be configured without preheating (the preheat parameter is set to false), as follows:
````yaml
server:
   port: 6380
spring:
   application:
     name: camellia-redis-proxy-server

camellia-redis-proxy:
   console-port: 16379 #console port, the default is 16379, the detect interface needs to go through this port
   transpond:
     type: local #Use local configuration
     local:
       type: simple
       resource: redis://@127.0.0.1:6379 #Forwarded redis address
     redis-conf:
       preheat: false #Indicates whether to check the connectivity of the backend redis at startup, the default is true, if the backend redis is unreachable, the proxy cannot be started
````