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

import java.nio.ByteOrder;

public class VciCommandResponseServer implements IJavaStruct {

    @JavaStruct.Field(order = 1)
    public byte serverId;

    @JavaStruct.Field(order = 2)
    public int ignore;

    @JavaStruct.Field(order = 3, sizeof = "data")
    public short size;

    @JavaStruct.Field(order = 4)
    public byte[] data;

    public byte getFunId() {
        return data[0];
    }

    public byte getState() {
        return data[1];
    }

    public static VciCommandResponseServer from(byte[] bytes) {
        VciCommandResponseServer server = new VciCommandResponseServer();
        server.fromBytes(bytes, ByteOrder.LITTLE_ENDIAN);
        return server;
    }

}
