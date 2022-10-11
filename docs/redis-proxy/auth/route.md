## routing configuration
The routing configuration represents the forwarding rules of camellia-redis-proxy after receiving the client's redis command

## outline
* Simplest example
* Supported backend redis types
* Dynamic configuration and complex configuration (read-write separation, sharding, etc.)
* Multi-tenancy support
* Use camellia-dashboard to manage multi-tenant dynamic routing
* Integrate ProxyRouteConfUpdater to custom manage multi-tenant dynamic routing

### Simplest example
Configure the following information in application.yml:
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  transpond:
    type: local
    local:
      type: simple
      resource: redis-cluster://@127.0.0.1:6379,127.0.0.1:6378,127.0.0.1:6377
````
The above configuration indicates that proxy's port=6380, proxy's password=pass123, proxy to the backend redis-cluster cluster, address string=127.0.0.1:6379,127.0.0.1:6378,127.0.0.1:6377

### Supported backend redis types
For details, see: [redis-resources](redis-resources.md)

### Dynamic configuration (single tenant)
For details, see: [dynamic-conf](dynamic-conf.md)

### How to define a complex configuration (read-write separation, sharding, etc.)
For details, see: [complex](complex.md)

### Multi-tenancy support
For details, see: [tenancy](tenancy.md)

### Using camellia-dashboard to manage multi-tenant dynamic routing
For details, see: [dashboard](dashboard.md)

### Use ProxyRouteConfUpdater to customize and manage multi-tenant dynamic routing
For details, see: [route-conf-updater](route-conf-updater.md)