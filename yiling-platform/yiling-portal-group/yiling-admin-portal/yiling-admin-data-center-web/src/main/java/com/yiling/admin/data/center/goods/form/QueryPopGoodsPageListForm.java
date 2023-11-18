package com.yiling.admin.data.center.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPopGoodsPageListForm extends QueryPageListForm {


    /**
     * 注册证号
     */
    @ApiModelProperty(value = "批准文号", example = "Z109090")
    private String licenseNo;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", example = "莲花")
    private String name;

    /**
     * 智能推荐Id
     */
    @ApiModelProperty(value = "智能推荐Id")
    private Long recommendId;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

}
