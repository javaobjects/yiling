package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 普药协议类型 协议普药一级/协议普药二级/非协议普药一级/非协议普药二级/不签协议
 */
@Getter
@AllArgsConstructor
public enum CrmGeneralMedicineLevelEnum {
    /**
     * 协议普药一级
     */
    PROTOCOL_FIRST(1, "协议普药一级"),
    /**
     * 协议普药二级
     */
    PROTOCOL_TWO(2, "协议普药二级"),
    /**
     * 非协议普药一级
     */
    NON_PROTOCOL_FIRST(3, "非协议普药一级"),
    /**
     * 非协议普药二级
     */
    NON_PROTOCOL_TWO (4, "非协议普药二级"),
    /**
     * 不签协议
     */
    NO_AGREEMENT(5, "不签协议"),
    ;

    private final Integer code;
    private final String name;

    public static CrmGeneralMedicineLevelEnum getByCode(Integer code) {
        for (CrmGeneralMedicineLevelEnum e : CrmGeneralMedicineLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmGeneralMedicineLevelEnum e : CrmGeneralMedicineLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}
