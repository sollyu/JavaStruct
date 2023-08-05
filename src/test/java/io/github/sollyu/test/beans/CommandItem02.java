package io.github.sollyu.test.beans;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class CommandItem02 implements IJavaStruct {
    @JavaStruct.Field(order = 0)
    public byte id = 0x03;

    @JavaStruct.Field(order = 1)
    public short length = 0x0005;

    @JavaStruct.Field(order = 2)
    public byte canMode = 0x01;

    @JavaStruct.Field(order = 3)
    public int baudRate = 0x00000000;
}
