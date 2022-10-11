**Docker commands**

[Bitnami package for Redis®](https://hub.docker.com/r/bitnami/redis)
```bash
docker run --name redis -e ALLOW_EMPTY_PASSWORD=yes -e REDIS_PORT_NUMBER=6377 -p 6377:6377 bitnami/redis:latest
```
