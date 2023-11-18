package com.yiling.admin.data.center.article.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存或者更新文章 Form
 *
 * @author: fan.shen
 * @date: 2021/12/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateArticleForm extends BaseForm {

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    private Long    id;

    /**
     * 文章标题
     */
    @ApiModelProperty("文章标题")
    @NotBlank(message = "文章标题不能为空")
    private String  articleTitle;

    /**
     * 文章描述
     */
    @ApiModelProperty("文章描述")
    @NotBlank(message = "文章描述不能为空")
    private String  articleDesc;

    /**
     * 文章内容
     */
    @ApiModelProperty("文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String  articleContent;

    /**
     * 文章状态
     */
    @ApiModelProperty("文章状态")
    @NotNull(message = "文章状态不能为空")
    private Integer articleStatus;

}
