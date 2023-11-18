package com.yiling.dataflow.relation.enums;

import com.yiling.dataflow.order.enums.FlowSaleReportSyncStatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/10/9
 */
@Getter
@AllArgsConstructor
public enum FlowGoodsRelationEditTaskSyncStatusEnum {

    WAIT(0, "未同步"),
    DOING(1, "同步中"),
    DONE(2, "同步成功"),
    FAIL(3, "同步失败"),
    ;

    private Integer code;
    private String desc;

    public static FlowGoodsRelationEditTaskSyncStatusEnum getFromCode(Integer code) {
        for (FlowGoodsRelationEditTaskSyncStatusEnum e : FlowGoodsRelationEditTaskSyncStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
