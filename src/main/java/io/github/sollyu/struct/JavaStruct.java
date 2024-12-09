/*
 * Copyright 2023 sollyu.com.
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

package io.github.sollyu.struct;

import io.github.sollyu.struct.core.JavaStructPack;
import io.github.sollyu.struct.core.JavaStructUnpack;
import io.github.sollyu.struct.core.io.BigEndianInputStream;
import io.github.sollyu.struct.core.io.LittleEndianInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.ByteOrder;

public class JavaStruct {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {

        /**
         * 排序
         *
         * @return
         */
        int order();

        /**
         * 数字变量的长度
         *
         * @return
         */
        String sizeof() default "";

        /**
         * 字段名称
         *
         * @return
         */
        String name() default "";
    }

    /**
     * 解包
     *
     * @param data      数据
     * @param output    输出对象
     * @param byteOrder 字节序
     */
    public static void unpack(byte @NotNull [] data, @NotNull IJavaStruct output, @NotNull ByteOrder byteOrder) {
        new JavaStructUnpack(data, output, byteOrder).run();
    }

    public static void unpack(byte @NotNull [] data, IJavaStruct @NotNull [] output, @NotNull ByteOrder byteOrder) {
        DataInput dataInputStream = byteOrder == ByteOrder.BIG_ENDIAN ?
                new BigEndianInputStream(new ByteArrayInputStream(data)) :
                new LittleEndianInputStream(new ByteArrayInputStream(data));

        for (IJavaStruct iJavaStruct : output) {
            new JavaStructUnpack(dataInputStream, iJavaStruct).run();
        }
    }

    /**
     * 打包
     *
     * @param input     输入对象
     * @param byteOrder 字节序
     * @return 数据
     */
    public static byte[] pack(@NotNull IJavaStruct input, @NotNull ByteOrder byteOrder) {
        return new JavaStructPack(input, byteOrder).get();
    }

}
