package io.github.sollyu.test.beans;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class CommandItem01 implements IJavaStruct {

    public CommandItem01() {
    }

    public CommandItem01(byte resistance, byte canH, byte canL) {
        this.resistance = resistance;
        this.canH = canH;
        this.canL = canL;
    }

    @JavaStruct.Field(order = 0)
    public byte id = 0x01;

    @JavaStruct.Field(order = 1)
    public short length = 0x0003;

    @JavaStruct.Field(order = 2)
    public byte resistance;

    @JavaStruct.Field(order = 3)
    public byte canH;

    @JavaStruct.Field(order = 4)
    public byte canL;
}