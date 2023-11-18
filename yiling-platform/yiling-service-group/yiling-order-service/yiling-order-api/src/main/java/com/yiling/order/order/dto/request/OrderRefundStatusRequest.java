package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderRefundStatusRequest extends BaseRequest {

    /**
     * 退款单id
     */
    private Long refundId;

    /**
     * 退款状态1-待退款 2-退款中 3-退款成功 4-退款失败
     */
    private Integer refundStatus;

    /**
     * 实际退款金额
     */
    private BigDecimal realRefundAmount;

    /**
     * 操作人
     */
    private Long operateUser;

    /**
     * 操作时间
     */
    private Date operateTime;
}
