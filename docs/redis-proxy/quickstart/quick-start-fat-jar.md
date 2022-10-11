## Quick start (based on fatJar and sample-code)

Download the source code and switch to the latest stable branch (v1.1.1)
````
git clone https://github.com/netease-im/camellia.git
cd camellia
git checkout v1.1.1
````
Modify the configuration file in [sample-code](/) as needed:
* application.yml
* logback.xml
* camellia-redis-proxy.properties
* resource-table.json

Compile with maven
````
mvn clean install
````
Find the executable jar package and run it with the java -jar command (pay attention to setting memory and GC, and make sure that jdk8 or above has been installed and added to the path):
````
cd camellia-samples/camellia-redis-proxy-samples/target
java -XX:+UseG1GC -Xms2048m -Xmx2048m -server -jar camellia-redis-proxy-samples-1.1.1.jar
````

If it is in a container environment, you need to add the UseContainerSupport parameter (requires jdk8u191 or above), as follows:
````
cd camellia-samples/camellia-redis-proxy-samples/target
java -XX:+UseG1GC -XX:+UseContainerSupport -Xms2048m -Xmx2048m -server -jar camellia-redis-proxy-samples-1.1.1.jar
````