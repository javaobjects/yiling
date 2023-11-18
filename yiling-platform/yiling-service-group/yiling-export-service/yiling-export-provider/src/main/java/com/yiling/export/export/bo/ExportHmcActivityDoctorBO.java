package com.yiling.export.export.bo;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class ExportHmcActivityDoctorBO {
    /**
     * 活动id
     */
    private Integer activityId;
    /**
     * 医生id
     */
    private Integer doctorId;
    /**
     * 医生姓名
     */
    private String name;
    /**
     * 医院名称
     */
    private String hospitalName;
    /**
     * 邀请病历数量
     */
    private Long caseCount;
    /**
     * 活动码
     */
    private String qrcodeUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    private String createTimeStr;


    // 新增属性

    /**
     * 所属省
     */
    private String provinceName;

    /**
     * 报道总人数
     */
    private Long reportCount;

    /**
     * 新用户报道人数
     */
    private Long newReportCount;

    /**
     * 处方审核通过人数
     */
    private Long prescriptionCount;


}
