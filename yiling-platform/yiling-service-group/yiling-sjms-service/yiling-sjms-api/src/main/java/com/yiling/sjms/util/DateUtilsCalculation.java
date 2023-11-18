package com.yiling.sjms.util;

import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 时间工具类
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Slf4j
public class DateUtilsCalculation {

    public static boolean isRange(String inputTimeStr){
        try {
            // 输入日期
            Date soTime = DateUtil.parse(inputTimeStr, "yyyy-MM-dd");
            Calendar inputTime = Calendar.getInstance();
            inputTime.setTime(soTime);

            // 当前日期 即区间范围的结束时间
            Date dNow = new Date();   //当前时间
            // 获取当前日期所在月份的第一天
            Date endOfMonth = DateUtil.parse(DateUtil.format(DateUtil.endOfMonth(dNow), "yyyy-MM-dd"));

            // 前6个月的日期 即区间范围的开始时间
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(endOfMonth);//把当前时间赋给日历
            calendar.add(Calendar.MONTH, -7);  //设置为前7个月
            Date dBefore = new Date();
            dBefore = calendar.getTime();   //得到前6个月的时间
            Calendar dBeforeTime = Calendar.getInstance();
            dBeforeTime.setTime(dBefore);

            // 获取当前系统时间日期第一天
            Date beginOfMonth = DateUtil.parse(DateUtil.format(DateUtil.beginOfMonth(dNow), "yyyy-MM-dd"));
            calendar.setTime(beginOfMonth);//把当前时间赋给日历
            Date endDate = new Date();
            endDate = calendar.getTime();   //得到当前日期第一天数据
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(endDate);

            if(inputTime.after(dBeforeTime) && inputTime.before(endTime)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            log.error("解析销售日期错误", e);
            return false;
        }
    }

    public static boolean isRange(Date soTime){
        try {
            // 输入日期
            Calendar inputTime = Calendar.getInstance();
            inputTime.setTime(soTime);

            // 当前日期 即区间范围的结束时间
            Date dNow = new Date();   //当前时间
            // 获取当前日期所在月份的第一天
            Date endOfMonth = DateUtil.parse(DateUtil.format(DateUtil.endOfMonth(dNow), "yyyy-MM-dd"));

            // 前6个月的日期 即区间范围的开始时间
            Calendar calendar = Calendar.getInstance(); //得到日历
            calendar.setTime(endOfMonth);//把当前时间赋给日历
            calendar.add(Calendar.MONTH, -7);  //设置为前7个月
            Date dBefore = new Date();
            dBefore = calendar.getTime();   //得到前6个月的时间
            Calendar dBeforeTime = Calendar.getInstance();
            dBeforeTime.setTime(dBefore);

            // 获取当前系统时间日期第一天
            Date beginOfMonth = DateUtil.parse(DateUtil.format(DateUtil.beginOfMonth(dNow), "yyyy-MM-dd"));
            calendar.setTime(beginOfMonth);//把当前时间赋给日历
            Date endDate = new Date();
            endDate = calendar.getTime();   //得到当前日期第一天数据
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(endDate);

            if(inputTime.after(dBeforeTime) && inputTime.before(endTime)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            log.error("解析销售日期错误", e);
            return false;
        }
    }

    public static void main(String[] args) {
        // 输入日期
        System.out.println(isRange("2022-12-30 00:00:00"));
        System.out.println(isRange(new Date()));

//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
//        String defaultStartDate = sdf.format(dBefore);    //格式化前3月的时间
//        String defaultEndDate = sdf.format(endDate); //格式化当前时间
//        System.out.println("六个月之前时间======="+defaultStartDate);
//        System.out.println("当前时间==========="+defaultEndDate);
    }
}
