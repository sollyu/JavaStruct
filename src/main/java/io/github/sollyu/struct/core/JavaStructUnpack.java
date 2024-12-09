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

package io.github.sollyu.struct.core;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import io.github.sollyu.struct.core.io.BigEndianInputStream;
import io.github.sollyu.struct.core.io.LittleEndianInputStream;
import io.github.sollyu.struct.core.util.JavaStructFieldComparator;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.Predicate;

public class JavaStructUnpack implements Runnable {

    private final DataInput dataInputStream;
    private final IJavaStruct output;

    private final SortedMap<JavaStruct.Field, Field> fieldMap = new TreeMap<>(new JavaStructFieldComparator());

    public JavaStructUnpack(byte[] data, IJavaStruct output, ByteOrder byteOrder) {
        if (output == null) {
            throw new NullPointerException("output is null");
        }
        if (data == null) {
            throw new NullPointerException("data is null");
        }
        if (byteOrder == null) {
            throw new NullPointerException("byteOrder is null");
        }
        if (data.length == 0) {
            throw new IllegalArgumentException("data is empty");
        }

        this.dataInputStream = byteOrder == ByteOrder.BIG_ENDIAN ? new BigEndianInputStream(new ByteArrayInputStream(data)) : new LittleEndianInputStream(new ByteArrayInputStream(data));
        this.output = output;
    }

    public JavaStructUnpack(DataInput dataInputStream, IJavaStruct output) {
        this.dataInputStream = dataInputStream;
        this.output = output;
    }

