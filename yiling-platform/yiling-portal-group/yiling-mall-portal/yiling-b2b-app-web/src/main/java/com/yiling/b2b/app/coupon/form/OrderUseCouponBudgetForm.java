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
 * @date: 2021/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUseCouponBudgetForm extends BaseForm {

    /**
     * 平台优惠券ID
     */
    @NotNull
    @ApiModelProperty(required = true, value = "平台优惠券ID")
    private Long platformCouponId;

    /**
     * 企业信息明细
     */
    @NotNull
    @ApiModelProperty(required = true, value = "企业信息明细")
    private List<OrderUseCouponBudgetEnterpriseForm> enterpriseDetailList;

}
