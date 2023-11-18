package com.yiling.f2b.web.article.form;

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
public class QueryArticleForm extends BaseForm {

    /**
     * 文章ID
     */
    @NotNull
    @ApiModelProperty("文章ID")
    private Long id;

}
