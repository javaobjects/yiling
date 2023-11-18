package com.yiling.admin.cms.feedback.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/7/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFeedbackPageListForm extends QueryPageListForm {
    private Date startTime;

    private Date endTime;

    @ApiModelProperty(value = "类型 枚举同内容业务线一样")
    private Integer source;
}