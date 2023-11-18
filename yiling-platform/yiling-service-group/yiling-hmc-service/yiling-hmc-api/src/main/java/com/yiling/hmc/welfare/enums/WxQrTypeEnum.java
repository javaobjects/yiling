package com.yiling.hmc.welfare.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信二维码类型
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@Getter
@AllArgsConstructor
public enum WxQrTypeEnum {

    /**
     * 通心络福利二维码
     */
    TXL_WELFARE(1, "通心络福利二维码"),

    /**
     * 药品福利员工二维码
     */
    STAFF_WELFARE(10, "药品福利员工二维码"),

    /**
     * 抽奖活动二维码
     */
    C_LOTTERY_ACTIVITY(20, "抽奖活动二维码"),

    /**
     * 健康测评二维码
     */
    C_HEALTH_EVALUATE(30, "健康测评二维码"),

    /**
     * 医带患二维码
     */
    C_ACT_DOC_PATIENT(40, "医带患二维码"),


    ;

    private final Integer code;

    private final String name;

    public static WxQrTypeEnum getByCode(Integer code) {
        for (WxQrTypeEnum e : WxQrTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
