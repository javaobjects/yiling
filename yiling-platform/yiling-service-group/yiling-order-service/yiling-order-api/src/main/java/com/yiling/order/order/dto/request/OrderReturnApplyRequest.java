package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.ReturnSourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单审核请求数据
 *
 * @author:tingwei.chen
 * @date:2021/6/22
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnApplyRequest extends BaseRequest {

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private ReturnSourceEnum returnSourceEnum;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 交易订单号
     */
    private Long orderId;

    /**
     * 退货类型
     */
    private Integer returnType;

    /**
     * 退款订单编号
     */
    private Long orderReturnId;

    /**
     * 商品明细组装集合
     */
    private List<OrderDetailRequest> orderDetailList;
    /**
     * 备注
     */
    private String remark;

}
