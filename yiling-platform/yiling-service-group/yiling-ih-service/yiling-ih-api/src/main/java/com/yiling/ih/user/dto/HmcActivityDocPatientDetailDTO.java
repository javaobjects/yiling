package com.yiling.ih.user.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 医带患活动患者详情
 *
 * @author: fan.shen
 * @data: 2023/02/01
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocPatientDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 6232095217976779033L;

    /**
     *患者姓名
     */
    private String patientName;

    /**
     *手机号
     */
    private String mobile;

    /**
     *性别
     */
    private String gender;

    /**
     *年龄
     */
    private Integer age;

    /**
     *疾病标签
     */
    private String diseaseTags;

    /**
     *药品标签
     */
    private String medicationTags;

    /**
     *医生id
     */
    private Integer doctorId;

    /**
     *医院名称
     */
    private String hospitalName;

    /**
     *医生名称
     */
    private String doctorName;

    /**
     *提审时间
     */
    private Date arraignmentTime;

    /**
     * 凭证状态 1-待上传 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer certificateState;

    /**
     *处方图片地址
     */
    private List<String> picture;

    /**
     * 审核日志集合
     */
    private List<PatientCheckLogDTO> logList;
}
