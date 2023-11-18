package com.yiling.hmc.pharmacy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 终端药店状态：1-启用，2-停用
 * @Author fan.shen
 * @Date 2022/6/1
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcPharmacyStatusEnum {

    /**
     * 启用
     */
    ENABLED(1,"启用"),

    /**
     * 停用
     */
    NOT_ENABLED(2,"停用"),

    ;

    /**
     * 类型
     */
    private Integer type;


    /**
     * 名称
     */
    private String  name;


    public static HmcPharmacyStatusEnum getByCode(Integer code) {
        for (HmcPharmacyStatusEnum e : HmcPharmacyStatusEnum.values()) {
            if (e.getType().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
