package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货单
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细orderDetailId
     */
    private Long orderDetailId;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 买家企业id
     */
    private Long buyerEid;

    /**
     * 买家名称
     */
    private String buyerEname;

    /**
     * 卖家企业id
     */
    private Long sellerEid;

    /**
     * 卖家名称
     */
    private String sellerEname;

    /**
     * 退货单类型：1-供应商退货单 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 配送商企业ID
     */
    private Long distributorEid;

    /**
     * 配送商名称
     */
    private String distributorEname;

    /**
     * 退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 备注
     */
    private String remark;
    /**
     * 退货商品件数
     */
    private Integer returnGoodsNum;

    /**
     * 退货数量
     */
    private Integer returnQuantity;
}
