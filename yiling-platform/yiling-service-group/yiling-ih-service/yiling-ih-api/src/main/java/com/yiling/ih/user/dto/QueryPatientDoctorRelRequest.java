package com.yiling.ih.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询是否已入组 Request
 *
 * @author: fan.shen
 * @date: 2022-09-07
 */
@Data
@Accessors(chain = true)
public class QueryPatientDoctorRelRequest implements java.io.Serializable {

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 用户id
     */
    private Integer userId;

}
