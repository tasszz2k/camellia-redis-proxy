## netty configuration
Generally speaking, you can use the default configuration, please modify it carefully

````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  password: pass123
  #Represents the netty parameter from client to proxy
  netty:
    boss-thread: 1 #The default is 1
    work-thread: -1 #Indicates the number of work threads, the default is -1, which means that the number of cpu cores is automatically obtained. It is recommended not to modify
    so-backlog: 1024 #default 1024
    so-sndbuf: 10485760 #default 10M
    so-rcvbuf: 10485760 #Default 10M
    write-buffer-water-mark-low: 131072 #Default 128k
    write-buffer-water-mark-high: 524288 #default 512k
    reader-idle-time-seconds: -1 #Default -1, which means that detection is not enabled. If you want to enable detection, all three configurations need to be greater than or equal to 0
    writer-idle-time-seconds: -1 #Default -1, which means that detection is not enabled. If you want to enable detection, all three configurations need to be greater than or equal to 0
    all-idle-time-seconds: -1 #Default -1, which means that detection is not enabled. If you want to enable detection, all three configurations need to be greater than or equal to 0
    so-keepalive: false #default false
    tcp-no-delay: true #default true
  transpond:
    type: local
    local:
      resource: redis://@127.0.0.1:6379
    #Represents the netty parameter from proxy to redis
    netty:
      so-keepalive: true #default true
      so-sndbuf: 10485760 #default 10M
      so-rcvbuf: 10485760 #Default 10M
      write-buffer-water-mark-low: 131072 #Default 128k
      write-buffer-water-mark-high: 524288 #default 512k
      tcp-no-delay: true #default true
````