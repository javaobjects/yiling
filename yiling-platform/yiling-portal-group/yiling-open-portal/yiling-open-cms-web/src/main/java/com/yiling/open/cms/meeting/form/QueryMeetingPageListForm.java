package com.yiling.open.cms.meeting.form;

import java.util.Date;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/06/06
 */
@Data
@ApiModel("查询会议管理分页列表Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingPageListForm extends QueryPageListForm {

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    @Range(min = 0, max = 4)
    @ApiModelProperty("引用业务线：0-全部 1-2C患者 2-医生 3-患者 4-店员 5-医代")
    private Integer useLine;

    @Range(min = 1, max = 2)
    @ApiModelProperty("1-未发布（未发布、未发布已过期） 2-已发布（进行中、已结束、未开始）")
    private Integer status;

    @Range(min = 0, max = 1)
    @ApiModelProperty("是否公开：0-否 1-是")
    private Integer publicFlag;

    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("活动开始时间排序：true为倒序，false为正序")
    private Boolean activityStartTimeDesc;

    @ApiModelProperty("大于等于 活动结束时间")
    private Date revertActivityEndTime;


}
