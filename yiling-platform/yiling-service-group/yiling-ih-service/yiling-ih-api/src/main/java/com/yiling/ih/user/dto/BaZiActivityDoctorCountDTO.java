package com.yiling.ih.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 八子补肾活动医生 DTO
 *
 * @author: fan.shen
 * @date: 2022-10-24
 */
@NoArgsConstructor
@Data
public class BaZiActivityDoctorCountDTO implements Serializable {

    private static final long serialVersionUID = -2295160299359559926L;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 医生数量
     */
    private Long doctorCount;

}
