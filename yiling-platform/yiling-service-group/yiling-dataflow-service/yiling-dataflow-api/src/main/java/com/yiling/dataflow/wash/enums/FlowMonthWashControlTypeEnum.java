package com.yiling.dataflow.wash.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/12/12
 */
@Getter
@AllArgsConstructor
public enum FlowMonthWashControlTypeEnum {
    GOODS_MAPPING(1,"流向上传（包含产品对照）"),
    CUSTOMER_MAPPING(2,"客户对照、销量申诉"),
    GOODS_BATCH(3,"在途订单、终端库存上报"),
    FLOW_CROSS(4,"窜货提报"),
    ;

    private Integer code;
    private String desc;

    public static FlowMonthWashControlTypeEnum getByCode(Integer code) {
        for(FlowMonthWashControlTypeEnum e: FlowMonthWashControlTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
