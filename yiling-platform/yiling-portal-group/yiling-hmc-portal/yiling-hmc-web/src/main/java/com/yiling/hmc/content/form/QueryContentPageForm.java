package com.yiling.hmc.content.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 内容分页列表查询参数
 * @author: gxl
 * @date: 2022/3/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryContentPageForm extends QueryPageListForm {

    /**
     * 栏目id
     */
    @ApiModelProperty(value = "标准库商品id")
    @NotNull
    private Long standardGoodsId;


}