package com.yiling.dataflow.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流向销售同步返利状态枚举类
 *
 * @author: houjie.sun
 * @date: 2022/6/30
 */
@Getter
@AllArgsConstructor
public enum FlowSaleReportSyncStatusEnum {

    TODO(0, "未同步"),
    DONE(1, "已同步"),
    ;

    private Integer code;
    private String desc;

    public static FlowSaleReportSyncStatusEnum getFromCode(Integer code) {
        for (FlowSaleReportSyncStatusEnum e : FlowSaleReportSyncStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
