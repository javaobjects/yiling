package com.yiling.admin.cms.content.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询会议分页列表 Form
 *
 * @author: lun.yu
 * @date: 2022-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingPageForm extends QueryPageListForm {

    @Length(max = 50)
    @ApiModelProperty(value = "标题")
    private String title;

}