package io.github.sollyu.test.beans;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class CommandData implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public short counters;

    @JavaStruct.Field(order = 1)
    public byte frameType;

    @JavaStruct.Field(order = 2)
    public byte serverId;

    @JavaStruct.Field(order = 3, sizeof = "data")
    public short dataLength;

    @JavaStruct.Field(order = 4)
    public byte[] data;

    @JavaStruct.Field(order = 5)
    public byte odd;

    @JavaStruct.Field(order = 6)
    public short crc;


}
