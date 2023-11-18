package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDetailErpDTO extends BaseDO {
    private static final long serialVersionUID = 1L;

    /**
     * 退货单ID
     */
    private Long returnId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 有效期
     */
    private Date expiryDate;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * ERP出库单号
     */
    private String erpDeliveryNo;

    /**
     * ERP出库录入id
     */
    private String erpSendOrderId;

    /**
     * 备注
     */
    private String remark;
}
