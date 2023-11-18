package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Collectors;

/**
 * 商业等级
 */
@Getter
@AllArgsConstructor
public enum CrmSupplierLevelEnum {
    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    one_level(1,"一级经销商"),
    two_level(2,"二级经销商"),
    standard_one_level(3,"准一级经销商"),
    chain_level(4,"连锁商业"),
    cloud_level(5,"云仓商业"),
    no_level(6,"未分级经销商"),
    ;
    private final Integer code;
    private final String name;

    public static CrmSupplierLevelEnum getByCode(Integer code) {
        if(code==null){
            return null;
        }
        for (CrmSupplierLevelEnum e : CrmSupplierLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        if(code==null){
            return null;
        }
        for (CrmSupplierLevelEnum e : CrmSupplierLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}
