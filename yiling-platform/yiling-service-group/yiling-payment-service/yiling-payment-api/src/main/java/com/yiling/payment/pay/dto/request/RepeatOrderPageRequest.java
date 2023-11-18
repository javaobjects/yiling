package com.yiling.payment.pay.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.payment.enums.OrderPlatformEnum;

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
public class RepeatOrderPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID=-1441319445485148279L;

    /**
     * 订单号
     */
    private String appOrderNo;

    /**
     * 退款状态
     */
    private Integer refundState;

    /**
     * 支付开始时间
     */
    private Date startPayTime;
    /**
     * 支付结束时间
     */
    private Date endPayTime;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付订单交易类型
     */
    private Integer tradeType;

    /**
     * 处理状态 1:未处理 2:处理中,3:已处理)
     */
    private Integer dealState;

    /**
     * 支付订单来源
     */
    private String orderPlatform;

}
