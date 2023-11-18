package com.yiling.ih.patient.dto;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 患者DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcPatientDTO extends BaseDTO {

    private static final long serialVersionUID = 3768586786173659462L;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 患者关联的用户id
     */
    private Long fromUserId;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者性别
     */
    private Integer gender;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 患者绑定的医生数量
     */
    private Long bindDoctorCount;

    /**
     * 患者绑定的用户数量
     */
    private Long bindUserCount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 药品标签
     */
    private String medicationTags;

    /**
     * 疾病标签
     */
    private String  diseaseTags;

}