package io.github.sollyu.test;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import io.github.sollyu.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class Server14Test implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public short counter;

    @JavaStruct.Field(order = 1)
    public byte frameType;

    @JavaStruct.Field(order = 2)
    public byte serverId;

    @JavaStruct.Field(order = 3)
    public byte itemId;

    @JavaStruct.Field(order = 4, sizeof = "data")
    public byte size;

    @JavaStruct.Field(order = 5)
    public Can[] data;

    @JavaStruct.Field(order = 6)
    public short crc;

    public static class Can implements IJavaStruct {
        @JavaStruct.Field(order = 0)
        public int timestamp;

        @JavaStruct.Field(order = 1)
        public int counter;

        @JavaStruct.Field(order = 2)
        public int frame;

        @JavaStruct.Field(order = 3)
        public byte[] data = new byte[8];
    }

    @Test
    public void unpack01() {
        String hexString = "000010140101786a8056790301000003f00cfbfe00ffffffffff88e6";
        byte[] data = StringUtils.hexStringToByteArray(hexString);
        Server14Test server14Test = new Server14Test();
        JavaStruct.unpack(data, server14Test, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(0x14, server14Test.serverId);
        Assertions.assertEquals(0x01, server14Test.itemId);
        Assertions.assertEquals(0x01, server14Test.size);
        Assertions.assertEquals(0xCF00300, server14Test.data[0].frame);
        Assertions.assertEquals((short) 0xE688, server14Test.crc);
    }

}
