package com.yiling.hmc.remind.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 用药管理 - 用药天数 枚举
 * @Author fan.shen
 * @Date 2022/5/31
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcMedsRemindUseDaysEnum {

    /**
     * 1天
     */
    ONE_DAY(1,"1天", 1),

    /**
     * 7天
     */
    SEVEN_DAY(2,"7天", 7),

    /**
     * 14天
     */
    FOURTEEN_DAY(3,"14天", 14),

    /**
     * 1个月
     */
    ONE_MONTH(4,"1个月", 30),

    /**
     * 2个月
     */
    TWO_MONTH(5,"2个月", 60),

    /**
     * 3个月
     */
    THREE_MONTH(6,"3个月", 90),

    /**
     * 半年
     */
    HALF_YEAR(7,"半年", 180),

    /**
     * 1年
     */
    ONE_YEAR(8,"1年", 365),

    ;

    /**
     * 值
     */
    private Integer code;

    /**
     * 名称
     */
    private String  name;

    /**
     * 时间周期
     */
    private Integer times;

    public static HmcMedsRemindUseDaysEnum getByCode(Integer code) {
        for (HmcMedsRemindUseDaysEnum e : HmcMedsRemindUseDaysEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
