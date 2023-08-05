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
import io.github.sollyu.test.beans.CommandItem01;
import io.github.sollyu.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class StructTest implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte serverId = 0x10;

    @JavaStruct.Field(order = 2)
    public CommandItem01 data;

    @Test
    public void pack() {
        CommandItem01 odb = new CommandItem01();
        odb.resistance = 0x00;
        odb.canH = 0x06;
        odb.canL = 0x0E;

        StructTest input = new StructTest();
        input.data = odb;

        byte[] output = JavaStruct.pack(input, ByteOrder.LITTLE_ENDIAN);
        String output1 = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertEquals("10 01 03 00 00 06 0E", output1);
    }

    @Test
    public void unpack() {
        String hexString = "10 01 03 00 00 06 0E";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);

        StructTest output = new StructTest();
        JavaStruct.unpack(bytes, output, ByteOrder.LITTLE_ENDIAN);
        Assertions.assertEquals(0x10, output.serverId);
        Assertions.assertEquals(0x01, output.data.id);
        Assertions.assertEquals(0x03, output.data.length);
        Assertions.assertEquals(0x00, output.data.resistance);
        Assertions.assertEquals(0x06, output.data.canH);
        Assertions.assertEquals(0x0E, output.data.canL);
    }

}
