package com.yiling.hmc.activity.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患教活动DTO
 *
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityPatientEducateDTO extends BaseDTO {

    /**
     * 活动名称
     */
    private String activityName;

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
