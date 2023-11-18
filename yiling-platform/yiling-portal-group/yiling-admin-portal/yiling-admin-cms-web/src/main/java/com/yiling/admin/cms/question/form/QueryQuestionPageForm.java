package com.yiling.admin.cms.question.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;
import com.yiling.framework.common.base.request.QueryPageListRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryQuestionPageForm extends QueryPageListForm {

    private static final long serialVersionUID = 1515883922320291419L;

    /**
     * 标题
     */
    @Length(max = 20)
    @ApiModelProperty("标题")
    private String title;

    /**
     * 创建开始时间
     */
    @ApiModelProperty("创建开始时间")
    private Date startCreateTime;

    /**
     * 创建结束时间
     */
    @ApiModelProperty("创建结束时间")
    private Date endCreateTime;


}
