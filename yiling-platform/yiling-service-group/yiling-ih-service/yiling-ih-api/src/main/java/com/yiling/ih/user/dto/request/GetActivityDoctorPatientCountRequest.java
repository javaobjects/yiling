package com.yiling.ih.user.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: fan.shen
 * @data: 2023/01/31
 */
@Data
public class GetActivityDoctorPatientCountRequest implements java.io.Serializable {

    /**
     * 活动id集合
     */
    private List<Long> activityIds;
}
