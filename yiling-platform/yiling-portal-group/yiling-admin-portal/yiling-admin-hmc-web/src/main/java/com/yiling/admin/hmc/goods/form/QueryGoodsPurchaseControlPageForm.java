package com.yiling.admin.hmc.goods.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsPurchaseControlPageForm extends QueryPageListForm {
    @ApiModelProperty(value = "渠道名称")
    private String enterpriseName;

    @ApiModelProperty(value = "管控药品id",required = true)
    @NotNull
    private Long goodsControlId;
}