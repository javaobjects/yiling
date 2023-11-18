package com.yiling.order.order.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 创建预售订单request
 * @author zhigang.guo
 * @date: 2022/10/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreatePresaleOrderRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 预售活动类型 1-定金 2-全款
     */
    private Integer activityType;

    /**
     * 是否已支付定金
     */
    private Integer isPayDeposit;

    /**
     * 是否已支付尾款
     */
    private Integer isPayBalance;

    /**
     * 定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 尾款金额
     */
    private BigDecimal balanceAmount;

    /**
     * 尾款支付开始时间
     */
    private Date balanceStartTime;

    /**
     * 尾款支付结束时间
     */
    private Date balanceEndTime;

    /**
     * 是否发送支付短信
     */
    private Integer hasSendPaySms;

    /**
     * 是否已发送取消短信
     */
    private Integer hasSendCancelSms;

}
