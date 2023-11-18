package com.yiling.dataflow.utils;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: shuang.zhang
 * @date: 2022/7/14
 */
public class DateTransUtil {

    public static DateTime parseDate(CharSequence dateString) {
       if(StrUtil.isBlank(dateString)){
           return null;
       }
       try {
           return DateUtil.parseDate(dateString);
       }catch (DateException e){}
       return null;
    }
}
