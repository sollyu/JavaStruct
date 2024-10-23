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