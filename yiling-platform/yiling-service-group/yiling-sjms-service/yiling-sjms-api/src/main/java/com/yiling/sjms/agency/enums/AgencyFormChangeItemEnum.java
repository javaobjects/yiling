package com.yiling.sjms.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 变更项目 1-供应链角色 2-机构名称、3-社会统一信用代码、4-电话、5-所属区域、6-地址
 *
 * @author: yong.zhang
 * @date: 2023/2/28 0028
 */
@Getter
@AllArgsConstructor
public enum AgencyFormChangeItemEnum {
    /**
     * 1-供应链角色
     */
    SUPPLY_CHAIN_ROLE(1, "供应链角色"),
    /**
     * 2-机构名称
     */
    NAME(2, "机构名称"),
    /**
     * 3-社会统一信用代码
     */
    LICENSE_NUMBER(3, "社会统一信用代码"),
    /**
     * 4-电话
     */
    PHONE(4, "电话"),
    /**
     * 5-所属区域
     */
    AREA(5, "所属区域"),
    /**
     * 6-地址
     */
    ADDRESS(6, "地址"),
    ;
    private final Integer code;
    private final String name;


    public static AgencyFormChangeItemEnum getByCode(Integer code) {
        for (AgencyFormChangeItemEnum e : AgencyFormChangeItemEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}