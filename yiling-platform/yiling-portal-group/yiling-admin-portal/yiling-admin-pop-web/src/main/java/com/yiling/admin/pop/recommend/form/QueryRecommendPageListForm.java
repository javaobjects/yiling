package com.yiling.admin.pop.recommend.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询banner分页列表 Form
 *
 * @author: yuecheng.yue
 * @date: 2021/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRecommendPageListForm extends QueryPageListForm {


    /**
     * Recommend标题
     */
    @ApiModelProperty(value = "Recommend标题")
    private String title;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;



}
