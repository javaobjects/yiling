package com.yiling.dataflow.flow;

import java.util.Date;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * @author fucheng.bai
 * @date 2023/2/7
 */
public class Test {

    public static void main(String[] args) {
        Date date = DateUtil.offsetMonth(DateUtil.beginOfMonth(new Date()), -5);
        System.out.println(DateUtil.format(date, "yyyy-MM-dd"));
    }

    private static void checkStartAndEndDate(Date startTime, Date endTime) {
        DateTime startDate = DateUtil.beginOfDay(startTime);
        DateTime endDate = DateUtil.endOfDay(endTime);
        long offset = DateUtil.betweenDay(startDate, endDate, false);
        System.out.println(offset);
    }
}
