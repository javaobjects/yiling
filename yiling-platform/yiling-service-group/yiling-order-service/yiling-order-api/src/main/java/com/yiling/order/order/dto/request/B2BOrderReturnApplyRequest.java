package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请退货请求参数
 *
 * @author: yong.zhang
 * @date: 2021/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2BOrderReturnApplyRequest extends BaseRequest {

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer                                returnSource;

    /**
     * 交易订单号
     */
    private Long                                   orderId;

    /**
     * 订单号
     */
    private String                                 orderNo;

    /**
     * 申请退货单明细
     */
    private List<B2BOrderReturnDetailApplyRequest> orderReturnDetailList;

    /**
     * 备注
     */
    private String                                 remark;
}
