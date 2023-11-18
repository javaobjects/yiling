package com.yiling.sales.assistant.app.mr.meeting.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询会议分页列表 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-07
 */
@Data
@ApiModel("查询会议分页列表Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingPageListForm extends QueryPageListForm {

    /**
     * 标题
     */
    @Length(max = 20)
    @ApiModelProperty("标题")
    private String title;

}
