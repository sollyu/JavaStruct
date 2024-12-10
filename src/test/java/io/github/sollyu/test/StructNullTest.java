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
import io.github.sollyu.test.utils.ByteArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class StructNullTest implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte flag;

    @Nullable
    @JavaStruct.Field(order = 1)
    public Item1 item1;

    @Nullable
    @JavaStruct.Field(order = 2)
    public Item2 item2;

    public static class Item1 implements IJavaStruct {
        @JavaStruct.Field(order = 0)
        public short item1;

        @JavaStruct.Field(order = 1)
        public byte[] item2;
    }

    public static class Item2 implements IJavaStruct {
        @JavaStruct.Field(order = 0)
        public short item1;

        @JavaStruct.Field(order = 1)
        public byte item2;
    }

    @Test
    public void pack() {
        Item2 item2 = new Item2();
        item2.item1 = 0x21;
        item2.item2 = 0x22;

        StructNullTest structNullTest = new StructNullTest();
        structNullTest.flag = 0x7E;
        structNullTest.item2 = item2;

        byte[] output = structNullTest.toBytes(ByteOrder.LITTLE_ENDIAN);
        String hexString = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertNull(structNullTest.item1);
        Assertions.assertEquals(hexString, "7E 21 00 22");
    }

}
