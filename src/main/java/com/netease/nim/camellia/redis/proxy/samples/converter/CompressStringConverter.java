package com.netease.nim.camellia.redis.proxy.samples.converter;

import com.netease.nim.camellia.redis.proxy.command.CommandContext;
import com.netease.nim.camellia.redis.proxy.plugin.converter.StringConverter;
import com.netease.nim.camellia.tools.compress.CamelliaCompressor;

/**
 * Created by caojiajun on 2021/8/16
 */
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
