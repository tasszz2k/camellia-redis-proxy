## Quick start (based on installation package)

Download the latest version of the installation package and extract it (v1.1.1):
````
wget https://github.com/netease-im/camellia/releases/download/1.1.1/camellia-redis-proxy-1.1.1.tar.gz
tar zxvf camellia-redis-proxy-1.1.1.tar.gz
cd camellia-redis-proxy-1.1.1/
````
Modify the configuration files under BOOT-INF/classes/ as needed:
* application.yml
* logback.xml
* camellia-redis-proxy.properties
* resource-table.json

Adjust the startup parameters (mainly JVM parameters) of start.sh as needed. The default parameters are as follows (make sure jdk8 or above has been installed and added to the path):
````
java -XX:+UseG1GC -Xms2048m -Xmx2048m -server org.springframework.boot.loader.JarLauncher
````
Just start it directly:
````
./start.sh
````

If it is in docker, call start_in_docker.sh, the default parameters are as follows (mainly adding UseContainerSupport, which requires jdk8u191 or above):
````
java -XX:+UseG1GC -XX:+UseContainerSupport -Xms2048m -Xmx2048m -server org.springframework.boot.loader.JarLauncher
````
Start as follows:
````
./start_in_docker.sh
````


In particular, in some business scenarios, it may be desirable to separate the proxy executable file from the configuration file application.yml. In this case, the tar package can be re-packaged into a jar package, and combined with the spring.config.location parameter provided by spring to Specify application.yml as follows:
````
wget https://github.com/netease-im/camellia/releases/download/1.1.1/camellia-redis-proxy-1.1.1.tar.gz
tar zxvf camellia-redis-proxy-1.1.1.tar.gz
cd camellia-redis-proxy-1.1.1/
jar -cvf0M camellia-redis-proxy.jar BOOT-INF/META-INF/org/
````
At this point, a camellia-redis-proxy.jar will be generated, and then use the java -jar command to start it, as follows:
````
java -XX:+UseG1GC -Xms2048m -Xmx2048m -server -jar camellia-redis-proxy.jar --spring.config.location=file:/xxx/xxx/application.yml
````
If it is in docker, remember to add the UseContainerSupport parameter, as follows:
````
java -XX:+UseG1GC -XX:+UseContainerSupport -Xms2048m -Xmx2048m -server -jar camellia-redis-proxy.jar --spring.config.location=file:/xxx/xxx/application.yml
````