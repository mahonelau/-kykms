package org.jeecg.modules.KM.common.rules;

import org.apache.commons.lang.math.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SerialNumberRule {
    static public String generate() {
        String prefix = "Z";

        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        int random = RandomUtils.nextInt(90) + 10;
        String value = prefix + format.format(new Date()) + random;
        return value;
    }

}
