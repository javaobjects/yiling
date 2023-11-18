package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2023/3/6
 */
@Getter
@AllArgsConstructor
public enum FlowClassifyEnum {
    //1-正常 2-销量申诉 3-窜货申报
    NORMAL(1, "正常", ""),
    SALE_APPEAL(2, "销量申诉", "A"),
    FLOW_CROSS(3, "窜货申报", "C"),
    FLOW_GB(4, "团购处理", "T"),
    SUPPLY_TRANS_MONTH_FLOW(5, "补传月流向", ""),
    HOSPITAL_DRUGSTORE(6, "院外药店", "H"),
    ;

    private Integer code;
    private String  desc;
    private String prefix;

    public static FlowClassifyEnum getByCode(Integer code) {
        for(FlowClassifyEnum e: FlowClassifyEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
