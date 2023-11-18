package com.yiling.b2b.app.shop.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * B2B-店铺列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/19
 */
@Data
@ApiModel
public class QueryShopForm extends QueryPageListForm {

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;



}
