/*
 * Copyright 2023 sollyu.com.
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
import io.github.sollyu.test.utils.ByteArrayUtils;
import io.github.sollyu.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;
import java.util.Arrays;

public class StructNameTest implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte startFlag;

    @JavaStruct.Field(order = 1, sizeof = "data")
    public short length;

    @JavaStruct.Field(order = 2, name = "data")
    public byte[] data1;

    @JavaStruct.Field(order = 3)
    public byte endFlag;

    @Test
    public void unpack() {
        String hexString = "7E 0C 00 00 00 00 00 00 00 00 00 00 00 00 00 7E";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);
        StructNameTest output = new StructNameTest();
        output.fromBytes(bytes, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(output.startFlag, 0x7E);
        Assertions.assertEquals(output.length, 0x000C);
        Assertions.assertEquals(output.data1.length, 0x000C);
        Assertions.assertEquals(output.endFlag, 0x7E);
    }

    @Test
    public void pack() {
        StructNameTest input = new StructNameTest();
        input.startFlag = 0x7E;
        input.data1 = new byte[0x00C];
        input.endFlag = 0x7E;

        // fill 0xFF
        Arrays.fill(input.data1, (byte) 0xFF);

        byte[] output = input.toBytes(ByteOrder.LITTLE_ENDIAN);
        String hexString = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertEquals(input.length, 0x000C);
        Assertions.assertEquals(hexString, "7E 0C 00 FF FF FF FF FF FF FF FF FF FF FF FF 7E");
    }


}
