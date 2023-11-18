package com.yiling.payment.pay.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询支付重复的订单
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto.request
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRepeatOrderRequest extends BaseRequest {

    private static final long serialVersionUID=-1331319445474143279L;

    /**
     * 退款处理方式：1，未退款通过接口退款，2，已退款标记已处理
     */
    private Integer methodType;

    /**
     * 重复退款Id
     */
    private Long repeatOrderId;

}
