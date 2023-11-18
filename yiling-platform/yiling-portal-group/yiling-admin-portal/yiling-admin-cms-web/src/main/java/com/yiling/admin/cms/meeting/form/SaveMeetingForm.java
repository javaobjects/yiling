package com.yiling.admin.cms.meeting.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加会议 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Data
@ApiModel("添加会议Form")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMeetingForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 标题
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /**
     * 封面图
     */
    @NotEmpty
    @ApiModelProperty(value = "封面图", required = true)
    private String backgroundPic;

    /**
     * 是否立即发布：0-否 1-是
     */
    @ApiModelProperty("是否立即发布：0-否 1-是")
    private Integer publishFlag;

    /**
     * 活动开始时间
     */
    @NotNull
    @ApiModelProperty(value = "活动开始时间", required = true)
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    @NotNull
    @ApiModelProperty(value = "活动结束时间", required = true)
    private Date activityEndTime;

    /**
     * 报名结束时间
     */
    @ApiModelProperty("报名结束时间")
    private Date applyEndTime;

    /**
     * 活动形式：1-线上 2-线下
     */
    @NotNull
    @Range(min = 0, max = 2)
    @ApiModelProperty(value = "活动形式：1-线上 2-线下", required = true)
    private Integer activityModus;

    /**
     * 是否有学分：0-否 1-是
     */
    @Range(min = 0, max = 1)
    @ApiModelProperty("是否有学分：0-否 1-是")
    private Integer creditFlag;

    /**
     * 学分值/人
     */
    @ApiModelProperty("学分值/人")
    private Integer creditValue;

    /**
     * 活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它
     */
    @NotNull
    @Range(min = 0, max = 5)
    @ApiModelProperty(value = "活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它（字典：meeting_activity_type）", required = true)
    private Integer activityType;

    /**
     * 是否公开：0-否 1-是
     */
    @NotNull
    @Range(min = 0, max = 1)
    @ApiModelProperty(value = "是否公开：0-否 1-是", required = true)
    private Integer publicFlag;

    /**
     * 主讲人
     */
    @Length(max = 50)
    @ApiModelProperty("主讲人")
    private String mainSpeaker;

    /**
     * 内容详情
     */
    @NotEmpty
    @ApiModelProperty(value = "内容详情", required = true)
    private String content;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    @NotEmpty
    @ApiModelProperty(value = "引用业务线集合：1-2C患者 2-医生 3-患者 4-店员 5-医代（字典：display_line）", required = true)
    private List<Integer> businessLineList;

    @ApiModelProperty(value = "内容权限 1-仅登录 2-需认证通过")
    private Integer viewLimit;
}
