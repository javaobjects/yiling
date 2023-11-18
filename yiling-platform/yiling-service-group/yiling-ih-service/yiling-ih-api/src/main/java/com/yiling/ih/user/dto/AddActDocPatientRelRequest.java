package com.yiling.ih.user.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 保存活动医带患信息 Request
 *
 * @author: fan.shen
 * @date: 2022-09-13
 */
@Data
@Accessors(chain = true)
public class AddActDocPatientRelRequest implements java.io.Serializable {

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 用户id
     */
    private Integer fromUserId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 医生id
     */
    private Integer doctorId;

    /**
     * 性别1 : 男 ， 0 : 女
     */
    private Integer gender;

    /**
     * 标签:所患疾病，所用药品
     */
    private List<PatientTagRequest> patientTagForms;

    /**
     * 出生日期 19511120
     */
    private String birthday;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 是否是新用户 1-是 2-不是
     */
    private Integer userState;

    /**
     * 医带患活动状态 1-进行中，2-已结束
     */
    private Integer activityState;

    /**
     * 实名认证标志 1-是，0-否
     */
    private Integer realNameFlag;

    /**
     * 身份证
     */
    private String idCard;
}
