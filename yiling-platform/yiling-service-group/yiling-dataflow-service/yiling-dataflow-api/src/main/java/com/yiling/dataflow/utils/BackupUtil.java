package com.yiling.dataflow.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author: houjie.sun
 * @date: 2022/7/12
 */
public class BackupUtil {

    public static final  String FORMATE_YEAR_ONLY = "yyyy";
    public static final  String FORMATE_MONTH     = "yyyy-MM";
    public static final  String FORMATE_SECOND    = "yyyy-MM-dd HH:mm:ss";
    private static final String SUFFIX            = "wash_";

    public static String monthBackup(Integer flowDateCount) {
        Integer count = flowDateCount - 1;
        List<String> monthStrList = new ArrayList<>();
        DateTime lastMonth = DateUtil.lastMonth();
        Date startMonth = DateUtil.offset(lastMonth, DateField.MONTH, count * -1);
        return DateUtil.format(startMonth, FORMATE_MONTH);
    }

    /**
     * 生成备份表后缀
     *
     * @param year
     * @param month
     * @return
     */
    public static String generateTableSuffix(Integer year, Integer month) {
        if (year == null || year == 0) {
            return null;
        }
        if (month == null || month == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(SUFFIX);
        sb.append(year).append(String.format("%02d", month));
        return sb.toString();
    }


}
