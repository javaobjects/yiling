package com.yiling.ih.user.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class HmcActivityDoctorDTO extends BaseDTO {
    private static final long serialVersionUID = 6232095217976779033L;
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     *医生id
     */
    private Integer doctorId;
    /**
     *医生姓名
     */
    private String name;
    /**
     *医院名称
     */
    private String hospitalName;
    /**
     *邀请病历数量
     */
    private Long caseCount;
    /**
     *活动码
     */
    private String qrcodeUrl;
    /**
     *创建时间
     */
    private Date createTime;

    /**
     *医生是否已参加活动 1：是 2：否
     */
    private Integer activityState;

}
