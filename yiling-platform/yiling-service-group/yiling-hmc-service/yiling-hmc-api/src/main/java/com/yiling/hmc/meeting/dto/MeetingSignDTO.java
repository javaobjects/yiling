package com.yiling.hmc.meeting.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 会议签到DTO
 * @author: fan.shen
 * @data: 2023/04/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingSignDTO extends BaseDTO {

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

}
