package com.yiling.ih.user.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @data: 2022-11-09
 */
@Data
@Accessors(chain = true)
public class VerifyActivityDoctorRequest implements java.io.Serializable{

    private Integer doctorId;

    private Integer activityId;
}
