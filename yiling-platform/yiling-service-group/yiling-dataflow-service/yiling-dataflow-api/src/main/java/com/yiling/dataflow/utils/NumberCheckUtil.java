package com.yiling.dataflow.utils;

import java.util.regex.Pattern;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
public class NumberCheckUtil {

    /**
     * 校验1-8位正整数
     *
     * @param gbNumber
     * @return
     */
    public static boolean positiveInteger8BitNumber(String gbNumber) {
        String regex = "^[1-9]\\d{0,7}$";
        Pattern pattern = Pattern.compile(regex);
        boolean matches = pattern.matcher(gbNumber).matches();
        if (!matches) {
            return false;
        }
        return true;
    }
}
