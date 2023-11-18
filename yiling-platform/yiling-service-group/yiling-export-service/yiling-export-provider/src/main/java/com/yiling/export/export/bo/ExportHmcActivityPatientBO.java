package com.yiling.export.export.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动-患者信息
 * @author: fan.shen
 * @data: 2023-02-06
 */
@Data
@Accessors(chain = true)
public class ExportHmcActivityPatientBO {

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别
     */
    private String gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 疾病标签
     */
    private String diseaseTags;

    /**
     * 药品标签
     */
    private String medicationTags;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 医院省份
     */
    private String province;

    /**
     * 医生名称
     */
    private String doctorName;

    /**
     * 是否是新用户 1-是 2-不是
     */
    private String userState;

    /**
     * 绑定时间
     */
    private Date createTime;
    private String createTimeStr;

    /**
     * 提审时间
     */
    private Date arraignmentTime;
    private String arraignmentTimeStr;

    /**
     * 凭证状态 1-待上传 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer certificateState;

    /**
     * 审核时间
     */
    private Date auditTime;
    private String auditTimeStr;

    /**
     * 驳回原因
     */
    private String rejectReason;
}
