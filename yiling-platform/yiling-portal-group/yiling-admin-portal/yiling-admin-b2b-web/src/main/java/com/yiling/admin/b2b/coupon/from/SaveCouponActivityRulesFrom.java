package com.yiling.admin.b2b.coupon.from;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCouponActivityRulesFrom extends BaseForm {

    /**
     * 优惠券活动id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 平台限制（1-全部平台；2-部分平台）
     */
    @ApiModelProperty(value = "平台限制（1-全部平台；2-部分平台）")
    @NotNull
    private Integer platformLimit;

    /**
     * 选择平台（1-B2B；2-销售助手）
     */
    @ApiModelProperty(value = "选择平台（1-B2B；2-销售助手）")
    private List<String> platformSelectedList;

    /**
     * 支付方式限制（1-全部支付方式；2-部分支付方式）
     */
    @ApiModelProperty(value = "支付方式限制（1-全部支付方式；2-部分支付方式）")
    @NotNull
    private Integer payMethodLimit;

    /**
     * 选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）
     */
    @ApiModelProperty(value = "选择支付方式（1-在线支付；2-货到付款；3-账期。2,3现在不支持）")
    private List<String> payMethodSelectedList;

    /**
     * 可叠加促销列表（1-满赠）
     */
    @ApiModelProperty(value = "可叠加促销列表（1-满赠）")
    private List<String> coexistPromotionList;

    /**
     * 可用供应商（1-全部供应商可用；2-部分供应商可用）
     */
    @ApiModelProperty(value = "供应商限制（1-全部供应商；2-部分供应商）")
    @NotNull
    private Integer enterpriseLimit;

    /**
     * 可用商品（1-全部商品可用；2-部分商品可用）
     */
    @NotNull
    @ApiModelProperty(value = "可用商品（1-全部商品可用；2-部分商品可用）")
    private Integer goodsLimit;

    /**
     * 生成优惠券总数量
     */
    @ApiModelProperty(value = "生成优惠券总数量")
    private Integer totalCount;

    /**
     * 状态：1-启用 2-停用 3-废弃
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用 3-废弃")
    private Integer status;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
