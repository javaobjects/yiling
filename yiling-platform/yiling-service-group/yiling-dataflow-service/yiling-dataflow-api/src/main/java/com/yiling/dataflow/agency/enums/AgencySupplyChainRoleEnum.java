package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 供应链角色 1-商业公司 2-医疗机构 3-零售机构
 *
 * @author: yong.zhang
 * @date: 2023/2/27 0027
 */
@Getter
@AllArgsConstructor
public enum AgencySupplyChainRoleEnum {
    /**
     * 1-商业公司
     */
    SUPPLIER(1, "商业公司"),
    /**
     * 2-医疗机构
     */
    HOSPITAL(2, "医疗机构"),
    /**
     * 3-零售机构
     */
    PHARMACY(3, "零售机构"),
    ;

    private final Integer code;
    private final String name;

    public static AgencySupplyChainRoleEnum getByCode(Integer code) {
        for (AgencySupplyChainRoleEnum e : AgencySupplyChainRoleEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
