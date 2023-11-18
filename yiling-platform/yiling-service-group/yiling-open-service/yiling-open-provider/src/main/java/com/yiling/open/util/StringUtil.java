package com.yiling.open.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: houjie.sun
 * @date: 2023/3/29
 */
public class StringUtil {

    public static void strTrim(JSONObject jsonObject){
        if(ObjectUtil.isNull(jsonObject)){
            return;
        }
        Set<String> keys = jsonObject.keySet();
        if(CollUtil.isEmpty(keys)){
            return;
        }
        keys.forEach(key -> {
            Object object = jsonObject.get(key);
            if(ObjectUtil.isNotNull(object) && object instanceof String){
                jsonObject.put(key, ((String) object).trim());
            }
        });
    }

    /**
     * 获取日期列表，["yyyy-MM-dd"]
     *
     * @param flowDateCount 天数，从结束日期向前推算的天数
     * @param endDate 结束日期
     * @return
     */
    public static List<String> dateList(Integer flowDateCount, Date endDate) {
        Integer count = flowDateCount - 1;
        List<String> dateStrList = new ArrayList<>();
        Date startDate = cn.hutool.core.date.DateUtil.offset(endDate, DateField.DAY_OF_MONTH, count * -1);
        List<DateTime> dateTimeList = cn.hutool.core.date.DateUtil.rangeToList(startDate, endDate, DateField.DAY_OF_MONTH);
        for (DateTime dateTime : dateTimeList) {
            String dateStr = cn.hutool.core.date.DateUtil.format(dateTime.toJdkDate(), "yyyy-MM-dd");
            dateStrList.add(dateStr);
        }
        return dateStrList;
    }

}
