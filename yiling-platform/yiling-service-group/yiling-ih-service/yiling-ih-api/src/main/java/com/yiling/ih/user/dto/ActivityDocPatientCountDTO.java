package com.yiling.ih.user.dto;

import lombok.Data;

/**
 * 医带患医生信息 DTO
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@Data
public class ActivityDocPatientCountDTO implements java.io.Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 医生数量
     */
    private Long doctorCount;

    /**
     * 患者数量
     */
    private Long patientCount;

    /**
     * 活动浏览人数
     */
    private Long uvCount;

}
