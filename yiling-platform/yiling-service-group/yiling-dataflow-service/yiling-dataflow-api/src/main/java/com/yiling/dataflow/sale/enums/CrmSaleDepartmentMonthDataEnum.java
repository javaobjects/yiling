package com.yiling.dataflow.sale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门配置月份基础数据
 */
@Getter
@AllArgsConstructor
public enum CrmSaleDepartmentMonthDataEnum  {
    JAN(1L,"1月"),
    FEB(2L,"2月"),
    MAR(3L,"3月"),
    APR(4L,"4月"),
    MAY(5L,"5月"),
    JUN(6L,"6月"),
    JUL(7L,"7月"),
    AUG(8L,"8月"),
    SEP(9L,"9月"),
    OCT(10L,"10月"),
    NOV(11L,"11月"),
    DEC(12L,"12月"),
    ;
    private Long code;
    private String desc;

    public static CrmSaleDepartmentMonthDataEnum getFromCode(Integer code) {
        for (CrmSaleDepartmentMonthDataEnum e : CrmSaleDepartmentMonthDataEnum.values()) {
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
