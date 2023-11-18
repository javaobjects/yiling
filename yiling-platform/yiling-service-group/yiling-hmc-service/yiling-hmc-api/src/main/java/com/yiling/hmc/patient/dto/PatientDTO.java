package com.yiling.hmc.patient.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 患者信息
 * @author: gxl
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientDTO extends BaseDTO {
    private static final long serialVersionUID = 1201138492244032116L;
    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 患者关联的用户id
     */
    private Long fromUserId;

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    private String mobile;
}