## Monitoring data acquisition
* You can request proxy's console (a simple http-server) to get it, the default port is 16379, and the specific interface is http://127.0.0.1:16379/monitor
* The refresh cycle of the monitoring data obtained by /monitor depends on the monitor-interval-seconds configuration in application.yml
* The monitoring data obtained by /monitor is a json, such as [example](monitor.json)
## monitor.json field meaning analysis
````
{
  "connectStats": [
    {
      "connect": 53 //The number of client connections, although it is an array, there is actually only one
    }
  ],
  "countStats": [
    {
      "count": 422214, //Total requests
      "totalReadCount": 207582, //Number of read requests
      "totalWriteCount": 214632 //Number of write requests
    }
  ],
  "qpsStats": [
    {
      "qps": 7036.9, //Total qps
      "writeQps": 3577.2, //write qps
      "readQps": 3459.7 //Read qps
    }
  ],
  "total": [ //Request amount, qps of each command
    {
      "qps": 2389.483333333333, //qps
      "count": 143369, //request amount
      "command": "hgetall" //command
    },
    {
      "qps": 0.03333333333333333,
      "count": 2,
      "command": "auth"
    }
  ],
  "bidbgroup": [ //The request volume and qps of each business, divide the business according to bid/bgroup
    {
      "qps": 0.05, //qps
      "bgroup": "default", //bgroup
      "count": 3, //request amount
      "bid": "35" //bid
    },
    {
      "qps": 0.06666666666666667,
      "bgroup": "default",
      "count": 4,
      "bid": "default"
    }
  ],
  "detail": [ //The request volume and qps of each command under each business
    {
      "qps": 180.18333333333334, //qps
      "bgroup": "default", //bgroup
      "count": 10811, //request amount
      "bid": "3", //bid
      "command": "hget" //command
    },
    {
      "qps": 0.0166666666666666666,
      "bgroup": "default",
      "count": 1,
      "bid": "35",
      "command": "zrangebyscore"
    }
  ],
  "spendStats": [ //Time-consuming monitoring
    {
      "maxSpendMs": 16.464333, //Maximum rt
      "count": 143565, //Number of requests
      "avgSpendMs": 1.1236299562985408, //average rt
      "command": "hgetall" //command
    },
    {
      "maxSpendMs": 0.041476,
      "count": 206,
      "avgSpendMs": 0.0021440485436893205,
      "command": "ping"
    }
  ],
  "bidBgroupSpendStats": [//Time-consuming monitoring according to business statistics
    {
      "bgroup": "default",
      "maxSpendMs": 7.929259,
      "count": 263,
      "bid": 9,
      "avgSpendMs": 0.7394532509505704,
      "command": "incrby"
    },
    {
      "bgroup": "default",
      "maxSpendMs": 1.668735,
      "count": 21,
      "bid": 9,
      "avgSpendMs": 0.7418927142857142,
      "command": "hget"
    }
  ],
  "failStats": [ //Fail monitoring
    {
      "reason": "xxx", //reason
      "count": 2 //Number
    }
  ],
  "slowCommandStats": [ //Slow query monitoring
    //Specially, you can limit the maximum number of monitoring data items by configuring slow.command.monitor.json.max.count=100 in camellia-redis-proxy.propertis, the default is 100
    {
      "bgroup": "default", //bgroup
      "bid": "1", //bid, if the camellia-dashboard is not used, but the local configuration is used, bid=default/bgroup=default
      "command": "mget", //command
      "keys": "k1,k2,k3", //The list of keys involved, comma separated
      "spendMillis": 10200, //time-consuming
      "thresholdMillis":1000 //The threshold of slow query
    },
    {
      "bgroup": "default",
      "bid": "1",
      "command": "hgetall",
      "keys": "kkk",
      "spendMillis": 10200,
      "thresholdMillis": 1000
    }],
  "hotKeyStats": [ //Hot key monitoring
    {
      "times": 1, // times the threshold is exceeded
      "avg": 101.0, //In the N cycles exceeding the threshold, the average request volume of all cycles
      "max": 101, //The number of requests in the cycle with the largest amount of requests in the N cycles exceeding the threshold
      "bgroup": "default", //bgroup
      "count": 101, //sum of N cycles of requests exceeding the threshold
      "checkMillis": 1000, //check cycle
      "bid": "1", //bid
      "key": "dao_c|kfk_tpc_.23380.", //key
      "checkThreshold": 100 //Check threshold, if the number of requests exceeds the threshold during the check period, it is considered a hot key, and times will be +1
    }
  ],
  "bigKeyStats": [ //Big key monitoring
    //Specially, you can limit the maximum number of monitoring data items by configuring big.key.monitor.json.max.count=10000 in camellia-redis-proxy.propertis. The default limit is 100. Generally speaking, A better way is to reduce the data items by adjusting a more appropriate large key detection threshold
    {
      "commandType": "ZSET", //type of key, including STRING/ZSET/SET/LIST/HASH, etc.
      "size": 1903, //size
      "bgroup": "default", //bgroup
      "threshold": 1000, //threshold
      "bid": "16", //bid, if the camellia-dashboard is not used, but the local configuration is used, bid=default/bgroup=default
      "command": "zrangebyscore", //command
      "key": "saaaaa" //The key involved
    },
    {
      "commandType": "SET",
      "size": 2912,
      "bgroup": "default",
      "threshold": 1000,
      "bid": "16",
      "command": "scard",
      "key": "ssasas"
    }
  ],
  "hotKeyCacheStats": [ //Hot key cache monitoring
    {
      "bid": "1", //bid
      "bgroup": "default", //bgroup
      "key": "xxxx", //the key involved
      "hitCount": 49, //Number of cache hits
      "checkMillis": 1000, //check cycle
      "checkThreshold": 100 //Check threshold. During the check period, if the number of requests exceeds this threshold, the next request will be directly hit by the cache
    },
    {
      "hitCount": 459,
      "bgroup": "default",
      "checkMillis": 1000,
      "bid": "1",
      "key": "yyyy",
      "checkThreshold": 100
    }
  ],
  "routeConf": [ //Current routing table
    {
      "bgroup": "default",
      "updateTime": "2021-04-29 10:52:01", //If the proxy is restarted, updateTime will be reset to the current time
      "bid": 1,
      "resourceTable": "{\"type\":\"simple\",\"operation\":{\"read\":\"redis-sentinel-slaves://passwd123@127.0.0.1:26379/master ?withMaster=true\",\"type\":\"rw_separate\",\"write\":\"redis-sentinel://passwd123@127.0.0.1:26379/master\"}}"
    },
    {
      "bgroup": "default",
      "updateTime": "2021-04-29 10:52:32",
      "bid": 2,
      "resourceTable": "redis://passwd123@127.0.0.1:6379"
    }
  ],
  "resourceBidBgroupCommandStats": [ //Command-level routing statistics based on business statistics
    {
      "resource": "redis://passwd123@127.0.0.1:6379",
      "qps": 0.4,
      "bgroup": "default",
      "count": 24,
      "bid": 2,
      "command": "smembers"
    },
    {
      "resource": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
      "qps": 0.35,
      "bgroup": "default",
      "count": 21,
      "bid": 1,
      "command": "hget"
    }
  ],
  "resourceCommandStats": [ //Route statistics (command)
    {
      "resource": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
      "qps": 238.61666666666667,
      "count": 14317,
      "command": "get"
    },
    {
      "resource": "redis://passwd123@127.0.0.1:6379",
      "qps": 63.9,
      "count": 3834,
      "command": "setex"
    }
  ],
  "resourceStats": [ //Route statistics
    {
      "resource": "redis://passwd123@127.0.0.1:6379",
      "qps": 196.28333333333333,
      "count": 11777
    },
    {
      "resource": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
      "qps": 404.31666666666666,
      "count": 24259
    }
  ],
  "resourceBidBgroupStats": [ //Business routing statistics
    {
      "resource": "redis-sentinel-slaves://passwd123@127.0.0.1:26379/master?withMaster=true",
      "qps": 404.31666666666666,
      "bgroup": "default",
      "count": 24259,
      "bid": 9
    },
    {
      "resource": "redis://passwd123@127.0.0.1:6379",
      "qps": 196.28333333333333,
      "bgroup": "default",
      "count": 11777,
      "bid": 1
    }
  ],
  "redisConnectStats": [ //Total number of connections to backend redis
    {
      "connect": 16
    }
  ],
  "redisConnectDetailStats": [//Number of connections to each redis in the backend
    {
      "addr": "@10.177.0.69:8803",
      "connect": 4
    },
    {
      "addr": "abc@10.201.48.171:6379",
      "connect": 4
    }
  ],
  "upstreamRedisSpendStats": [//Response time of backend redis
    {
      "maxSpendMs": 8.689271,
      "count": 184,
      "addr": "abc@10.201.48.171:6379",
      "avgSpendMs": 3.060518804347826
    },
    {
      "maxSpendMs": 0.176157,
      "count": 19,
      "addr": "@10.177.0.69:8803",
      "avgSpendMs": 0.12276473684210526
    }
  ]
}
````