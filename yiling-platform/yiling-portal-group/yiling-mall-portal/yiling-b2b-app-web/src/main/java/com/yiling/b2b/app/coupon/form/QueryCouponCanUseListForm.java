package com.yiling.b2b.app.coupon.form;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCouponCanUseListForm extends BaseForm {

    /**
     * 平台标识（1-POP；2-B2B；3-销售助手；4-互联网医院；5-数据中台）
     * CouponPlatformTypeEnum
     */
    @ApiModelProperty(required = true, value = "平台标识（1-POP；2-B2B；3-销售助手；4-互联网医院；5-数据中台）")
    @NotNull
    private Integer platform;

    /**
     * 优惠券类型（1-平台券；2-商家券）
     * CouponActivitySponsorTypeEnum
     */
    @ApiModelProperty(required = true, value = "优惠券类型（1-平台券；2-商家券）")
    @NotNull
    private Integer sponsorType;

    /**
     * 企业ID
     */
    @ApiModelProperty(required = true, value = "企业ID")
    @NotNull
    private Long eid;

    /**
     * 支付方式（1-线下支付；2-账期；3-预付款；4-在线支付）
     * CouponPayMethodTypeEnum
     */
    @ApiModelProperty(required = true, value = "支付方式（1-线下支付；2-账期；3-预付款；4-在线支付）")
    @NotNull
    private Integer paymentMethod;

    /**
     * 商品明细
     */
    @ApiModelProperty(required = true, value = "商品明细")
    @NotNull
    private List<QueryCouponCanUseListDetailForm> goodsDetailList;


}
