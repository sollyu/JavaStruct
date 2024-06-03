package io.github.sollyu.test.business;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class VciCommandResponseOdd implements IJavaStruct, VciCommandResponseBase.IData {

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
    public byte odd;

    @JavaStruct.Field(order = 7)
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
