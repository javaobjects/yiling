package com.yiling.sales.assistant.app.mr.question.form;

import java.util.Date;

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
     * 是否回复 1-未回复 2-已回复 3-不需要回复
     */
    @ApiModelProperty(value = "是否回复 1-未回复 2-已回复" ,required = true)
    private Integer replyFlag;
}
