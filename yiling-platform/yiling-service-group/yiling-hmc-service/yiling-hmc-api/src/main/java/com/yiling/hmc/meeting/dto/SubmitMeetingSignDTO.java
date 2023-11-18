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
public class SubmitMeetingSignDTO extends BaseDTO {

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
     * 签到结果 1-提交签到成功，2-之前已签到
     */
    private Integer signResult;


    /**
     * 核销码
     */
    private String checkCode;

    private Date firstSignDate;
    
    /**
     * 会场来源id 1、2、3...
     */
    private Integer meetingSourceId;

}
