package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 用药次数 枚举
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcMedsRemindUseTimesEnum {

    /**
     * 每天1次
     */
    everyday_one_times(1,1, "每天1次", 1),

    /**
     * 每天2次
     */
    everyday_two_times(2,1, "每天2次", 2),

    /**
     * 每天3次
     */
    everyday_three_times(3,1, "每天3次", 3),

    /**
     * 每天4次
     */
    everyday_four_times(4,1, "每天4次", 4),

    /**
     * 饭前（每天3次）
     */
    before_dinner_three_times(5, 1, "饭前（每天3次）", 3),

    /**
     * 饭后（每天3次）
     */
    after_dinner_three_times(6, 1, "饭后（每天3次）", 3),

    /**
     * 睡前（每天1次 ）
     */
    before_sleep_one_times(7, 1, "睡前（每天1次 ）", 1),

    /**
     * 每小时
     */
    every_one_hour_times(8, 2, "每小时", 1),

    /**
     * 每2小时
     */
    every_tow_hour_times(9, 2, "每2小时", 2),

    /**
     * 每4小时
     */
    every_four_hour_times(10, 2, "每4小时", 4),

    /**
     * 每8小时
     */
    every_eight_hour_times(11, 2, "每8小时", 8),

    /**
     * 每12小时
     */
    every_twelve_hour_times(12, 2, "每12小时", 12),

    ;

    /**
     * 值
     */
    private Integer code;

    /**
     * 类型 1-天、2-小时
     */
    private Integer type;

    /**
     * 名称
     */
    private String  name;

    /**
     * 时间周期
     */
    private Integer times;

    public static HmcMedsRemindUseTimesEnum getByCode(Integer code) {
        for (HmcMedsRemindUseTimesEnum e : HmcMedsRemindUseTimesEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
