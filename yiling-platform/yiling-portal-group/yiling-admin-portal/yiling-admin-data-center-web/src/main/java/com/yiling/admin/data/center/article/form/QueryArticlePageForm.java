package com.yiling.admin.data.center.article.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询文章 Form
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryArticlePageForm extends QueryPageListForm {

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    private String  articleTitle;

    /**
     * 文章状态
     */
    @ApiModelProperty("文章状态")
    private Integer articleStatus;

}
