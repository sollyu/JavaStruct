package io.github.sollyu.test.business;

import io.github.sollyu.test.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VciCommand01Test {

    @Test
    public void test() {
        String command = "7E26000008003000000000190087000114000001000F677F1B91F3EA66051FEB22472DE734A40093CE7E";
        VciCommandResponseBase<VciCommandResponseBase.IData> response = VciCommandResponseBase.parse(StringUtils.hexStringToByteArray(command));
        System.out.println(response);

        Assertions.assertEquals(response.start, 0x7E);
        Assertions.assertEquals(response.end, 0x7E);

        VciCommandResponseBase.IData data = response.data;
        Assertions.assertEquals(data.getCounters(), 0x800);
        Assertions.assertEquals(data.getServerId(), 0x30);
        Assertions.assertEquals(data.getSize(), 0x19);
        Assertions.assertEquals(data.getData().length, 0x19);
        Assertions.assertEquals(data.getCrc(), (short)0xCE93);
    }

}
