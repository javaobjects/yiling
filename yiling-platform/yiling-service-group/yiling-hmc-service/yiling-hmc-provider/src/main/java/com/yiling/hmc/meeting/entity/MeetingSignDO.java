package com.yiling.hmc.meeting.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议签到
 * </p>
 *
 * @author fan.shen
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_meeting_sign")
public class MeetingSignDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 省区
     */
    private String provinceName;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 工作单位
     */
    private String hospitalName;

    /**
     * 科室
     */
    private String departmentName;

    /**
     * 职务
     */
    private String jobTitle;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 是否签到 1-是，2-否
     */
    private Integer signFlag;

    /**
     * 签到时间
     */
    private Date signTime;

    /**
     * 机构编码
     */
    private String sysCode;

    /**
     * 核销码
     */
    private String checkCode;

    private Integer checkFlag;
    private Date checkTime;

    /**
     * 会场来源id 1、2、3...
     */
    private Integer meetingSourceId;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
