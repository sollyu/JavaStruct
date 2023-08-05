package io.github.sollyu.struct.core.util;

import io.github.sollyu.struct.JavaStruct;

import java.util.Comparator;

public class JavaStructFieldComparator implements Comparator<JavaStruct.Field> {
    @Override
    public int compare(JavaStruct.Field o1, JavaStruct.Field o2) {
        return o1.order() - o2.order();
    }
}