package com.netease.nim.camellia.redis.proxy.samples.converter;

import com.netease.nim.camellia.redis.proxy.command.CommandContext;
import com.netease.nim.camellia.redis.proxy.plugin.converter.KeyConverter;
import com.netease.nim.camellia.redis.proxy.enums.RedisCommand;
import com.netease.nim.camellia.redis.proxy.util.Utils;

/**
 * Created by caojiajun on 2021/8/19
 */
public class CustomKeyConverter implements KeyConverter {

    public static final String CONVERTED_KEY_FORMAT = "%d|%s|%s";
    public static final String CONVERTED_KEY_WITHOUT_ORIGIN_KEY_FORMAT = "%d|%s|";

    @Override
    public byte[] convert(CommandContext commandContext, RedisCommand redisCommand, byte[] originalKey) {
        if (commandContext.getBid() == null || commandContext.getBgroup() == null) {
            return originalKey; // TODO: change logic here
        }
        long bid = commandContext.getBid();
        String bgroup = commandContext.getBgroup();
        String originalKeyStr = Utils.bytesToString(originalKey);
        String convertedKeyStr = String.format(CONVERTED_KEY_FORMAT, bid, bgroup, originalKeyStr);
        return Utils.stringToBytes(convertedKeyStr);
    }

    @Override
    public byte[] reverseConvert(CommandContext commandContext, RedisCommand redisCommand, byte[] convertedKey) {
        if (commandContext.getBid() == null || commandContext.getBgroup() == null) {
            return convertedKey; // TODO: change logic here
        }
        long bid = commandContext.getBid();
        String bgroup = commandContext.getBgroup();
        String convertedKeyStr = Utils.bytesToString(convertedKey);
        String originalKeyStr = convertedKeyStr.substring(String.format(CONVERTED_KEY_WITHOUT_ORIGIN_KEY_FORMAT, bid, bgroup).length());
        return Utils.stringToBytes(originalKeyStr);
    }
}
