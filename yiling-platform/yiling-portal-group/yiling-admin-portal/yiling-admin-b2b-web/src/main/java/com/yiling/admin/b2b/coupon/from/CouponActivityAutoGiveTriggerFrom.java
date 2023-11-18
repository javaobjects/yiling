package com.yiling.admin.b2b.coupon.from;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class CouponActivityAutoGiveTriggerFrom extends BaseForm {

    /**
     * 企业id
     */
    @ApiModelProperty("企业id")
    private Long eid;

    /**
     * 商品ID
     */
    @ApiModelProperty("id")
    private Long goodsId;

    /**
     * 支付方式（1-在线支付；2-货到付款；3-账期） CouponPayMethodTypeEnum
     */
    @ApiModelProperty("支付方式（1-线下支付 2-账期 3-预付款 4-在线支付）")
    private Integer paymentMethod;

    /**
     * 订单状态（1-已发货） CouponOrderStatusEnum
     */
    @ApiModelProperty("订单状态（1-已发货）")
    private Integer orderStatus;

    /**
     * 下单平台（1-B2B；2-销售助手） CouponPlatformTypeEnum
     */
    @ApiModelProperty("下单平台（1-B2B；2-销售助手）")
    private Integer platformType;

    /**
     * 企业类型（数据字典：EnterpriseTypeEnum）
     */
    @ApiModelProperty("企业类型（数据字典：EnterpriseTypeEnum）")
    private Integer enterpriseType;



}
