package com.yiling.ih.user.feign.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @data: 2022/09/06
 */
@Data
@Accessors(chain = true)
public class ActivityDocPatientCountResponse implements java.io.Serializable{

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
