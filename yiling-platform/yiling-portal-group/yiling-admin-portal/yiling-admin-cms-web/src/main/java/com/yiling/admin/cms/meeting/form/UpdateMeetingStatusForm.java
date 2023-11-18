package com.yiling.admin.cms.meeting.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新会议状态 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Data
@ApiModel("更新会议状态Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMeetingStatusForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @ApiModelProperty(value = "ID", required = true)
    private Long id;

    /**
     * 操作类型：1-发布 2-取消发布 3-删除
     */
    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "操作类型：1-发布 2-取消发布 3-删除", required = true)
    private Integer opType;

}
