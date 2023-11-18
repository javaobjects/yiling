package com.yiling.dataflow.sale.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 指标配置类型 1-省区2-月份3-品种4区办
 *
 * @author: dexi.yao
 * @date: 2022/12/27
 */
@Getter
@AllArgsConstructor
public enum CrmSaleDepartmentSubTargetTypeEnum {

    PROVINCE(1, "省区"),
    MONTH(2, "月份"),
    GOODS(3, "品种"),
    AREA(4, "区办"),
    ;

    private Integer code;
    private String desc;

    public static CrmSaleDepartmentSubTargetTypeEnum getFromCode(Integer code) {
        for (CrmSaleDepartmentSubTargetTypeEnum e : CrmSaleDepartmentSubTargetTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static Map<Integer, String> getCodeMap() {
        Map<Integer, String> flowGoodsRelationLabelMap = Arrays.stream(CrmSaleDepartmentSubTargetTypeEnum.values())
                .collect(Collectors.toMap(o -> o.getCode(), o -> o.getDesc(), (k1, k2) -> k1));
        return flowGoodsRelationLabelMap;
    }

    public static List<Integer> getCodeList() {
        return Arrays.stream(CrmSaleDepartmentSubTargetTypeEnum.values()).map(CrmSaleDepartmentSubTargetTypeEnum::getCode).distinct().collect(Collectors.toList());
    }

}
