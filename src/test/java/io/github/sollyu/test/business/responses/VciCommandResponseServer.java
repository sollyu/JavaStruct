package io.github.sollyu.test.business.responses;

import java.nio.ByteOrder;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

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
