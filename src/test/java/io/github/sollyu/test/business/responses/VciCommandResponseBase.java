/*
 * Copyright (c) 2023-2025 sollyu.com..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.sollyu.test.business.responses;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import io.github.sollyu.struct.core.io.BigEndianInputStream;
import io.github.sollyu.struct.core.io.LittleEndianOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class VciCommandResponseBase<T extends VciCommandResponseBase.IData> implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte start;

    @JavaStruct.Field(order = 1)
    public short length;

    @JavaStruct.Field(order = 2)
    public T data;

    @JavaStruct.Field(order = 3)
    public byte end;

    public interface IData extends IJavaStruct {

        VciCommandResponseServer getServer();

        short getCrc();
    }

    /**
     * 全局替换
     * 0x7d 0x5d 替换为 0x7d
     * 0x7d 0x5e 替换为 0x7e
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
        final byte[] byteArray = replace(bytes);
        final VciCommandResponseBase<IData> beanCommandBase = new VciCommandResponseBase<>();
        beanCommandBase.data = new VciCommandResponseEven();
        beanCommandBase.fromBytes(byteArray, ByteOrder.LITTLE_ENDIAN);
        if (beanCommandBase.data.getServer().size % 2 != 0) {
            beanCommandBase.data = new VciCommandResponseOdd();
            beanCommandBase.fromBytes(byteArray, ByteOrder.LITTLE_ENDIAN);
        }
        return beanCommandBase;
    }

    public static byte[] toBytes(IData data) {
        final VciCommandResponseBase<IData> beanCommandBase = new VciCommandResponseBase<>();
        beanCommandBase.start = 0x00;
        beanCommandBase.data = data;
        beanCommandBase.end = 0x00;
        final byte[] bytes = beanCommandBase.toBytes(ByteOrder.LITTLE_ENDIAN);

        // 计算长度
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final LittleEndianOutputStream littleEndianOutputStream = new LittleEndianOutputStream(byteArrayOutputStream);
            littleEndianOutputStream.writeShort((short) bytes.length - 2 /* flag */ - 2 /* length */);
            littleEndianOutputStream.flush();
            final byte[] length = byteArrayOutputStream.toByteArray();
            bytes[1] = length[0];
            bytes[2] = length[1];
            littleEndianOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 计算CRC
        try {
            long crc = 0;
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            final BigEndianInputStream bigEndianInputStream = new BigEndianInputStream(byteArrayInputStream);
            for (int i = 1; i < bytes.length - 1; i = i + 2) {
                crc += bigEndianInputStream.readUnsignedShort();
            }
            bigEndianInputStream.close();
            byteArrayInputStream.close();

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final LittleEndianOutputStream littleEndianOutputStream = new LittleEndianOutputStream(byteArrayOutputStream);
            littleEndianOutputStream.writeLong(crc);
            littleEndianOutputStream.flush();
            final byte[] crc1 = byteArrayOutputStream.toByteArray();
            bytes[bytes.length - 3] = crc1[0];
            bytes[bytes.length - 2] = crc1[1];
            littleEndianOutputStream.close();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 数据替换
        // 7d -> 0x7d 0x5d
        // 7e -> 0x7d 0x5e
        final ArrayList<Byte> list = new ArrayList<>();
        for (byte b : bytes) {
            if (b == 0x7d) {
                list.add((byte) 0x7d);
                list.add((byte) 0x5d);
            } else if (b == 0x7e) {
                list.add((byte) 0x7d);
                list.add((byte) 0x5e);
            } else {
                list.add(b);
            }
        }

        // 设置flag
        final byte[] bytes2 = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            bytes2[i] = list.get(i);
        }

        // set flag
        bytes2[0] = 0x7e;
        bytes2[bytes2.length - 1] = 0x7e;
        return bytes2;
    }

    public static class Sub implements IJavaStruct {
        @JavaStruct.Field(order = 1)
        public byte subId;

        @JavaStruct.Field(order = 2, sizeof = "data")
        public short size;

        @JavaStruct.Field(order = 3)
        public byte[] data;
    }

}
