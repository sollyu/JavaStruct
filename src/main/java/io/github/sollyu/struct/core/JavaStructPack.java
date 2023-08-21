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
import io.github.sollyu.struct.core.io.BigEndianOutputStream;
import io.github.sollyu.struct.core.io.LittleEndianOutputStream;
import io.github.sollyu.struct.core.util.JavaStructFieldComparator;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.Supplier;

public class JavaStructPack implements Supplier<byte[]> {

    private final IJavaStruct input;
    private final DataOutput outputStream;
    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    private final SortedMap<JavaStruct.Field, Field> fieldMap = new TreeMap<>(new JavaStructFieldComparator());

    public JavaStructPack(IJavaStruct input, ByteOrder byteOrder) {
        this.input = input;
        this.outputStream = byteOrder == ByteOrder.BIG_ENDIAN ? new BigEndianOutputStream(byteArrayOutputStream) : new LittleEndianOutputStream(byteArrayOutputStream);
    }

    public JavaStructPack(DataOutput dataOutput, IJavaStruct input) {
        this.input = input;
        this.outputStream = dataOutput;
    }

    public byte[] getWithException() throws Exception {
        for (Field field : input.getClass().getFields()) {
            JavaStruct.Field annotation = field.getAnnotation(JavaStruct.Field.class);
            if (annotation == null) {
                continue;
            }
            fieldMap.put(annotation, field);
        }

        for (Map.Entry<JavaStruct.Field, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();

            Class<?> fieldType = field.getType();
            handleSizeOf(entry, fieldType);

            if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                outputStream.writeBoolean(field.getBoolean(input));
            } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                outputStream.writeByte(field.getByte(input));
            } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                outputStream.writeShort(field.getShort(input));
            } else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
                outputStream.writeChar(field.getChar(input));
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                outputStream.writeInt(field.getInt(input));
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                outputStream.writeLong(field.getLong(input));
            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                outputStream.writeFloat(field.getFloat(input));
            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                outputStream.writeDouble(field.getDouble(input));
            } else if (fieldType.equals(String.class)) {
                outputStream.writeUTF((String) field.get(input));
            } else if (IJavaStruct.class.isAssignableFrom(fieldType)) {
                new JavaStructPack(outputStream, (IJavaStruct) field.get(input)).getWithException();
            } else if (fieldType.isArray()) {
                handleArray(entry);
            }

        }


        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] get() {
        try {
            return getWithException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleSizeOf(Map.Entry<JavaStruct.Field, Field> entry, Class<?> fieldType) throws Exception {
        String sizeOf = entry.getKey().sizeof();
        if (sizeOf.isEmpty()) {
            return;
        }
        Optional<Map.Entry<JavaStruct.Field, Field>> arrayEntryOptional = fieldMap.entrySet()
                .stream()
                .filter(e -> Objects.equals(e.getValue().getName(), sizeOf))
                .findFirst();
        if (!arrayEntryOptional.isPresent()) {
            throw new Exception("Can't find field " + sizeOf);
        }
        Map.Entry<JavaStruct.Field, Field> arrayEntry = arrayEntryOptional.get();

        Integer arraySize = 0;
        Class<?> arrayFieldType = arrayEntry.getValue().getType();
        if (arrayFieldType.equals(boolean[].class)) {
            boolean[] value = (boolean[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(byte[].class)) {
            byte[] value = (byte[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(short[].class)) {
            short[] value = (short[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(char[].class)) {
            char[] value = (char[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(int[].class)) {
            int[] value = (int[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(long[].class)) {
            long[] value = (long[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(float[].class)) {
            float[] value = (float[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (arrayFieldType.equals(double[].class)) {
            double[] value = (double[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else if (IJavaStruct[].class.isAssignableFrom(arrayFieldType)) {
            IJavaStruct[] value = (IJavaStruct[]) arrayEntry.getValue().get(input);
            arraySize = value == null ? 0 : value.length;
        } else {
            throw new Exception("Can't get size of field " + arrayEntry.getValue().getName());
        }

        if (fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
            entry.getValue().set(input, arraySize.byteValue());
        } else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
            entry.getValue().set(input, arraySize.shortValue());
        } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            entry.getValue().set(input, arraySize);
        } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            entry.getValue().set(input, arraySize.longValue());
        } else {
            throw new Exception("Can't set field " + entry.getValue().getName() + " with size of " + arrayEntry.getValue().getName());
        }
    }

    private void handleArray(Map.Entry<JavaStruct.Field, Field> entry) throws Exception {
        Object value = entry.getValue().get(input);
        if (value == null) {
            return;
        }
        Class<?> fieldType = entry.getValue().getType();
        if (fieldType.equals(boolean[].class) || fieldType.equals(Boolean[].class)) {
            handleArrayLengthBoolean(value, fieldType);
        } else if (fieldType.equals(byte[].class) || fieldType.equals(Byte[].class)) {
            handleArrayLengthByte(value, fieldType);
        } else if (fieldType.equals(short[].class) || fieldType.equals(Short[].class)) {
            handleArrayLengthShort(value, fieldType);
        } else if (fieldType.equals(int[].class) || fieldType.equals(Integer[].class)) {
            handleArrayLengthInt(value, fieldType);
        } else if (fieldType.equals(long[].class) || fieldType.equals(Long[].class)) {
            handleArrayLengthLong(value, fieldType);
        } else if (fieldType.equals(float[].class) || fieldType.equals(Float[].class)) {
            handleArrayLengthFloat(value, fieldType);
        } else if (fieldType.equals(double[].class) || fieldType.equals(Double[].class)) {
            handleArrayLengthDouble(value, fieldType);
        } else if (fieldType.equals(char[].class) || fieldType.equals(Character[].class)) {
            handleArrayLengthChar(value, fieldType);
        } else if (IJavaStruct[].class.isAssignableFrom(fieldType)) {
            handleArrayLengthStruct(value);
        }
    }

    private void handleArrayLengthBoolean(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(boolean[].class))
            for (boolean o : (boolean[]) value) outputStream.writeBoolean(o);
        else if (fieldType.equals(Boolean[].class))
            for (Boolean o : (Boolean[]) value) outputStream.writeBoolean(o);
    }

    private void handleArrayLengthByte(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(byte[].class))
            for (byte o : (byte[]) value) outputStream.writeByte(o);
        else if (fieldType.equals(Byte[].class))
            for (Byte o : (Byte[]) value) outputStream.writeByte(o);
    }

    private void handleArrayLengthShort(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(short[].class))
            for (short o : (short[]) value) outputStream.writeShort(o);
        else if (fieldType.equals(Short[].class))
            for (Short o : (Short[]) value) outputStream.writeShort(o);
    }

    private void handleArrayLengthInt(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(int[].class))
            for (int o : (int[]) value) outputStream.writeInt(o);
        else if (fieldType.equals(Integer[].class))
            for (Integer o : (Integer[]) value) outputStream.writeInt(o);
    }

    private void handleArrayLengthLong(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(long[].class))
            for (long o : (long[]) value) outputStream.writeLong(o);
        else if (fieldType.equals(Long[].class))
            for (Long o : (Long[]) value) outputStream.writeLong(o);
    }

    private void handleArrayLengthFloat(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(float[].class))
            for (float o : (float[]) value) outputStream.writeFloat(o);
        else if (fieldType.equals(Float[].class))
            for (Float o : (Float[]) value) outputStream.writeFloat(o);
    }

    private void handleArrayLengthDouble(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(double[].class))
            for (double o : (double[]) value) outputStream.writeDouble(o);
        else if (fieldType.equals(Double[].class))
            for (Double o : (Double[]) value) outputStream.writeDouble(o);
    }

    private void handleArrayLengthChar(Object value, Class<?> fieldType) throws Exception {
        if (fieldType.equals(char[].class))
            for (char o : (char[]) value) outputStream.writeChar(o);
        else if (fieldType.equals(Character[].class))
            for (Character o : (Character[]) value) outputStream.writeChar(o);
    }

    private void handleArrayLengthStruct(Object value) throws Exception {
        for (IJavaStruct o : (IJavaStruct[]) value)
            new JavaStructPack(outputStream, o).getWithException();
    }

}
