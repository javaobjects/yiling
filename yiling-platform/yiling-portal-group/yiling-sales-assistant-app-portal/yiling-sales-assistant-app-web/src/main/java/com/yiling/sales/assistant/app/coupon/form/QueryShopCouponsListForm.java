package com.yiling.sales.assistant.app.coupon.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * B2B-店铺优惠券分页列表 Form
 *
 * @author houjie.sun
 * @date 2022/04/06
 */
@Data
@ApiModel
public class QueryShopCouponsListForm extends BaseForm {

    /**
     * 选择的代下单客户企业ID
     */
    @NotNull
    @ApiModelProperty("选择的代下单客户企业ID")
    private Long customerEid;

    /**
     * 店铺企业ID
     */
    @NotNull
    @ApiModelProperty("店铺企业ID")
    private Long shopEid;

}
