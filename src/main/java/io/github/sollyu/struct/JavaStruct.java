package io.github.sollyu.struct;

import io.github.sollyu.struct.core.JavaStructPack;
import io.github.sollyu.struct.core.JavaStructUnpack;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteOrder;

public class JavaStruct {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        int order();

        String sizeof() default "";
    }

    public static void unpack(byte @NotNull [] data, @NotNull IJavaStruct output, @NotNull ByteOrder byteOrder) {
        new JavaStructUnpack(data, output, byteOrder).run();
    }

    public static byte[] pack(@NotNull IJavaStruct input, @NotNull ByteOrder byteOrder) {
        return new JavaStructPack(input, byteOrder).get();
    }

}
