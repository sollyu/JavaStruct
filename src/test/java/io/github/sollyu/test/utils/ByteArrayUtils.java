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

package io.github.sollyu.test.utils;

public class ByteArrayUtils {

    public static String byteArrayToHexString(byte[] bytes) {
        return byteArrayToHexString(bytes, " ");
    }

    public static String byteArrayToHexString(byte[] bytes, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append(String.format("%02X", bytes[i]));
            if (i != bytes.length - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

}
