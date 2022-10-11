
## 关于scan

* camellia-redis-proxy支持scan命令，不管后端是代理的的redis-standalone还是redis-sentinel还是redis-cluster
* 当proxy的路由配置是自定义分片时（比如2个redis-cluster集群组成一个逻辑上的大集群，或者多组redis-sentinel主从组成一个大集群），scan命令仍然是有效的，proxy会按照顺序依次扫描每个读后端
* 当proxy的后端是redis-cluster或者定义分片时，每次scan返回的cursor从数值上看可能很大，这是因为cursor上记录了后端node的index，调用方不需要关心

### 例子一
```
redis-cluster://@127.0.0.1:6379,127.0.0.1:6380
```
* scan会扫描redis-cluster://@127.0.0.1:6379,127.0.0.1:6380的所有master节点

### 例子二
```json
{
  "type": "simple",
  "operation": {
    "read": "redis://@127.0.0.1:6379",
    "type": "rw_separate",
    "write": "redis://@127.0.0.1:6378"
  }
}
```
* scan会扫描redis://@127.0.0.1:6379

### 例子三
```json
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
```
* scan会扫描redis://@127.0.0.1:6379以及redis-cluster://@127.0.0.1:6378,127.0.0.1:6377的所有master节点

