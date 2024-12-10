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

package io.github.sollyu.test;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

import static io.github.sollyu.test.utils.StringUtils.hexStringToByteArray;

public class UnknownLengthTest implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte startFlag;

    @JavaStruct.Field(order = 1)
    public short length;

    @JavaStruct.Field(order = 2)
    public byte null1;

    @JavaStruct.Field(order = 3)
    public byte counter;

    @JavaStruct.Field(order = 4)
    public byte frameType;

    @JavaStruct.Field(order = 5)
    public byte serverId;

    @JavaStruct.Field(order = 6)
    public short source;

    @JavaStruct.Field(order = 7)
    public short target;

    @JavaStruct.Field(order = 8)
    public Data data;

    @JavaStruct.Field(order = 9)
    public short crc;

    @JavaStruct.Field(order = 10)
    public byte endFlag;

    public static class Data implements IJavaStruct {
        @JavaStruct.Field(order = 0)
        public short length;

        @JavaStruct.Field(order = 1)
        public byte functionId;     // 功能ID + 40

        @JavaStruct.Field(order = 2)
        public byte status;         // 0:成功 X:失败

        @JavaStruct.Field(order = 3)
        public byte[] data;

        @Override
        public int onFieldUnknownLength(int order) {
            if (order == 3) {
                return length - 0x01 - 0x01;
            }
            throw new RuntimeException("Unknown field size: " + order + ".");
        }
    }

    @Test
    public void test() {
        String hexString = "7e32000003002100000000260042000121000001010601010601010101050101050101060ef401030bfa000000000000000000a3727e";
        byte[] bytes = hexStringToByteArray(hexString);

        UnknownLengthTest output = new UnknownLengthTest();
        output.fromBytes(bytes, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(0x7E, output.startFlag);
        Assertions.assertEquals(0x7E, output.endFlag);
        Assertions.assertEquals(0x72A3, output.crc);
        Assertions.assertEquals(0x21, output.serverId);
        Assertions.assertEquals(0x42, output.data.functionId);
        Assertions.assertEquals(0x26, output.data.length);
        Assertions.assertEquals(0x24, output.data.data.length);
    }
}
