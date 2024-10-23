package io.github.sollyu.test.business.responses;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public interface VciCommandResponseFunId extends IJavaStruct {

    @Override
    default void fromBytes(byte @NotNull [] data0, @NotNull ByteOrder  byteOrder) {
        byte funId = data0[0];
        byte state = data0[1];

        byte[] data1 = new byte[data0.length - 2];
        System.arraycopy(data0, 2, data1, 0, data1.length);

        VciCommandResponseBase.Sub[] data_ = new VciCommandResponseBase.Sub[]{
            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
            new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(), new VciCommandResponseBase.Sub(),
        };
        try {
            JavaStruct.unpack(data1, data_, ByteOrder.LITTLE_ENDIAN);
        } catch (Exception ignored) {
        }
        int dataSize = 0;
        for (int i = 0; i < data_.length; i++) {
            if (data_[i].subId == 0x00 && data_[i].data == null) {
                dataSize = i;
                break;
            }
        }
        VciCommandResponseBase.Sub[] sub = new VciCommandResponseBase.Sub[dataSize];
        System.arraycopy(data_, 0, sub, 0, dataSize);
        fromBytes(funId, sub);
    }

    void fromBytes(byte funId, VciCommandResponseBase.Sub[] subs);

}
