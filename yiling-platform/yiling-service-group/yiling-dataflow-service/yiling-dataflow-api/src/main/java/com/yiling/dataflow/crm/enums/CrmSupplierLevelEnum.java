package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 CrmSupplierLevelEnum
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Getter
@AllArgsConstructor
public enum CrmSupplierLevelEnum {

    FIRST_LEVEL(1,"一级经销商"),
    SECOND_LEVEL(2,"二级经销商"),
    STANDARD_FIRST_LEVEL(3,"准一级经销商"),
    CHAIN_BUSINESS(4,"连锁商业"),
    CLOUD_BUSINESS(5,"云仓商业"),
    UNRATED_BUSINESS(6,"未分级经销商"),
    ;

    private Integer code;
    private String name;

    public static CrmSupplierLevelEnum getByCode(Integer code) {
        for(CrmSupplierLevelEnum e: CrmSupplierLevelEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
