package io.github.sollyu.struct.core.io;

import java.io.DataInputStream;
import java.io.InputStream;

public class BigEndianInputStream extends DataInputStream {
    public BigEndianInputStream(InputStream in) {
        super(in);
    }

}
