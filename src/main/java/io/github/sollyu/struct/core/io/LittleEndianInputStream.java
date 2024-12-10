/*
 * Copyright (c) 2023-2025 sollyu.com..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.sollyu.struct.core.io;

import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream implements DataInput {

    private final DataInputStream dataInputStream;

    public LittleEndianInputStream(InputStream in) {
        dataInputStream = new DataInputStream(in);
    }

    @Override
    public void readFully(@NotNull byte[] b) throws IOException {
        dataInputStream.readFully(b);
    }

    @Override
    public void readFully(@NotNull byte[] b, int off, int len) throws IOException {
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
