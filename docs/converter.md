## ConverterProxyPlugin

### illustrate
* A plugin for transparent key/value conversion, which can be used for transparent data encryption/decryption/decompression, and can also be used for
* Currently, supports key conversion for most commands
* Currently, supports custom value conversion of string/set/list/hash/set related commands

### Principle
When a command request arrives at the proxy, the proxy will parse out specific parameters according to the type of command, and go through the forward conversion interface of Converters
Under this interface, users can implement the transformation logic from originalValue/originalKey to convertedValue/convertedKey, and the proxy will send the converted command to the back-end redis node
When the back-end redis returns, the proxy will parse the packet back and go through the Converters reverse conversion interface
Under this interface, users can implement the transformation logic from convertedValue/convertedKey to originalValue/originalKey, and the proxy will return the converted originalValue/originalKey to the client

### Currently supported commands for key conversion
Commands with keys are supported, in particular:
* For KEYS and SCAN commands, the pattern is called back to the KeyConverter interface, not the actual key
* For PUBSUB series commands, the pattern or channel is called back to the KeyConverter interface, not the actual key

Thanks to [@yangxb2010000](https://github.com/yangxb2010000) for the enhancement of the key conversion function

### Currently supported data structures and commands for value conversion
The following command will call back to the Converter interface for value conversion:
````
#String
SET,GETSET,SETNX,SETEX,PSETEX,MSET,MSETNX,GET,MGET,GETDEL,GETEX,
#List
RPUSH,LPUSH,LLEN,LRANGE,LINDEX,LSET,LREM,LPOP,RPOP,LINSERT,LPUSHX,RPUSHX,LPOS,BRPOP,BLPOP,
#Set
SADD,SMEMBERS,SREM,SPOP,SCARD,SISMEMBER,SRANDMEMBER,SSCAN,SMISMEMBER,
#Hash
HSET,HGET,HSETNX,HMSET,HMGET,HINCRBY,HEXISTS,HDEL,HKEYS,
HVALS,HGETALL,HINCRBYFLOAT,HSCAN,HSTRLEN,HRANDFIELD,
#ZSet
ZADD,ZINCRBY,ZRANK,ZSCORE,ZMSCORE,ZRANGE,ZRANGEBYSCORE,ZRANGEBYLEX,
ZREVRANK,ZREVRANGE,ZREVRANGEBYSCORE,ZREVRANGEBYLEX,ZREM,
ZREMRANGEBYRANK,ZREMRANGEBYSCORE,ZREMRANGEBYLEX,ZSCAN,
ZPOPMAX,ZPOPMIN,BZPOPMAX,BZPOPMIN,ZRANDMEMBER,
````
### how to use
You need to implement the relevant Converter interface, including:
* KeyConverter can convert keys
* StringConverter can convert value
* HashConverter can convert field and value in hash
* ListConverter can convert each element in the list
* SetConverter can convert each member in the set
* ZSetConverter can convert each member in zset

### Enable method
````yaml
server:
  port: 6380
spring:
  application:
    name: camellia-redis-proxy-server

camellia-redis-proxy:
  console-port: 16379 #console port, the default is 16379, if set to -16379, there will be a random available port, if set to 0, the console will not be started
  password: pass123 #proxy password, if a custom client-auth-provider-class-name is set, the password parameter is invalid
  monitor-enable: true #Whether to enable monitoring
  monitor-interval-seconds: 60 #Monitor callback interval
  plugins: #Use yml to configure plugins, built-in plugins can be enabled directly using aliases, custom plugins need to configure the full class name
    - converterPlugin
  transpond:
    type: local #Use local configuration
    local:
      type: simple
      resource: redis://@127.0.0.1:6379 #Forwarded redis address
````

Taking StringConverter as an example, the interface is defined as follows:
````java
public interface StringConverter {

    /**
     * Convert the original value
     * @param commandContext command context
     * @param key belongs to the key
     * @param originalValue original value
     * @return converted value
     */
    byte[] valueConvert(CommandContext commandContext, byte[] key, byte[] originalValue);

    /**
     * Reverse the converted value to the original value
     * @param commandContext command context
     * @param key belongs to the key
     * @param convertedValue converted value
     * @return original value
     */
    byte[] valueReverseConvert(CommandContext commandContext, byte[] key, byte[] convertedValue);

}
````

A simple example:
````java
public class CustomStringConverter implements StringConverter {

    @Override
    public byte[] valueConvert(CommandContext commandContext, byte[] key, byte[] originalValue) {
        String keyStr = Utils.bytesToString(key);
        if (keyStr.equals("k1")) {
            if (originalValue == null) return null;
            String str = Utils.bytesToString(originalValue);
            return Utils.stringToBytes(str.replaceAll("abc", "***"));
        }
        return originalValue;
    }

    @Override
    public byte[] valueReverseConvert(CommandContext commandContext, byte[] key, byte[] convertedValue) {
        String keyStr = Utils.bytesToString(key);
        if (keyStr.equals("k1")) {
            if (convertedValue == null) return null;
            String str = Utils.bytesToString(convertedValue);
            return Utils.stringToBytes(str.replaceAll("\\*\\*\\*", "abc"));
        }
        return convertedValue;
    }
}

````

Then, you need to configure in camellia-redis-proxy.properties:
````
#string value converter
converter.string.className=com.xxxx.CustomStringConverter

##Other converters
#converter.key.className=com.xxxx.CustomXXX
#converter.hash.className=com.xxxx.CustomXXX
#converter.set.className=com.xxxx.CustomXXX
#converter.zset.className=com.xxxx.CustomXXX
#converter.list.className=com.xxxx.CustomXXX
````

In the above example, if the key is k1, the abc in the value will be converted to *** and then stored in redis; when you get, the *** will be converted back to abc and then returned to the client. The whole process is for Client is transparent

### Use CamelliaCompressor/CamelliaEncryptor for transparent decompression or encryption and decryption
First, you need to import:
````
<dependency>
    <groupId>com.netease.nim</groupId>
    <artifactId>camellia-tools</artifactId>
    <version>a.b.c</version>
</dependency>
````

Compression example
````java
public class CompressStringConverter implements StringConverter {

    private final CamelliaCompressor compressor = new CamelliaCompressor();

    @Override
    public byte[] valueConvert(CommandContext commandContext, byte[] key, byte[] originalValue) {
        return compressor.compress(originalValue);
    }

    @Override
    public byte[] valueReverseConvert(CommandContext commandContext, byte[] key, byte[] convertedValue) {
        return compressor.decompress(convertedValue);
    }
}
````

Encrypted example:
````java
public class EncryptStringConverter implements StringConverter {
    
    private final CamelliaEncryptor encryptor = new CamelliaEncryptor(new CamelliaEncryptAesConfig("abc"));
    
    @Override
    public byte[] valueConvert(CommandContext commandContext, byte[] key, byte[] originalValue) {
        return encryptor.encrypt(originalValue);
    }

    @Override
    public byte[] valueReverseConvert(CommandContext commandContext, byte[] key, byte[] convertedValue) {
        return encryptor.decrypt(convertedValue);
    }
}
````

Further, in fact, you can compress first and then encrypt, as follows:
````java
public class CompressEncryptStringConverter implements StringConverter {

    private final CamelliaCompressor compressor = new CamelliaCompressor();
    private final CamelliaEncryptor encryptor = new CamelliaEncryptor(new CamelliaEncryptAesConfig("abc"));
    
    @Override
    public byte[] valueConvert(CommandContext commandContext, byte[] key, byte[] originalValue) {
        return encryptor.encrypt(compressor.compress(originalValue));
    }

    @Override
    public byte[] valueReverseConvert(CommandContext commandContext, byte[] key, byte[] convertedValue) {
        return compressor.decompress(encryptor.decrypt(convertedValue));
    }
}
````