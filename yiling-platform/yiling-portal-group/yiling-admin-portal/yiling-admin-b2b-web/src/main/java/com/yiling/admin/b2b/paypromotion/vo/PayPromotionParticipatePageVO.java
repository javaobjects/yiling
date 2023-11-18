package com.yiling.admin.b2b.paypromotion.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayPromotionParticipatePageVO extends BaseVO {

    @ApiModelProperty("活动名称")
    private String name;
    /**
     * 支付促销活动id
     */
    @ApiModelProperty("支付促销活动id")
    private Long marketingPayId;

    /**
     * 买家企业ID
     */
    @ApiModelProperty("买家企业ID")
    private Long eid;

    /**
     * 买家企业名称
     */
    @ApiModelProperty("买家企业名称")
    private String ename;

    /**
     * 参与时间
     */
    @ApiModelProperty("参与时间")
    private Date participateTime;

    /**
     * 订单id
     */
    @ApiModelProperty("订单id")
    private Long orderId;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String status;

    /**
     * 优惠金额
     */
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    /**
     * 订单实付金额
     */
    @ApiModelProperty("订单实付金额")
    private BigDecimal payment;
}
