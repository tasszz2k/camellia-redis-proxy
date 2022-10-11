## Supported redis backend types

We describe the back-end redis server by url, which supports three types: ordinary single-point redis, redis-sentinel, and redis-cluster. In addition, it also supports proxying the read command to the slave node of redis-sentinel. The specific url format is as follows:

* Ordinary single point redis
````
##with password
redis://passwd@127.0.0.1:6379
##without password
redis://@127.0.0.1:6379
##Have an account and a password
redis://username:passwd@127.0.0.1:6379
````

* redis-sentinel
````
##with password
redis-sentinel://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName
##without password
redis-sentinel://@127.0.0.1:16379,127.0.0.1:16379/masterName
##Have an account and a password
redis-sentinel://username:passwd@127.0.0.1:16379,127.0.0.1:16379/masterName
````

* redis-cluster
````
##with password
redis-cluster://passwd@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
##without password
redis-cluster://@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
##Have an account and a password
redis-cluster://username:passwd@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
````

* redis-sentinel-slaves
````
##This type of backend can only be configured as a read address in read-write separation mode

##Do not read the master, at this time the proxy will randomly select a slave from the slave set to forward the command
##with password
redis-sentinel-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=false
##without password
redis-sentinel-slaves://@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=false
##Have an account and a password
redis-sentinel-slaves://username:passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=false

##Read the master, at this time the proxy will randomly select a node from the master+slave set to forward the command (maybe the master or the slave, all nodes have the same probability)
##with password
redis-sentinel-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=true
##without password
redis-sentinel-slaves://@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=true
##Have an account and a password
redis-sentinel-slaves://username:passwd@127.0.0.1:16379,127.0.0.1:16379/masterName?withMaster=true

##redis-sentinel-slaves will automatically sense: node downtime, master-slave switching and node expansion
````

* redis-cluster-slaves
````
##This type of backend can only be configured as a read address in read-write separation mode. If it is configured as a write address, the proxy will not report an error, but each write request will generate a redirection, which will greatly affect performance.

##Do not read the master, at this time the proxy will randomly select a slave from the slave set to forward the command
##with password
redis-cluster-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379?withMaster=false
##without password
redis-cluster-slaves://@127.0.0.1:16379,127.0.0.1:16379?withMaster=false
##Have an account and a password
redis-cluster-slaves://username:passwd@127.0.0.1:16379,127.0.0.1:16379?withMaster=false

##Read the master, at this time the proxy will randomly select a node from the master+slave set to forward the command (maybe the master or the slave, all nodes have the same probability)
##with password
redis-cluster-slaves://passwd@127.0.0.1:16379,127.0.0.1:16379?withMaster=true
##without password
redis-cluster-slaves://@127.0.0.1:16379,127.0.0.1:16379?withMaster=true
##Have an account and a password
redis-cluster-slaves://username:passwd@127.0.0.1:16379,127.0.0.1:16379?withMaster=true

##redis-cluster-slaves will automatically sense: node downtime, master-slave switchover and node expansion
````

*redis-proxies
````
##This type is mainly for proxying to multiple stateless proxy nodes, such as codis-proxy, twemproxy, etc. camellia-redis-proxy will randomly select one of the configured nodes for forwarding
##When the back-end proxy node is down, camellia-redis-proxy will dynamically remove the relevant node, and if the node is restored, it will be dynamically added back

##with password
redis-proxies://passwd@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
##without password
redis-proxies://@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
##Have an account and a password
redis-proxies://username:passwd@127.0.0.1:6379,127.0.0.2:6379,127.0.0.3:6379
````

* redis-proxies-discovery
````
##This type is mainly for proxying to multiple stateless proxy nodes, such as codis-proxy, twemproxy, etc.
##The difference from redis-proxies is that this type supports obtaining a list of stateless proxy nodes from the registry, and dynamically increasing or decreasing related nodes
##In order to interact with the registry, you need to implement the ProxyDiscoveryFactory interface first (supports full class name configuration, and also supports direct injection of related implementation classes by spring)
##This type identifies the proxy node list through proxyName, and goes to ProxyDiscoveryFactory through proxyName to obtain the actual proxy node list

##with password
redis-proxies-discovery://passwd@proxyName
##without password
redis-proxies-discovery://@proxyName
##Have a password and have an account
redis-proxies-discovery://username:passwd@proxyName
````