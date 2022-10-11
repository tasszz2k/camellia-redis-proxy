## How to use spring to manage bean generation

* Many custom entries of proxy (whether it is plugin or callback) need to configure the full class name
* The proxy will first obtain the class from spring. If it cannot be obtained, it will call the no-argument constructor of the full class name to create the related object