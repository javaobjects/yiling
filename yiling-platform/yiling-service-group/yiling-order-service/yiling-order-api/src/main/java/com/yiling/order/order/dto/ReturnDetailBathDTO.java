package com.yiling.order.order.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReturnDetailBathDTO extends BaseDTO {

    /**
     * 退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 退货数量
     */
    private Integer returnQuantity;
}
