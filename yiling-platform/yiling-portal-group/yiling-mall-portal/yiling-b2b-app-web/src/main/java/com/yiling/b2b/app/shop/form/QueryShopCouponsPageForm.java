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
 * @date 2021/11/17
 */
@Data
@ApiModel
public class QueryShopCouponsPageForm extends QueryPageListForm {

    /**
     * 选择的企业ID
     */
    @ApiModelProperty("选择的企业ID")
    private Long eid;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String ename;

}
