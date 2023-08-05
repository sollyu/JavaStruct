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
