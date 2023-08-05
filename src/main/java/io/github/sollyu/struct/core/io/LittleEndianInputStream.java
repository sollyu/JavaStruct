package io.github.sollyu.struct.core.io;

import org.jetbrains.annotations.NotNull;

import java.io.*;

public class LittleEndianInputStream implements DataInput {

    private final DataInputStream dataInputStream;

    public LittleEndianInputStream(InputStream in) {
        dataInputStream = new DataInputStream(in);
    }

    @Override
    public void readFully(byte @NotNull [] b) throws IOException {
        dataInputStream.readFully(b);
    }

    @Override
    public void readFully(byte @NotNull [] b, int off, int len) throws IOException {
        dataInputStream.readFully(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return dataInputStream.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return dataInputStream.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return dataInputStream.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return dataInputStream.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return Short.reverseBytes(dataInputStream.readShort());
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return Short.reverseBytes(dataInputStream.readShort()) & 0xFFFF;
    }

    @Override
    public char readChar() throws IOException {
        return Character.reverseBytes(dataInputStream.readChar());
    }

    @Override
    public int readInt() throws IOException {
        return Integer.reverseBytes(dataInputStream.readInt());
    }

    @Override
    public long readLong() throws IOException {
        return Long.reverseBytes(dataInputStream.readLong());
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(dataInputStream.readInt()));
    }


    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(Long.reverseBytes(dataInputStream.readLong()));
    }


    @Override
    @Deprecated
    public String readLine() throws IOException {
        return dataInputStream.readLine();
    }

    @NotNull
    @Override
    public String readUTF() throws IOException {
        return dataInputStream.readUTF();
    }
}
