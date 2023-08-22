package io.github.sollyu.test.business;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import io.github.sollyu.test.utils.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

/**
 * 实际业务中使用的复杂结构体
 * 里面包含：
 * 1. 动态长度的数组
 * 2. 动态长度的子结构体
 * 3. 动态长度的子结构体数组
 */
public class CommandTest implements IJavaStruct {

    @JavaStruct.Field(order = 1)
    public byte serverId;

    @JavaStruct.Field(order = 2)
    public short source;

    @JavaStruct.Field(order = 3)
    public short target;

    @JavaStruct.Field(order = 8)
    public Data data;

    public static class Data implements IJavaStruct {
        @JavaStruct.Field(order = 0)
        public short length;

        @JavaStruct.Field(order = 1)
        public byte functionId;     // 功能ID + 40

        @JavaStruct.Field(order = 3)
        public byte[] data;

        @Override
        public int onFieldUnknownLength(int order) {
            if (order == 3) {
                return length - 0x01;
            }
            throw new RuntimeException("Unknown field size: " + order + ".");
        }

        @Override
        public void onFromBytesFinished() {
            IJavaStruct.super.onFromBytesFinished();
            switch (this.functionId) {
                case 0x04:
                    var data_ = new Item[]{
                            new Item(), new Item(), new Item(), new Item(), new Item(),
                            new Item(), new Item(), new Item(), new Item(), new Item(),
                            new Item(), new Item(), new Item(), new Item(), new Item(),
                    };
                    try {
                        JavaStruct.unpack(data, data_, ByteOrder.LITTLE_ENDIAN);
                    } catch (Exception ignored) {
                    }
                    int dataSize = 0;
                    for (int i = 0; i < data_.length; i++) {
                        if (data_[i].subId == 0x00 && data_[i].data == null) {
                            dataSize = i;
                            break;
                        }
                    }
                    this.data_ = new Item[dataSize];
                    System.arraycopy(data_, 0, this.data_, 0, dataSize);
                    break;
            }
        }

        /**
         * 根据功能ID，动态解析数据
         * 使用时，需要根据功能ID，强制转换为对应的类型
         */
        @Nullable
        private Item[] data_ = null;

        public Item[] getData() {
            return data_;
        }

        public static class Item implements IJavaStruct {
            @JavaStruct.Field(order = 0)
            public byte subId;

            @JavaStruct.Field(order = 1, sizeof = "data")
            public short length;

            @JavaStruct.Field(order = 2)
            public byte[] data;

            @Override
            public void onFromBytesFinished() {
                IJavaStruct.super.onFromBytesFinished();
                switch (this.subId) {
                    case 0x03: {
                        var data_ = new Data03();
                        try {
                            JavaStruct.unpack(data, data_, ByteOrder.LITTLE_ENDIAN);
                        } catch (Exception ignored) {
                        }
                        this.data_ = data_;
                        break;
                    }

                    case 0x05: {
                        var data_ = new Data05();
                        try {
                            JavaStruct.unpack(data, data_, ByteOrder.LITTLE_ENDIAN);
                        } catch (Exception ignored) {
                        }
                        this.data_ = data_;
                        break;
                    }


                    case 0x0C: {
                        var data_ = new Data0C[]{
                                new Data0C(), new Data0C(), new Data0C(), new Data0C(), new Data0C(),
                                new Data0C(), new Data0C(), new Data0C(), new Data0C(), new Data0C(),
                        };
                        try {
                            JavaStruct.unpack(data, data_, ByteOrder.LITTLE_ENDIAN);
                        } catch (Exception ignored) {
                        }
                        int dataSize = 0;
                        for (int i = 0; i < data_.length; i++) {
                            if (data_[i].subId == 0x00 && data_[i].data == null) {
                                dataSize = i;
                                break;
                            }
                        }
                        this.data_ = new Data0C[dataSize];
                        System.arraycopy(data_, 0, this.data_, 0, dataSize);
                        break;
                    }
                }
            }

            private Object data_ = null;

            public Object getData() {
                return data_;
            }

            public static class Data03 implements IJavaStruct {
                @JavaStruct.Field(order = 1)
                public int frame;

                @JavaStruct.Field(order = 2)
                public int mask ;
            }

            public static class Data05 implements IJavaStruct {
                @JavaStruct.Field(order = 0, sizeof = "data")
                public short length;

                @JavaStruct.Field(order = 1)
                public Item1[] data;

                public static class Item1 implements IJavaStruct {
                    @JavaStruct.Field(order = 0)
                    public short length;

                    @JavaStruct.Field(order = 1)
                    public int frame;

                    @JavaStruct.Field(order = 2)
                    public byte[] data;

                    @Override
                    public int onFieldUnknownLength(int order) {
                        if (order == 2) {
                            return length - 0x04;
                        }
                        throw new RuntimeException("Unknown field size: " + order + ".");
                    }
                }

            }

            public static class Data0C implements IJavaStruct {
                @JavaStruct.Field(order = 0)
                public byte subId;

                @JavaStruct.Field(order = 1, sizeof = "data")
                public short length;

                @JavaStruct.Field(order = 2)
                public byte[] data;
            }
        }

    }

    @Test
    public void test() {
        String hexString = "23 00 00 00 00 67 00 04 01 01 00 00 02 08 00 64 00 00 00 05 00 00 00 03 08 00 E8 07 00 00 " +
                "FF FF FF FF 04 03 00 01 00 00 05 0F 00 01 00 0B 00 E0 07 00 00 23 14 51 81 16 B7 01 06 03 00 01 00 " +
                "00 07 01 00 01 08 01 00 01 09 01 00 01 0A 0C 00 E0 07 00 00 30 00 00 00 00 00 00 00 0B 02 00 00 00 " +
                "0C 0B 00 01 01 00 02 02 04 00 10 27 01 78";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);

        CommandTest commandTest = new CommandTest();
        commandTest.fromBytes(bytes, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(commandTest.data.getData().length, 12);

        {
            var item02 = commandTest.data.getData()[2];
            Assertions.assertInstanceOf(Data.Item.Data03.class,item02.getData());
            var data03 = (Data.Item.Data03) item02.getData();
            Assertions.assertEquals(data03.frame, 0x07E8);
            Assertions.assertEquals(data03.mask, 0xFFFFFFFF);
        }

        {
            var item04 = commandTest.data.getData()[4];
            Assertions.assertInstanceOf(Data.Item.Data05.class,item04.getData());
            var data05 = (Data.Item.Data05) item04.getData();
            Assertions.assertEquals(data05.length, 0x01);
            Assertions.assertEquals(data05.data.length, 0x01);
            Assertions.assertEquals(data05.data[0].length, 0x0B);
            Assertions.assertEquals(data05.data[0].frame, 0x07E0);
            Assertions.assertEquals(data05.data[0].data.length, 0x07);
        }

        {
            var item0B = commandTest.data.getData()[11];
            Assertions.assertInstanceOf(Data.Item.Data0C[].class,item0B.getData());
            var data0C = (Data.Item.Data0C[]) item0B.getData();
            Assertions.assertEquals(data0C.length, 2);
            var data0C0 = data0C[0];
            Assertions.assertEquals(data0C0.subId, 0x01);
            Assertions.assertEquals(data0C0.length, 0x01);
            Assertions.assertEquals(data0C0.data.length, 0x01);
            Assertions.assertEquals(data0C0.data[0], 0x02);
        }

    }

}
