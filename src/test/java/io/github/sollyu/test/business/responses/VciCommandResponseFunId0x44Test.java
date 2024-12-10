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

import com.google.common.hash.HashCode;
import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;
import java.util.Locale;

public class VciCommandResponseFunId0x44Test implements VciCommandResponseFunId {

    public VciCommandResponseBase.Sub sub0x01 = null;
    public VciCommandResponseBase.Sub sub0x02 = null;
    public VciCommandResponseBase.Sub sub0x03 = null;
    public VciCommandResponseBase.Sub sub0x04 = null;
    public VciCommandResponseBase.Sub sub0x05 = null;
    public VciCommandResponseBase.Sub sub0x06 = null;
    public Sub0x07 sub0x07 = null;
    public VciCommandResponseBase.Sub sub0x08 = null;
    public VciCommandResponseBase.Sub sub0x09 = null;
    public VciCommandResponseBase.Sub sub0x0A = null;
    public VciCommandResponseBase.Sub sub0x0B = null;
    public VciCommandResponseBase.Sub sub0x0C = null;
    public VciCommandResponseBase.Sub sub0x0D = null;
    public VciCommandResponseBase.Sub sub0x0E = null;

    @Override
    public void fromBytes(byte funId, VciCommandResponseBase.Sub[] subs) {
        if (funId != (byte) 0x44) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "Invalid funId, request: 0x44, current=0x%02X", funId));
        }

        for (VciCommandResponseBase.Sub sub : subs) {
            switch (sub.subId) {
                case 0x01:
                    sub0x01 = sub;
                    break;
                case 0x02:
                    sub0x02 = sub;
                    break;
                case 0x03:
                    sub0x03 = sub;
                    break;
                case 0x04:
                    sub0x04 = sub;
                    break;
                case 0x05:
                    sub0x05 = sub;
                    break;
                case 0x06:
                    sub0x06 = sub;
                    break;
                case 0x07:
                    sub0x07 = sub.size < 2 ? new Sub0x07Error() : new Sub0x07Success();
                    sub0x07.fromBytes(sub.data, ByteOrder.LITTLE_ENDIAN);
                    break;
                case 0x08:
                    sub0x08 = sub;
                    break;
                case 0x09:
                    sub0x09 = sub;
                    break;
                case 0x0A:
                    sub0x0A = sub;
                    break;
                case 0x0B:
                    sub0x0B = sub;
                    break;
                case 0x0C:
                    sub0x0C = sub;
                    break;
            }
        }
    }

    public interface Sub0x07 extends IJavaStruct {
        byte getState();

        Sub0x07Success caseSuccess();
    }

    public static class Sub0x07Error extends VciCommandResponseBase.Sub implements Sub0x07 {
        @Override
        public byte getState() {
            return data[0];
        }

        @Override
        public Sub0x07Success caseSuccess() {
            return null;
        }
    }

    public static class Sub0x07Success implements Sub0x07 {
        @JavaStruct.Field(order = 1)
        public byte state;

        @JavaStruct.Field(order = 2, sizeof = "data")
        public byte count;

        @JavaStruct.Field(order = 3, name = "data")
        public Item[] data;

        @Override
        public byte getState() {
            return state;
        }

        @Override
        public Sub0x07Success caseSuccess() {
            return this;
        }

        public static class Item implements IJavaStruct {
            @JavaStruct.Field(order = 3)
            public int frame;

            @JavaStruct.Field(order = 4, sizeof = "data")
            public short dataSize;

            @JavaStruct.Field(order = 5)
            public byte[] data;

            /**
             * 检查数据是否消极<br>
             * <p>
             * 03 00 7F 23 7F  //ECU正常消极, 23 7F为消极码<br>
             * 01 00 FF //ECU恢复超时 下位机填充的<br>
             *
             * @return true: 消性数据
             */
            public boolean isNegativeData() {
                if (dataSize == 0x01) {
                    return data[0] == (byte) 0xFF;
                }
                if (dataSize == 0x03) {
                    return data[0] == (byte) 0x7F;
                }
                return false;
            }

        }

    }

    public static VciCommandResponseFunId0x44Test from(VciCommandResponseServer base) {
        VciCommandResponseFunId0x44Test response = new VciCommandResponseFunId0x44Test();
        response.fromBytes(base.data, ByteOrder.LITTLE_ENDIAN);
        return response;
    }

    @Test
    public void test01() {
        String command = "7ec2080000002300000000b508440001010000020100000301000004010000050100000601000008010000090100000a0100000b0100000c0900000101000002010000077c08009be80700000800056300000000aaaae80700000800026300aaaaaaaaaae80700000800056300000000aaaae80700000800056300000000aaaae807000008000563c4634daeaaaae80700000800056300000000aaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae80700000800056300000000aaaae807000008000363fc93aaaaaaaae8070000080003630000aaaaaaaae80700000800036301fbaaaaaaaae8070000080003630000aaaaaaaae80700000800036301fbaaaaaaaae8070000080003630648aaaaaaaae8070000080003630651aaaaaaaae8070000080003630ccfaaaaaaaae8070000080003630b73aaaaaaaae8070000080003636146aaaaaaaae807000008000363618baaaaaaaae80700000800026300aaaaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003632710aaaaaaaae807000008000363d8f0aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003631666aaaaaaaae80700000800056300020000aaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae80700000800026311aaaaaaaaaae80700000800026301aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae80700000800056300000000aaaae80700000800056300001770aaaae80700000800056300001194aaaae80700000800056300000000aaaae8070000080003630935aaaaaaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800056342fd8000aaaae80700000800056344c3c02faaaae80700000800056344c3c02faaaae80700000800056300000000aaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae80700000800056300000000aaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae80700000800036303a3aaaaaaaae80700000800036304c2aaaaaaaae8070000080003630490aaaaaaaae807000008000363045eaaaaaaaae807000008000363060aaaaaaaaae8070000080003630606aaaaaaaae8070000080003635a6eaaaaaaaae8070000080003635a6eaaaaaaaae8070000080003630c81aaaaaaaae8070000080003630ba5aaaaaaaae8070000080003630c81aaaaaaaae8070000080003630b73aaaaaaaae8070000080003630c81aaaaaaaae807000008000363253daaaaaaaae80700000800036304b3aaaaaaaae807000008000363375daaaaaaaae80700000800026301aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae8070000080003630000aaaaaaaae8070000080003630333aaaaaaaae8070000080003636e7aaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae8070000080003630b19aaaaaaaae80700000800056300000000aaaae8070000080003630b2faaaaaaaae80700000800036309b1aaaaaaaae8070000080003636144aaaaaaaae8070000080003630000aaaaaaaae80700000800026301aaaaaaaaaae8070000080003630000aaaaaaaae80700000800026301aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800056300000000aaaae80700000800056300000000aaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800056300100001aaaae80700000800056300100001aaaae80700000800056300100001aaaae8070000080003630000aaaaaaaae80700000800056300000000aaaae807000008000263fdaaaaaaaaaae80700000800036313c5aaaaaaaae8070000080003630bb8aaaaaaaae8070000080003630bb8aaaaaaaae8070000080003630cb2aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae8070000080003637fffaaaaaaaae8070000080003637fffaaaaaaaae8070000080003630666aaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae8070000080003630aabaaaaaaaae8070000080003630aabaaaaaaaae8070000080003631343aaaaaaaae80700000800036313a7aaaaaaaae8070000080003630b73aaaaaaaae8070000080003630b72aaaaaaaae8070000080003630000aaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae8070000080003630000aaaaaaaae8070000080003630898aaaaaaaae8070000080003630000aaaaaaaae8070000080002630baaaaaaaaaae807000008000263ffaaaaaaaaaae80700000800056345800000aaaae8070000080003630000aaaaaaaae8070000080003630000aaaaaaaae80700000800056345800000aaaae80700000800056344807862aaaae80700000800026301aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026300aaaaaaaaaae80700000800026301aaaaaaaaaae80700000800026300aaaaaaaaaae807000008000363147baaaaaaaae80700000800056300000000aaaae80700000800026300aaaaaaaaaa00d0937e7e7e7e";
        VciCommandResponseBase<VciCommandResponseBase.IData> base = VciCommandResponseBase.parse(HashCode.fromString(command).asBytes());
        VciCommandResponseFunId0x44Test response = VciCommandResponseFunId0x44Test.from(base.data.getServer());

        Assertions.assertEquals(response.sub0x07.caseSuccess().data.length, 155);
    }

}
