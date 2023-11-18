package com.yiling.dataflow.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存流向统计库存状态枚举类
 *
 * @author: houjie.sun
 * @date: 2022/6/22
 */
@Getter
@AllArgsConstructor
public enum FlowGoodsBatchStatisticsStatusEnum {

    TODO(0,"未统计库存总数"),
    DONE(1,"已统计库存总数"),
    ;

    private Integer code;
    private String desc;

    public static FlowGoodsBatchStatisticsStatusEnum getFromCode(Integer code) {
        for(FlowGoodsBatchStatisticsStatusEnum e: FlowGoodsBatchStatisticsStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
