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

public class VciCommandResponseEven implements IJavaStruct, VciCommandResponseBase.IData {

    @JavaStruct.Field(order = 0)
    public short counters;

    @JavaStruct.Field(order = 1)
    public byte frameType;

    @JavaStruct.Field(order = 2)
    public VciCommandResponseServer server = new VciCommandResponseServer();

    @JavaStruct.Field(order = 8)
    public short crc;

    @Override
    public VciCommandResponseServer getServer() {
        return server;
    }

    @Override
    public short getCrc() {
        return crc;
    }


//    @Override
//    public void onFromBytesFinished() {
//        VciCommandResponseBase.Sub[] data_ = new VciCommandResponseBase.Sub[]{
//            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
//            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
//            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
//        };
//        try {
//            JavaStruct.unpack(data, data_, ByteOrder.LITTLE_ENDIAN);
//        } catch (Exception ignored) {
//        }
//        int dataSize = 0;
//        for (int i = 0; i < data_.length; i++) {
//            if (data_[i].subId == 0x00 && data_[i].data == null) {
//                dataSize = i;
//                break;
//            }
//        }
//        this.sub = new VciCommandResponseBase.Sub[dataSize];
//        System.arraycopy(data_, 0, this.sub, 0, dataSize);
//    }
}