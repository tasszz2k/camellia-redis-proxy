## About lua

* camellia-redis-proxy supports eval/evalsha/eval_ro/evalsha_ro. When proxying to redis-cluster or custom shard, proxy will check whether each key points to the same backend node or slot
* The proxy also supports script commands and supports load/flush/exists parameters. When script load/flush is used, the proxy will send the script to all write backends, and when script exists is used, it will be sent to all read backends

### Example 1
````
redis-cluster://@127.0.0.1:6379,127.0.0.1:6380
````
* script load/flush will be sent to all master nodes of the above redis-cluster, and return the first master's return packet
* script exists will be sent to all master nodes of the above redis-cluster, and the sum of the return packets of each master will be summarized

### Example 2
````json
{
  "type": "simple",
  "operation": {
    "read": "redis://@127.0.0.1:6379",
    "type": "rw_separate",
    "write": "redis://@127.0.0.1:6378"
  }
}
````
* script load/flush will be sent to redis://@127.0.0.1:6378
* script exists will be sent to redis://@127.0.0.1:6379

### Example 3
````json
{
  "type": "sharding",
  "operation": {
    "operationMap": {
      "0-2-4": "redis://@127.0.0.1:6379",
      "1-3-5": "redis-cluster://@127.0.0.1:6378,127.0.0.1:6377"
    },
    "bucketSize": 6
  }
}
````
* script load/flush will be sent to all master nodes of redis://@127.0.0.1:6379 and redis-cluster://@127.0.0.1:6378,127.0.0.1:6377
* script exists will be sent to all master nodes of redis://@127.0.0.1:6379 and redis-cluster://@127.0.0.1:6378, 127.0.0.1:6377, and the aggregated results will be returned