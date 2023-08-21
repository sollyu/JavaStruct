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

import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

/**
 * 被标记为结构体的接口
 */
public interface IJavaStruct {

    /**
     * 打包
     *
     * @param byteOrder 字节序
     * @return 数据
     */
    default byte[] toBytes(@NotNull ByteOrder byteOrder) {
        return JavaStruct.pack(this, byteOrder);
    }


    /**
     * 解包
     *
     * @param data      数据
     * @param byteOrder 字节序
     */
    default void fromBytes(byte @NotNull [] data, @NotNull ByteOrder byteOrder) {
        JavaStruct.unpack(data, this, byteOrder);
    }

    /**
     * 获取未知长度
     * <p>
     * 用于长度需要动态计算的字段
     *
     * @param order 字段序号
     * @return 长度
     */
    default int onFieldUnknownLength(int order) {
        throw new UnsupportedOperationException();
    }

    /**
     * 从字节数组中解包完成
     */
    default void onFromBytesFinished() {
    }
}
