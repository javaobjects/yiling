package com.yiling.admin.hmc.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2021/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEnterpriseGoodsPageListForm extends QueryPageListForm {

    /**
     * 企业名称（全模糊查询）
     */
    @ApiModelProperty(value = "企业名称")
    private String name;
}
