package com.yiling.open.erp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.yiling.open.erp.enums.FlowPurchaseOrderSourceEnum;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: houjie.sun
 * @date: 2022/3/4
 */
public class FlowUtil {

    public static final String FORMATE_MONTH = "yyyy年MM月";

    /**
     * 订单来源 类型是“大运河采购”的公司名称
     * @return
     */
    public static List<String> getDyunheEnterpriseNames(){
        return Arrays.asList(OpenConstants.YILING_ENTERPRISE_NAMES);
    }

    /**
     * 根据供应商名称获取采购流向订单来源类型
     *
     * @param enterpriseName
     * @return
     */
    public static String getFlowPurchaseOrderSourceByEnterpriseName(String enterpriseName) {
        if(StrUtil.isBlank(enterpriseName)){
            return FlowPurchaseOrderSourceEnum.OTHERS.getCode();
        }
        enterpriseName = OpenStringUtils.clearAllSpace(enterpriseName);
        if (ArrayUtil.contains(OpenConstants.YILING_ENTERPRISE_NAMES, enterpriseName)) {
            return FlowPurchaseOrderSourceEnum.POP.getCode();
        }
        return FlowPurchaseOrderSourceEnum.OTHERS.getCode();
    }

    public static List<String> monthList(Integer flowDateCount){
        Integer count = flowDateCount - 1;
        List<String> monthStrList = new ArrayList<>();
        DateTime lastMonth = DateUtil.lastMonth();
        Date startMonth = cn.hutool.core.date.DateUtil.offset(lastMonth, DateField.MONTH, count * -1);
        List<DateTime> monthTimeList = cn.hutool.core.date.DateUtil.rangeToList(startMonth, lastMonth, DateField.MONTH);
        for (DateTime monthTime : monthTimeList) {
            String monthStr = cn.hutool.core.date.DateUtil.format(monthTime.toJdkDate(), FORMATE_MONTH);
            monthStrList.add(monthStr);
        }
        return monthStrList;
    }

    public static String flowSealedMonthConvertLine(String month){
        if(StrUtil.isBlank(month)){
            return null;
        }
        return month.replace("年", "-").replace("月", "");
    }

    public static String flowSealedMonthConvertChinese(String month){
        if(StrUtil.isBlank(month)){
            return null;
        }
        return month.replace("-", "年").concat("月");
    }

    /**
     * 比较list差异
     *
     * @param a
     * @param b
     * @param <T>
     * @return false-有差异，true-相同
     */
    public static <T extends Comparable<T>> boolean compareDiffence(List<T> a, List<T> b) {
        if(a.size() != b.size()){
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        for(int i=0;i<a.size();i++){
            if(!a.get(i).equals(b.get(i))){
                return false;
            }
        }
        return true;
    }

    public static <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> subList = new ArrayList<>();
        if (list.size() > size) {
            subList = Lists.partition(list, size);
        } else {
            subList.add(list);
        }
        return subList;
    }

}
