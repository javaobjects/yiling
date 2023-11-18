package com.yiling.admin.pop.category.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询Category商品分页列表 Form
 *
 * @author: yuecheng.yue
 * @date: 2021/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryCategoryGoodsPageListForm extends QueryPageListForm {

    /**
     * CategoryID
     */
    @NotNull
    @ApiModelProperty(value = "categoryID")
    private Long categoryId;

}
