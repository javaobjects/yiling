package com.yiling.open.erp.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/6/1
 */
@Getter
@AllArgsConstructor
public enum FlowPurchaseInventoryEnterpriseEnum {

    DA_YUN_HE(1,"河北大运河医药物流有限公司"),
    JING_DONG(2,"泰州市京东医药有限责任公司"),
    ;

    private Integer code;
    private String name;

    public static FlowPurchaseInventoryEnterpriseEnum getByCode(String code) {
        for(FlowPurchaseInventoryEnterpriseEnum e: FlowPurchaseInventoryEnterpriseEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static FlowPurchaseInventoryEnterpriseEnum getByName(String name) {
        for(FlowPurchaseInventoryEnterpriseEnum e: FlowPurchaseInventoryEnterpriseEnum.values()) {
            if(e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static List<String> getEnterpriseNameList(){
        return Arrays.stream(FlowPurchaseInventoryEnterpriseEnum.values()).map(FlowPurchaseInventoryEnterpriseEnum::getName).distinct().collect(Collectors.toList());
    }
}
