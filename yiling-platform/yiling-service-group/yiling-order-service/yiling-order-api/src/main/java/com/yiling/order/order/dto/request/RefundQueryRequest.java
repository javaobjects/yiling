package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据条件查询退款单信息-请求参数
 *
 * @author: yong.zhang
 * @date: 2021/12/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundQueryRequest extends BaseRequest {


    /**
     * 退款单号List
     */
    private List<Long> refundIdList;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 退货单ID
     */
    private Long returnId;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 退款类型1-订单取消退款 2-采购退货退款 3-商家驳回 4-会员退款
     */
    private Integer refundType;
    /**
     * 退款来源：1-正常订单，2-会员订单
     * @see com.yiling.order.order.enums.RefundSourceEnum
     */
    private Integer refundSource;

    /**
     * 退款状态1-待退款 2-退款中 3-退款成功
     */
    private Integer refundStatus;
}
