package com.yiling.open.cms.question.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQuestionDelegatePageForm extends QueryPageListForm {

    private static final long serialVersionUID = 1515883922320291419L;

    /**
     * 标题
     */
    @Length(max = 20)
    @ApiModelProperty("标题")
    private String title;

    /**
     *提问人id
     */
    @ApiModelProperty(value = "提问人id",required = true)
    @NotNull
    private Long fromUserId;

}
