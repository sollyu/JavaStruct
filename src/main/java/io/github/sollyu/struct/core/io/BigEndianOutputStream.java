package io.github.sollyu.struct.core.io;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class BigEndianOutputStream extends DataOutputStream {
    public BigEndianOutputStream(OutputStream out) {
        super(out);
    }
}
