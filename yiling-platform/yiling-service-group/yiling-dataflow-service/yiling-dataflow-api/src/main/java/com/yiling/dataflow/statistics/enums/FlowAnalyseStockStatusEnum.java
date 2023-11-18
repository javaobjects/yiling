package com.yiling.dataflow.statistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowAnalyseStockStatusEnum {
    NO_SALE(0,"未销售"),
    NORMAL(1, "正常"),
    NERVOUS(2, "紧张"),
    AMPLE(3, "充裕");
    private Integer code;
    private String desc;

    public static FlowAnalyseStockStatusEnum getFromCode(Integer code) {
        for (FlowAnalyseStockStatusEnum e : FlowAnalyseStockStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getDescByCode(Integer code) {
        for (FlowAnalyseStockStatusEnum e : FlowAnalyseStockStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getDesc();
            }
        }
        return null;
    }
}
