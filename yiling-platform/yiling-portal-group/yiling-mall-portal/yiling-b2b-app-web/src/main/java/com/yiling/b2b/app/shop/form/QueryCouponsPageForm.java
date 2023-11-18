package com.yiling.b2b.app.shop.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺优惠券分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class QueryCouponsPageForm extends QueryPageListForm {

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty("企业ID")
    private Long shopEid;

}
