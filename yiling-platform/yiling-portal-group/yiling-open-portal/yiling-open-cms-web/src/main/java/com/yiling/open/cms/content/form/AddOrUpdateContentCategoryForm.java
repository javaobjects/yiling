package com.yiling.open.cms.content.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/05
 */
@Data
@Accessors(chain = true)
public class AddOrUpdateContentCategoryForm {

    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    @NotNull
    @ApiModelProperty(value = "模块id 1-医院健康资讯 2-医院首页推荐")
    private Long moduleId;

    @NotNull
    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    @ApiModelProperty(value = "栏目排序")
    private Integer categoryRank;
}
