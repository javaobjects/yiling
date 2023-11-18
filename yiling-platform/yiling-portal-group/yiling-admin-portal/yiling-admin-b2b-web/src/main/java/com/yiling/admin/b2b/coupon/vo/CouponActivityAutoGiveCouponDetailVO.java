package com.yiling.admin.b2b.coupon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/22
 */
@Data
@Accessors(chain = true)
public class CouponActivityAutoGiveCouponDetailVO implements java.io.Serializable{

    private static final long serialVersionUID = 5417077146902105972L;

    /**
     * 优惠券活动ID
     */
    @ApiModelProperty(value = "优惠券活动ID", position = 1)
    private Long couponActivityId;

    /**
     * 自动发券数量
     */
    @ApiModelProperty(value = "自动发券数量", position = 2)
    private Integer giveNum;

    /**
     * 供应商限制（1-全部供应商；2-部分供应商）
     */
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    private Integer enterpriseLimit;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String name;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型1商家券，2会员券")
    private Integer memberType;

    /**
     * 剩余数量
     */
    @ApiModelProperty(value = "剩余数量")
    private Integer surplusCount;
}
