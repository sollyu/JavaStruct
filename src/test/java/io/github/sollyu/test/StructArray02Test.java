package io.github.sollyu.test;

import io.github.sollyu.struct.IJavaStruct;
import io.github.sollyu.struct.JavaStruct;
import io.github.sollyu.test.utils.ByteArrayUtils;
import io.github.sollyu.test.utils.StringUtils;
import io.github.sollyu.test.beans.CommandItem01;
import io.github.sollyu.test.beans.CommandItem02;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

public class StructArray02Test implements IJavaStruct {

    @JavaStruct.Field(order = 0)
    public byte serverId = 0x10;

    @JavaStruct.Field(order = 3)
    public IJavaStruct[] data;

    @Test
    public void pack() {
        CommandItem01 odb = new CommandItem01();
        odb.resistance = 0x00;
        odb.canH = 0x06;
        odb.canL = 0x0E;

        CommandItem02 can = new CommandItem02();
        can.baudRate = 250000;
        can.canMode = 0x01;

        StructArray02Test input = new StructArray02Test();
        input.data = new IJavaStruct[]{odb, can};

        byte[] output = JavaStruct.pack(input, ByteOrder.LITTLE_ENDIAN);
        String hexString = ByteArrayUtils.byteArrayToHexString(output);
        Assertions.assertEquals("10 01 03 00 00 06 0E 03 05 00 01 90 D0 03 00", hexString);
    }

    @Test
    public void unpack() {
        String hexString = "10 01 03 00 00 06 0E 03 05 00 01 90 D0 03 00";
        byte[] bytes = StringUtils.hexStringToByteArray(hexString);

        StructArray02Test output = new StructArray02Test();
        CommandItem01 odb = new CommandItem01();
        CommandItem02 can = new CommandItem02();
        output.data = new IJavaStruct[]{odb, can};

        JavaStruct.unpack(bytes, output, ByteOrder.LITTLE_ENDIAN);
        Assertions.assertInstanceOf(CommandItem01.class, output.data[0]);
        Assertions.assertInstanceOf(CommandItem02.class, output.data[1]);
        CommandItem01 odbOutput = (CommandItem01) output.data[0];
        CommandItem02 canOutput = (CommandItem02) output.data[1];
        Assertions.assertEquals(0x10, output.serverId);
        Assertions.assertEquals(0x01, odbOutput.id);
        Assertions.assertEquals(0x03, odbOutput.length);
        Assertions.assertEquals(0x00, odbOutput.resistance);
        Assertions.assertEquals(0x06, odbOutput.canH);
        Assertions.assertEquals(0x0E, odbOutput.canL);
        Assertions.assertEquals(0x03, canOutput.id);
        Assertions.assertEquals(0x05, canOutput.length);
        Assertions.assertEquals(250000, canOutput.baudRate);
        Assertions.assertEquals(0x01, canOutput.canMode);
    }

}
