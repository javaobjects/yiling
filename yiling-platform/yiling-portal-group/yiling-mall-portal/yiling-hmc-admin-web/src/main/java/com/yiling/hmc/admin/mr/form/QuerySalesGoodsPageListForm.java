package com.yiling.hmc.admin.mr.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询可售商品分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySalesGoodsPageListForm extends QueryPageListForm {

    /**
     * 员工ID
     */
    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "员工ID", required = true)
    private Long employeeId;

    /**
     * 药品名称（模糊查询）
     */
    @ApiModelProperty("药品名称（模糊查询）")
    private String name;
}
