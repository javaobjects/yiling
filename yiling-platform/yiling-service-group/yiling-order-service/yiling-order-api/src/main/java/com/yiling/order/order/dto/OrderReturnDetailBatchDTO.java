package com.yiling.order.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货明细批次表:用来区分退货商品对应发货的批次
 *
 * @author: yong.zhang
 * @date: 2021/8/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDetailBatchDTO extends BaseDO {
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

//    /**
//     * 收货数量
//     */
//    private Integer receiveQuantity;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * ERP出库单号
     */
//    private String erpDeliveryNo;

    /**
     * 备注
     */
    private String remark;
}
