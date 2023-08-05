package io.github.sollyu.test.utils;

public class StringUtils {

    public static byte[] hexStringToByteArray(String hexString) {
        String realHexString = hexString.replaceAll(" ", "");
        byte[] result = new byte[realHexString.length() / 2];
        for (int i = 0; i < realHexString.length(); i += 2) {
            result[i / 2] = (byte) ((Character.digit(realHexString.charAt(i), 16) << 4) + Character.digit(realHexString.charAt(i + 1), 16));
        }
        return result;
    }

}
