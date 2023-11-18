package com.yiling.mall.order.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建订单预售订单Request
 *
 * @author: zhigang.guo
 * @date: 2022/10/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleOrderSubmitRequest extends BaseRequest {

    /**
     * ip地址信息
     */
    @NotBlank
    private String ipAddress;

    /**
     * 用户代理
     */
    @NotBlank
    private String userAgent;


    /**
     * 收货地址ID
     */
    @NotNull
    @Min(1)
    private Long addressId;

    /**
     * 买家企业ID
     */
    @NotNull
    @Min(1)
    private Long buyerEid;

    /**
     * 配送商企业ID
     */
    @NotNull
    @Min(1)
    private Long distributorEid;

    /**
     * 订单类型枚举
     */
    @NotNull
    private OrderTypeEnum orderTypeEnum;

    /**
     * 订单来源枚举
     */
    @NotNull
    private OrderSourceEnum orderSourceEnum;

    /**
     * 配送商支付方式：1-线下支付 2-在线支付
     */
    @NotNull
    @Min(1)
    private Integer paymentType;

    /**
     * 支付方式ID
     */
    @NotNull
    private Integer paymentMethod;

    /**
     * 买家留言
     */
    @Length(max = 200)
    private String buyerMessage;


}
