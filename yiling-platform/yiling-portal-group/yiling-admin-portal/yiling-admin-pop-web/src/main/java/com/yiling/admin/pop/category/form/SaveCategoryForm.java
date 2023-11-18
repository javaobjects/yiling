package com.yiling.admin.pop.category.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.admin.common.form.GoodsForm;
import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 创建分类管理 form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCategoryForm extends BaseForm {


    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @NotEmpty
    private String name;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 商品集合
     */
    @ApiModelProperty(value = "商品集合")
    private List<GoodsForm> goodsList;
}
