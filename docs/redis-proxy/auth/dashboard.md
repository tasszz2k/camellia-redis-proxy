## Manage dynamic routing with camellia-dashboard
You can host routing information to a remote camellia-dashboard (see [camellia-dashboard](/docs/dashboard/dashboard.md))
camellia-dashboard is a web service. The proxy will regularly check whether the configuration in camellia-dashboard has changed. If there is, it will update the proxy's route.
The following is an example configuration:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: remote
    remote:
      url: http://127.0.0.1:8080 #camellia-dashbaord's address
      check-interval-millis: 5000 # Polling period to camellia-dashbaord
      dynamic: true # indicates that multiple sets of configurations are supported, the default is true
      bid: 1 #The default bid, the bgroup used when the client does not declare its own bid and bgroup when requesting, can be the default, if the default, the request without bid/bgroup will be rejected
      bgroup: default #The default bgroup, the bgroup used when the client does not declare its own bid and bgroup when requesting, can be the default, if the default, the request without bid/bgroup will be rejected
````
The above configuration indicates that the routing configuration of proxy will be obtained from camellia-dashboard, and the configuration of bid=1 and bgroup=default will be obtained.
In addition, the proxy will regularly check whether the configuration on the camellia-dashboard has been updated. If it is updated, the local configuration will be updated. The default check interval is 5s.
In particular, when you need to support multi-tenancy, you can set dynamic=true