    private void runWithException() throws Exception {
        for (Field field : output.getClass().getFields()) {
            JavaStruct.Field annotation = field.getAnnotation(JavaStruct.Field.class);
            if (annotation == null) {
                continue;
            }
            fieldMap.put(annotation, field);
        }

        for (Map.Entry<JavaStruct.Field, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();

            Class<?> fieldType = field.getType();
            if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                field.set(output, dataInputStream.readBoolean());
            } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                field.set(output, dataInputStream.readByte());
            } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                field.set(output, dataInputStream.readShort());
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                field.set(output, dataInputStream.readInt());
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                field.set(output, dataInputStream.readLong());
            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                field.set(output, dataInputStream.readFloat());
            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                field.set(output, dataInputStream.readDouble());
            } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                field.set(output, dataInputStream.readChar());
            } else if (fieldType.isArray()) {
                handleArray(entry);
            } else if (IJavaStruct.class.isAssignableFrom(fieldType)) {
                handleStruct(entry);
            } else {
                throw new RuntimeException("Unsupported type: " + fieldType.getName());
            }
        }
    }

    @Override
    public void run() throws RuntimeException {
        try {
            runWithException();
            output.onFromBytesFinished();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleStruct(Map.Entry<JavaStruct.Field, Field> entry) throws Exception {
        Object value = entry.getValue().get(output);
        if (value == null) {
            try {
                value = entry.getValue().getType().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("please initialize the field: " + entry.getValue().getName(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            entry.getValue().set(output, value);
        }
        IJavaStruct javaStruct = (IJavaStruct) value;
        new JavaStructUnpack(dataInputStream, javaStruct).run();
    }

    private void handleArray(Map.Entry<JavaStruct.Field, Field> entry) throws Exception {
        Object value = entry.getValue().get(output);
        if (value == null) {
            Optional<Map.Entry<JavaStruct.Field, Field>> sizeof = Optional.empty();
            for (Map.Entry<JavaStruct.Field, Field> fieldFieldEntry : fieldMap.entrySet()) {
                // 检查是否匹配sizeof的名称
                boolean matchSizeofName = Objects.equals(fieldFieldEntry.getKey().sizeof(), entry.getValue().getName());

                // 检查是否有注解名称且是否匹配
                boolean matchAnnotationName = !entry.getValue().getAnnotation(JavaStruct.Field.class).name().isEmpty() &&
                        Objects.equals(fieldFieldEntry.getKey().sizeof(), entry.getValue().getAnnotation(JavaStruct.Field.class).name());

                // 如果匹配，保存匹配的entry并退出循环
                if (matchSizeofName || matchAnnotationName) {
                    sizeof = Optional.of(fieldFieldEntry);
                    break;
                }
            }

            // 如果找到了匹配的sizeof，处理数组sizeof
            if (sizeof.isPresent()) {
                handleArraySizeOf(entry, sizeof.get().getValue());
            } else {
                // 如果没有找到匹配的sizeof，处理未知长度的数组
                handleArrayLength(entry, output.onFieldUnknownLength(entry.getKey().order()));
            }
        } else {
            // 如果value不为null，处理数组长度
            handleArrayLength(entry, Array.getLength(value));
        }
    }

    private void handleArraySizeOf(Map.Entry<JavaStruct.Field, Field> entry, Field sizeof) throws Exception {
        Integer size = getOutputFieldValueToInt(sizeof);
        handleArrayLength(entry, size);
    }

    private void handleArrayLength(Map.Entry<JavaStruct.Field, Field> entry, Integer size) throws Exception {
        Class<?> fieldType = entry.getValue().getType();
        if (fieldType.equals(boolean[].class) || fieldType.equals(Boolean[].class)) {
            handleArrayLengthBoolean(entry, size, fieldType);
        } else if (fieldType.equals(byte[].class) || fieldType.equals(Byte[].class)) {
            handleArrayLengthByte(entry, size, fieldType);
        } else if (fieldType.equals(short[].class) || fieldType.equals(Short[].class)) {
            handleArrayLengthShort(entry, size, fieldType);
        } else if (fieldType.equals(int[].class) || fieldType.equals(Integer[].class)) {
            handleArrayLengthInt(entry, size, fieldType);
        } else if (fieldType.equals(long[].class) || fieldType.equals(Long[].class)) {
            handleArrayLengthLong(entry, size, fieldType);
        } else if (fieldType.equals(float[].class) || fieldType.equals(Float[].class)) {
            handleArrayLengthFloat(entry, size, fieldType);
        } else if (fieldType.equals(double[].class) || fieldType.equals(Double[].class)) {
            handleArrayLengthDouble(entry, size, fieldType);
        } else if (fieldType.equals(char[].class) || fieldType.equals(Character[].class)) {
            handleArrayLengthChar(entry, size, fieldType);
        } else if (IJavaStruct[].class.isAssignableFrom(fieldType)) {
            handleArrayLengthStruct(entry, size, fieldType);
        }
    }

    private void handleArrayLengthBoolean(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(boolean[].class)) {
            boolean[] array = new boolean[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readBoolean();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Boolean[].class)) {
            Boolean[] array = new Boolean[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readBoolean();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthByte(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(byte[].class)) {
            byte[] array = new byte[size];
            dataInputStream.readFully(array);
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Byte[].class)) {
            Byte[] array = new Byte[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readByte();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthShort(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(short[].class)) {
            short[] array = new short[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readShort();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Short[].class)) {
            Short[] array = new Short[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readShort();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthInt(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(int[].class)) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readInt();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Integer[].class)) {
            Integer[] array = new Integer[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readInt();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthLong(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(long[].class)) {
            long[] array = new long[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readLong();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Long[].class)) {
            Long[] array = new Long[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readLong();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthFloat(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(float[].class)) {
            float[] array = new float[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readFloat();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Float[].class)) {
            Float[] array = new Float[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readFloat();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthDouble(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(double[].class)) {
            double[] array = new double[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readDouble();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Double[].class)) {
            Double[] array = new Double[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readDouble();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthChar(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        if (fieldType.equals(char[].class)) {
            char[] array = new char[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readChar();
            }
            entry.getValue().set(output, array);
        } else if (fieldType.equals(Character[].class)) {
            Character[] array = new Character[size];
            for (int i = 0; i < size; i++) {
                array[i] = dataInputStream.readChar();
            }
            entry.getValue().set(output, array);
        }
    }

    private void handleArrayLengthStruct(Map.Entry<JavaStruct.Field, Field> entry, Integer size, Class<?> fieldType) throws Exception {
        Object array = entry.getValue().get(output) != null ? entry.getValue().get(output) : Array.newInstance(entry.getValue().getType().getComponentType(), size);
        Object[] arrayObject = (Object[]) array;
        for (int i = 0; i < size; i++) {
            IJavaStruct value = arrayObject[i] != null ? (IJavaStruct) arrayObject[i] : (IJavaStruct) entry.getValue().getType().getComponentType().newInstance();
            new JavaStructUnpack(dataInputStream, value).run();
            arrayObject[i] = value;
        }
        entry.getValue().set(output, array);
    }

    private Integer getOutputFieldValueToInt(Field field) throws IllegalAccessException {
        Integer value = null;
        Object sizeUnknownType = field.get(output);
        if (sizeUnknownType instanceof Integer) {
            value = (Integer) sizeUnknownType;
        } else if (sizeUnknownType instanceof Long) {
            value = ((Long) sizeUnknownType).intValue();
        } else if (sizeUnknownType instanceof Short) {
            value = ((Short) sizeUnknownType) & 0xFFFF;
        } else if (sizeUnknownType instanceof Byte) {
            value = ((Byte) sizeUnknownType) & 0xFF;
        } else {
            throw new RuntimeException("sizeof type is not support: " + sizeUnknownType.getClass().getName());
        }
        return value;
    }


}
