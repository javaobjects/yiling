package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * erp供应链角色枚举类
 *
 * @author: houjie.sun
 * @date: 2023/2/14
 */
@Getter
@AllArgsConstructor
public enum CrmSupplyChainRoleEnum {

    DISTRIBUTOR(1, "经销商"),
    HOSPITAL(2, "终端医院"),
    PHARMACY(3, "终端药店")
    ;

    private Integer code;
    private String name;

    public static CrmSupplyChainRoleEnum getFromCode(Integer code) {
        for(CrmSupplyChainRoleEnum e: CrmSupplyChainRoleEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
