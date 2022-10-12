# Supported backend redis types
We describe the back-end redis server by url, which supports three types: ordinary single-point redis, redis-sentinel, and redis-cluster. In addition, it can support configuring the read request to the slave node of redis-sentinel. The specific url format is as follows:

* Ordinary single point redis
````
##with password
redis://passwd@127.0.0.1:6379
##without password
redis://@127.0.0.1:6379
````

* redis-sentinel
````
##with password
redis-sentinel://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName
##without password
redis-sentinel://@127.0.0.1:16379,127.0.0.1:16379/masterName
````

* redis-cluster
````
##with password
redis-cluster://passwd@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
##without password
redis-cluster://@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
````

* redis-sentinel-slaves
````
##This type of backend can only be configured as a read address in read-write separation mode

##Do not read the master, at this time the proxy will randomly select a slave from the slave set to forward the command
##with password
redis-sentinel-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=false
##without password
redis-sentinel-slaves://@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=false

##Read the master, at this time the proxy will randomly select a node from the master+slave set to forward the command (maybe the master or the slave, all nodes have the same probability)
##with password
redis-sentinel-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=true
##without password
redis-sentinel-slaves://@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=true

##redis-sentinel-slaves will automatically sense: node downtime, master-slave switching and node expansion
````


# support configuration

* Simple read and write
````
redis://password@127.0.0.1:6379
````

* Read and write separation
````
{
  "type": "simple",
  "operation": {
    "read": "redis://read@127.0.0.1:6379",
    "type": "rw_separate",
    "write": "redis://write@127.0.0.1:6380"
  }
}
````

* Read and write separation
````
{
  "type": "simple",
  "operation": {
    "read": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
    "type": "rw_separate",
    "write": "redis-sentinel://passwd123@127.0.0.1:26379/master"
  }
}
````

* Double write single read
````
{
  "type": "simple",
  "operation": {
    "read": "redis://read@127.0.0.1:6379",
    "type": "rw_separate",
    "write": {
      "resources": [
        "redis://read@127.0.0.1:6379",
        "redis://write@127.0.0.1:6380"
      ],
      "type": "multi"
    }
  }
}
````

* Fragmentation
````
{
  "type": "sharding",
  "operation": {
    "operationMap": {
      "0-2-4": "redis://password1@127.0.0.1:6379",
      "1-3-5": "redis://password2@127.0.0.1:6380"
    },
    "bucketSize": 6
  }
}
````

* Fragmentation + double write
````
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
      "0-2": "redis://password1@127.0.0.1:6379",
      "1-3-5": "redis://password2@127.0.0.1:6380"
    },
    "bucketSize": 6
  }
}
````