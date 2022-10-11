## About scan

* camellia-redis-proxy supports scan command, no matter whether the backend is proxy redis-standalone or redis-sentinel or redis-cluster
* When the proxy's routing configuration is a custom shard (for example, two redis-cluster clusters form a logically large cluster, or multiple groups of redis-sentinel master-slave form a large cluster), the scan command is still valid, and the proxy Each read backend is scanned in order
* When the backend of the proxy is redis-cluster or sharding is defined, the cursor returned by each scan may be numerically large, because the index of the backend node is recorded on the cursor, and the caller does not need to care

### Example 1
````
redis-cluster://@127.0.0.1:6379,127.0.0.1:6380
````
* scan will scan all master nodes of redis-cluster://@127.0.0.1:6379,127.0.0.1:6380

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
* scan will scan redis://@127.0.0.1:6379

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
* scan will scan all master nodes of redis://@127.0.0.1:6379 and redis-cluster://@127.0.0.1:6378,127.0.0.1:6377