package io.github.sollyu.test.business;


import java.nio.ByteOrder;
import java.util.ArrayList;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class VciCommandResponseBase<T extends IJavaStruct> implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte start;

    @JavaStruct.Field(order = 1)
    public short length;

    @JavaStruct.Field(order = 2)
    public T data;

    @JavaStruct.Field(order = 3)
    public byte end;

    public interface IData extends IJavaStruct {
        short getCounters();

        byte getFrameType();

        short getSize();

        byte getServerId();

        byte[] getData();

        short getCrc();

    }

    /**
     * 全局替换
     *   0x7d 0x5d 替换为 0x7d
     *   0x7d 0x5e 替换为 0x7e
     */
    public static byte[] replace(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (i + 1 >= bytes.length) {
                list.add(b);
                continue;
            }
            if (b != 0x7d) {
                list.add(b);
                continue;
            }

            byte n = bytes[i + 1];
            if (n == 0x5d) {
                list.add((byte) 0x7d);
                i++;
            } else if (n == 0x5e) {
                list.add((byte) 0x7e);
                i++;
            } else {
                list.add(b);
            }
        }
        byte[] bytes3 = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            bytes3[i] = list.get(i);
        }
        return bytes3;
    }

    /**
     * 解析7E命令
     */
    public static VciCommandResponseBase<IData> parse(byte[] bytes) {
        byte[] byteArray = replace(bytes);
        VciCommandResponseBase<IData> beanCommandBase = new VciCommandResponseBase<>();
        beanCommandBase.data = new VciCommandResponseEven();
        beanCommandBase.fromBytes(byteArray, ByteOrder.LITTLE_ENDIAN);
        if (beanCommandBase.length % 2 == 0) {
            beanCommandBase.data = new VciCommandResponseOdd();
            beanCommandBase.fromBytes(byteArray, ByteOrder.LITTLE_ENDIAN);
        }
        return beanCommandBase;
    }

}
