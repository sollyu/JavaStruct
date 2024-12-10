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

package io.github.sollyu.test.beans;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class CommandItem01 implements IJavaStruct {

    public CommandItem01() {
    }

    public CommandItem01(byte resistance, byte canH, byte canL) {
        this.resistance = resistance;
        this.canH = canH;
        this.canL = canL;
    }

    @JavaStruct.Field(order = 0)
    public byte id = 0x01;

    @JavaStruct.Field(order = 1)
    public short length = 0x0003;

    @JavaStruct.Field(order = 2)
    public byte resistance;

    @JavaStruct.Field(order = 3)
    public byte canH;

    @JavaStruct.Field(order = 4)
    public byte canL;
}