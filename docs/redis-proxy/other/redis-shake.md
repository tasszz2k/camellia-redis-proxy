## Instructions on using redis-shake for data migration
* camellia-redis-proxy implements the standard redis protocol, including the scan command, so you can migrate data from redis-standalone/redis-sentinel/redis-cluster or twemproxy/codis to camellia, and vice versa
* camellia-redis-proxy supports data migration using redis-shake's sync and rump modes. The sync mode supports the migration of stock data and incremental data, and the rump mode only supports stock data migration.
* Because redis-shake will verify the redis version through the info command before the migration, the info command of 1.0.50 and previous camellia-redis-proxy versions uses \n for line breaks, and later versions use \r\n for line breaks , and redis-shake uses \r\n by default to identify the info command return package, so please use version 1.0.51 and later to dock redis-shake
* Download address of redis-shake: https://github.com/alibaba/RedisShake

### Typical scene one
* Background: Migrating from redis-cluster to multiple sets of redis-sentinel
* Source cluster: a set of redis-cluster
* Target cluster: camellia-redis-proxy + redis-sentinel cluster *N

### Typical scene 2
* Background: Migrate from one set of redis-cluster to multiple sets of redis-cluster to form a large logical cluster with custom shards
* Source cluster: a set of redis-cluster
* Target cluster: camellia-redis-proxy + redis-cluster cluster *N

### Typical scene three
* Background: Migrated from twemproxy/codis to redis-cluster, but the client does not support the redis-cluster protocol, so camellia-redis-proxy was added as a front proxy
* Source cluster: twemproxy/codis + multiple redis-servers, a separate redis-shake task for each redis-server
* Target cluster: camellia-redis-proxy + redis-cluster cluster