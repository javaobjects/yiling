package com.yiling.hmc.activity.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动DTO
 *
 * @author: fan.shen
 * @date: 2023-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityDTO extends BaseDTO {

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动状态 1-启用，2-停用
     */
    private Integer activityStatus;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 活动下的医生数量
     */
    private Long activityDoctorCount;

}
