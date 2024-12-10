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

package io.github.sollyu.test.business;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class VciCommandResponseEven implements IJavaStruct, VciCommandResponseBase.IData {

    @JavaStruct.Field(order = 0)
    public short counters;

    @JavaStruct.Field(order = 1)
    public byte frameType;

    @JavaStruct.Field(order = 2)
    public byte serverId;

    @JavaStruct.Field(order = 3)
    public int ignore;

    @JavaStruct.Field(order = 4, sizeof = "data")
    public short size;

    @JavaStruct.Field(order = 5)
    public byte[] data;

    @JavaStruct.Field(order = 6)
    public short crc;

    @Override
    public short getCounters() {
        return counters;
    }

    @Override
    public byte getFrameType() {
        return frameType;
    }

    @Override
    public short getSize() {
        return size;
    }

    @Override
    public byte getServerId() {
        return serverId;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public short getCrc() {
        return crc;
    }

}
