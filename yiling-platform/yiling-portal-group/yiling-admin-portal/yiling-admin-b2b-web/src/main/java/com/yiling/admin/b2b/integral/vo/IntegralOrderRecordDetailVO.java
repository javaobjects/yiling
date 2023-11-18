package com.yiling.admin.b2b.integral.vo;

import java.math.BigDecimal;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 积分发放记录订单明细 VO
 * </p>
 *
 * @author lun.yu
 * @date 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralOrderRecordDetailVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNo;

    /**
     * 规则ID
     */
    @ApiModelProperty("规则ID")
    private Long ruleId;

    /**
     * 规则名称
     */
    @ApiModelProperty("规则名称")
    private String ruleName;

    /**
     * 商品ID
     */
    @ApiModelProperty("商品ID")
    private Long goodsId;

    /**
     * 支付金额
     */
    @ApiModelProperty("支付金额")
    private BigDecimal orderAmount;

    /**
     * 发放积分
     */
    @ApiModelProperty("发放积分")
    private Integer integralValue;

}
