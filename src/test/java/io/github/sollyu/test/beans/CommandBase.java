package io.github.sollyu.test.beans;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;

public class CommandBase<T extends IJavaStruct> implements IJavaStruct {

    public CommandBase(T data) {
        this.data = data;
    }

    @JavaStruct.Field(order = 0)
    public byte startFlag;

    @JavaStruct.Field(order = 1)
    public short length;

    @JavaStruct.Field(order = 2)
    public T data;

    @JavaStruct.Field(order = 3)
    public byte endFlag;

}
