package com.yiling.dataflow.order.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/6/13
 */
@Getter
@AllArgsConstructor
public enum DataFlowErrorCode implements IErrorCode {

    /**
     * erp同步信息失败
     */
    DATA_FLOW_SYNC_ERROR(21001, "dataFlow同步信息失败"),
    SALE_FLOW_SYNC_ERROR(21002, "销售流线汇总失败"),
    ;

    private Integer code;
    private String message;

}
