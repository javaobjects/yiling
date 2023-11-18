package com.yiling.cms.meeting.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议管理表
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("meeting")
public class MeetingDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    private String backgroundPic;

    /**
     * 是否立即发布：0-否 1-是
     */
    private Integer publishFlag;

    /**
     * 活动开始时间
     */
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 报名结束时间
     */
    private Date applyEndTime;

    /**
     * 活动形式：1-线上 2-线下
     */
    private Integer activityModus;

    /**
     * 是否有学分：0-否 1-是
     */
    private Integer creditFlag;

    /**
     * 学分值/人
     */
    private Integer creditValue;

    /**
     * 活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它
     */
    private Integer activityType;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer publicFlag;

    /**
     * 主讲人
     */
    private String mainSpeaker;

    /**
     * 活动状态：1-未发布 2-已发布（进行中、已结束、未开始）
     */
    private Integer status;

    /**
     * 阅读量（PV）
     */
    private Integer readNum;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;

    /**
     * 医生端查看权限1-仅登录 2-需认证通过
     */
    private Integer viewLimit;
}
