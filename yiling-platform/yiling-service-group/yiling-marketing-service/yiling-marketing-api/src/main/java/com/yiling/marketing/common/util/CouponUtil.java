package com.yiling.marketing.common.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode;
import com.yiling.marketing.couponactivity.enums.CouponActivityUseDateTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: houjie.sun
 * @date: 2021/10/28
 */
public class CouponUtil {

    public static String getLockName(String moduleName, String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append("b2b-coupon-lock-").append(moduleName).append("-").append(methodName);
        return sb.toString();
    }

    public static boolean checkPlatformEid(Long eid) {
        if (ObjectUtil.isNotNull(eid) && eid == 0) {
            return true;
        }
        return false;
    }

    public static void checkAuthorization(Long eid) {
        // 是否属于平台
        if (!CouponUtil.checkPlatformEid(eid)) {
            throw new BusinessException(CouponActivityErrorCode.BUSINESS_COUPON_AUTHORIZATION_ERROR);
        }
    }

    public static <T, R> Map<T, R> getGiveOrUseCount(List<T> idList, Function<List<T>, List<Map<T, R>>> function) {
        Map<T, R> giveCountMap = new HashMap<>();
        if (CollUtil.isEmpty(idList) || ObjectUtil.isNull(function)) {
            return giveCountMap;
        }
        List<Map<T, R>> mapList = function.apply(idList);
        if (CollUtil.isNotEmpty(mapList)) {
            mapList.forEach(g -> {
                Set<T> keys = g.keySet();
                T key = keys.stream().findFirst().orElse(null);
                giveCountMap.put(key, g.get(key));
            });
        }
        return giveCountMap;
    }

    /**
     * 获取集合中最接近的数
     * @param number    需要查找的数字
     * @param numbers   数字集合
     * @param flag   如果有两个相近的数据   true:选择大数  false:选择小数
     * @param <T>   必须为数字类型
     * @return  相近结果
     */
    public static <T extends Number> T getSimilarNumber(T number, Collection<T> numbers, Boolean flag) {
        if (null == numbers || numbers.isEmpty()) {
            throw new RuntimeException("数字集合不能为空");
        }
        if (numbers.contains(number)) {
            return number;
        }
        if (numbers instanceof List) {
            numbers = new HashSet<>(numbers);
        }
        numbers.add(number);
        List<T> numList = numbers.stream().sorted().collect(Collectors.toList());
        int size = numList.size();
        int index = numList.indexOf(number);
        if (index <= 0) {
            return numList.get(1);
        }
        if (index >= size - 1) {
            return null;
        }
        T before = numList.get(index - 1);
        T after = numList.get(index + 1);
        double beforeDifference = number.doubleValue() - before.doubleValue();
        double afterDifference = after.doubleValue() - number.doubleValue();
        if (beforeDifference == afterDifference) {
            return flag ? after : before;
        }
        return beforeDifference < afterDifference ? before : after;
    }

    /**
     * 获取当前毫秒时间
     * @return
     */
    public static String getMillisecondTime() {
        Date date = new Date();
        return DateUtil.format(date, "yyyyMMddHHmmssSSS");
    }

    public static Map<String,Date> buildCouponBeginEndTime(Date date, Date activityBeginTime, Date activityEndTime, Integer useDateType, Integer expiryDays){
        Map<String,Date> map = new HashMap<>();
        Date beginTime;
        Date endTime;
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            beginTime = activityBeginTime;
            endTime = activityEndTime;
        } else {
            beginTime = date;
            endTime = DateUtil.offsetDay(date, expiryDays);
        }
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return map;
    }

    /**
     * 获取有效时间
     * @param couponActivity
     * @param dayType true-天，false-时分秒
     * @return
     */
    public static String buildEffectiveTime(CouponActivityDetailDTO couponActivity, boolean dayType) {
        String effectiveTime = "";
        Integer useDateType = couponActivity.getUseDateType();
        if (ObjectUtil.equal(CouponActivityUseDateTypeEnum.FIXED.getCode(), useDateType)) {
            Date beginTime = couponActivity.getBeginTime();
            Date endTime = couponActivity.getEndTime();
            String format;
            if(dayType){
                format = "yyyy-MM-dd";
            } else {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            String begin = DateUtil.format(beginTime, format);
            String end = DateUtil.format(endTime, format);
            effectiveTime = begin.concat(" - ").concat(end);
        } else {
            Integer expiryDays = couponActivity.getExpiryDays();
            effectiveTime = "按发放/领取".concat(expiryDays.toString()).concat("天过期");
        }
        return effectiveTime;
    }

    /**
     * 日期相差的小时数
     * @param startTime
     * @param endTime
     * @return
     */
    public static int daysBetween2(Date startTime, Date endTime) {
        Calendar cal = Calendar.getInstance();
        long time1 = 0;
        long time2 = 0;

        try {
            cal.setTime(startTime);
            time1 = cal.getTimeInMillis();
            cal.setTime((endTime));
            time2 = cal.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between_hours = (time2 - time1) / (1000 * 3600);
        return Integer.parseInt(String.valueOf(between_hours));
    }

}
