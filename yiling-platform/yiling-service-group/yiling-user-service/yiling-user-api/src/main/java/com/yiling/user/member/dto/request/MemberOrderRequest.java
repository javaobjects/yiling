package com.yiling.user.member.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买生成订单DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberOrderRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 购买企业ID
     */
    private Long eid;

    /**
     * 购买企业名称
     */
    private String ename;

    /**
     * 购买条件ID
     */
    private Long buyStageId;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 订单金额（支付金额）
     */
    private BigDecimal payAmount;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广方名称
     */
    private String promoterName;

    /**
     * 推广人ID
     */
    private Long promoterUserId;

    /**
     * 推广方人名称
     */
    private String promoterUserName;

    /**
     * 订单状态：10-待支付 20-支付成功 30-支付失败
     */
    private Integer status;

    /**
     * 使用会员优惠券ID
     */
    private Long couponId;

}
