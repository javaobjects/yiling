package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议返利时段类型设置枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementTimeSegmentTypeSetEnum {

    /**
     * 协议返利时段类型设置
     */
    ALL_TIME(1, "全时段统一配置"),
    MUCH_TIME(2, "多时段分别配置"),
    GROUP_TIME(3, "全时段+多时段组合配置"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementTimeSegmentTypeSetEnum getByCode(Integer code) {
        for (AgreementTimeSegmentTypeSetEnum e : AgreementTimeSegmentTypeSetEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
