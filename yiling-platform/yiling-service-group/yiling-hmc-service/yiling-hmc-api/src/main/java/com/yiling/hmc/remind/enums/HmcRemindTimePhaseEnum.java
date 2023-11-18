package com.yiling.hmc.remind.enums;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 提醒时间段 枚举
 *
 * @Author fan.shen
 * @Date 2022/6/10
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcRemindTimePhaseEnum {

    /**
     * 凌晨1时至5时
     */
    time_0105(1, 1, "凌晨1时至5时", "01:00", "05:00"),

    /**
     * 早上5时至9时
     */
    time_0509(2, 2, "早上5时至9时", "05:00", "09:00"),

    /**
     * 上午9时至11时
     */
    time_0911(3, 3, "上午9时至11时", "09:00", "11:00"),

    /**
     * 中午11时至13时
     */

    time_1113(4, 4, "中午11时至13时", "11:00", "13:00"),

    /**
     * 下午13时至19时
     */
    time_1319(5, 5, "下午13时至19时", "13:00", "19:00"),

    /**
     * 晚上19时至1时
     */
    time_1924(6, 6, "晚上19时至1时", "19:00", "23:59"),
    /**
     * 晚上24时至1时
     */
    time_2401(7, 6, "晚上19时至1时", "00:00", "01:00"),

    ;

    /**
     * 类型
     */
    private Integer id;
    /**
     * 类型
     */
    private Integer flag;

    /**
     * 名称
     */
    private String name;

    /**
     * 名称
     */
    private String start;

    /**
     * 名称
     */
    private String end;


    public static HmcRemindTimePhaseEnum getByCode(Integer flag) {
        for (HmcRemindTimePhaseEnum e : HmcRemindTimePhaseEnum.values()) {
            if (e.getFlag().equals(flag)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 根据传入时间获取所属时间区间flag
     *
     * @param time
     * @return
     */
    public static HmcRemindTimePhaseEnum getFlagByTime(String time) {
        if (StrUtil.isBlank(time)) {
            return null;
        }
        if (StrUtil.equalsIgnoreCase(time, "00:00")) {
            return time_2401;
        }
        DateTime parseTime = DateUtil.parse(time, "HH:mm");
        for (HmcRemindTimePhaseEnum e : HmcRemindTimePhaseEnum.values()) {
            DateTime start = DateUtil.parse(e.getStart(), "HH:mm");
            DateTime end = DateUtil.parse(e.getEnd(), "HH:mm");
            if (start.compareTo(parseTime) < 0 && parseTime.compareTo(end) <= 0) {
                return e;
            }
        }
        return null;
    }

}
