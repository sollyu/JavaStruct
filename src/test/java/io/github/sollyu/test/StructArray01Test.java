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
import io.github.sollyu.test.beans.CommandItem01;
import io.github.sollyu.test.utils.ByteArrayUtils;
import io.github.sollyu.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class StructArray01Test implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte serverId;

    @JavaStruct.Field(order = 1, sizeof = "data")
    public short length;

    @JavaStruct.Field(order = 2)
    public CommandItem01[] data;

    @Test
    public void packNull() {
        StructArray01Test input = new StructArray01Test();
        input.serverId = 0x01;
        input.data = null;
        input.length = -1;

        byte[] output = JavaStruct.pack(input, ByteOrder.LITTLE_ENDIAN);
        String hexString = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertEquals(hexString, "01 00 00");
        Assertions.assertEquals(input.length, 0x0000);
    }

    @Test void pack() {
        StructArray01Test input = new StructArray01Test();
        input.serverId = 0x01;
        CommandItem01 item1 = new CommandItem01((byte) 0x01, (byte) 0x06, (byte) 0x0E);
        CommandItem01 item2 = new CommandItem01((byte) 0x00, (byte) 0x06, (byte) 0x0E);
        input.data = new CommandItem01[] { item1, item2 };

        byte[] output = JavaStruct.pack(input, ByteOrder.LITTLE_ENDIAN);
        String hexString = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertEquals(hexString, "01 02 00 01 03 00 01 06 0E 01 03 00 00 06 0E");
        Assertions.assertEquals(input.length, 0x0002);
    }

    @Test
    public void unpackNull() {
        String hexString = "01 00 00";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);
        StructArray01Test output = new StructArray01Test();
        output.data = null;
        output.length = -1;
        JavaStruct.unpack(bytes, output, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(output.serverId, 0x01);
        Assertions.assertEquals(output.length, 0x0000);
        Assertions.assertEquals(output.data.length, 0x0000);
    }

    @Test
    public void unpack() {
        String hexString = "01 02 00 01 03 00 01 06 0E 01 03 00 00 06 0E";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);
        StructArray01Test output = new StructArray01Test();
        JavaStruct.unpack(bytes, output, ByteOrder.LITTLE_ENDIAN);

        Assertions.assertEquals(output.serverId, 0x01);
        Assertions.assertEquals(output.length, 0x0002);
        Assertions.assertEquals(output.data.length, 0x0002);
        Assertions.assertEquals(output.data[0].resistance, 0x01);
        Assertions.assertEquals(output.data[0].canH, 0x06);
        Assertions.assertEquals(output.data[0].canL, 0x0E);
        Assertions.assertEquals(output.data[1].resistance, 0x00);
        Assertions.assertEquals(output.data[1].canH, 0x06);
        Assertions.assertEquals(output.data[1].canL, 0x0E);
    }
}
