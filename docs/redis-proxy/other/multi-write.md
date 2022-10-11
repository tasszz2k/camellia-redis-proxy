## Several questions about double writing

### Supported double-write schemes
* Through the routing configuration, the proxy can directly send the write command to redisA and redisB at the same time, or more redis clusters, we call it direct double write mode
* You can send write commands to mq through the MqMultiWriteCommandInterceptor interceptor (default supports kafka, other types of mq can be extended by yourself), and deploy a proxy in consumption mode for asynchronous double writing, which we call MQ-based double writing mode
* If there is a need for double writing of some keys and some double writing of keys, it can be implemented through the MultiWriteCommandInterceptor interceptor with a custom MultiWriteFunc, which we call the key-level custom double writing mode

Note, there are three modes of direct double writing:
#### first_resource_only
Indicates that if the first write address configured is returned, it will be returned to the client immediately, which is the default mode
#### all_resources_no_check
Indicates that all write addresses that need to be configured are returned before they are returned to the client, and the return result of the first address is returned. You can configure this mode to take effect:
````yaml
camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: complex
      json-file: resource-table.json
    redis-conf:
      multi-write-mode: all_resources_no_check
````
#### all_resources_check_error
Indicates that all write addresses that need to be configured are returned before they are returned to the client, and it will check whether all addresses are returned non-error results. If so, the return result of the first address will be returned; otherwise, the first address will be returned. Error results, you can configure this mode to take effect:
````yaml
camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: complex
      json-file: resource-table.json
    redis-conf:
      multi-write-mode: all_resources_check_error
````

### Double write delay problem
#### Direct double write mode
* The proxy establishes long links with multiple redis backends at the same time, and writes commands at the same time (proxy is a non-blocking model)
* In the direct double-write mode, by default, the first redis backend returns success, that is, the client write command is executed successfully. In addition, it can support configuring multiple backends at the same time to return success and then reply to the client write successfully. At this time, it can be considered that Double write with almost no delay
* In addition, starting from version 1.0.44, it supports to monitor the response time of each backend redis separately. Businesses can use this indicator to judge the delay of double write mode (the delay of netty queue and tcp queue will be reflected in this indicator)

#### MQ-based double write mode
* The proxy only establishes a long link with one of the redis backends. When the write command is sent to the redis backend, it will be sent to MQ together. The proxy across the computer room will consume data from MQ and write the write command asynchronously.
* In this mode, if you need to monitor the delay of double writing, you can judge by monitoring the consumption delay of MQ itself

#### Another way to monitor double write latency
* For example, to double-write redisA and redisB through proxy, whether it is direct double-write mode or MQ-based double-write mode, you can send a write command (such as setex) to proxy regularly through a monitoring script.
* Then directly connect redisA and redisB to get the key. If the get arrives, it means there is no delay. If one is not get, it will trigger waiting and retry until it is obtained, so as to calculate the size of the double write delay

### Double write reliability
* Because redis itself does not support distributed transactions, there is no guarantee that two redis can write successfully or fail at the same time
* However, in the direct-connect double-write mode, due to the establishment of a long link with the back-end redis, there will be no partial success under normal circumstances, unless the network fails or the back-end redis is abnormal, or the proxy itself is down
* In the direct-connect double-write mode, you can also support configuring two redis to return success at the same time and then return to the client successfully. In this mode, it can be considered for the client that if the write command returns successfully, it can be ensured Both redis are written successfully
* In the MQ-based double-write mode, MQ is used to ensure that the write command is not lost

### Double write consistency
* You can use some tools to judge the data consistency of multiple redis clusters, such as redis-full-check, see: https://developer.aliyun.com/article/690463