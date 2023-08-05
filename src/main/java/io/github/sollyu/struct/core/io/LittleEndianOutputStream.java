package io.github.sollyu.struct.core.io;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class LittleEndianOutputStream extends FilterOutputStream implements DataOutput {

    private final DataOutputStream dataOutputStream;

    public LittleEndianOutputStream(OutputStream out) {
        super(out);
        dataOutputStream = new DataOutputStream(out);
    }

    @Override
    public void write(int b) throws IOException {
        dataOutputStream.write(Integer.reverseBytes(b));
    }

    @Override
    public void write(byte @NotNull [] b) throws IOException {
        dataOutputStream.write(b);
    }

    @Override
    public void write(byte @NotNull [] b, int off, int len) throws IOException {
        dataOutputStream.write(b, off, len);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        dataOutputStream.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        dataOutputStream.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        dataOutputStream.writeShort(Short.reverseBytes((short) v));
    }

    @Override
    public void writeChar(int v) throws IOException {
        dataOutputStream.writeChar(Character.reverseBytes((char) v));
    }

    @Override
    public void writeInt(int v) throws IOException {
        dataOutputStream.writeInt(Integer.reverseBytes(v));
    }

    @Override
    public void writeLong(long v) throws IOException {
        dataOutputStream.writeLong(Long.reverseBytes(v));
    }

    @Override
    public void writeFloat(float v) throws IOException {
        dataOutputStream.writeFloat(Float.intBitsToFloat(Integer.reverseBytes(Float.floatToIntBits(v))));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        dataOutputStream.writeDouble(Double.longBitsToDouble(Long.reverseBytes(Double.doubleToLongBits(v))));
    }

    @Override
    public void writeBytes(@NotNull String s) throws IOException {
        dataOutputStream.writeBytes(s);
    }

    @Override
    public void writeChars(@NotNull String s) throws IOException {
        dataOutputStream.writeChars(s);
    }

    @Override
    public void writeUTF(@NotNull String s) throws IOException {
        dataOutputStream.writeUTF(s);
    }
}
