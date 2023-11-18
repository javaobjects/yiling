package com.yiling.hmc.patient.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存就诊人
 * @author: gxl
 * @date: 2022/4/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePatientRequest extends BaseRequest {

    private static final long serialVersionUID = 2280448824232686833L;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 患者关联的用户id
     */
    private Long fromUserId;

    /**
     * 被保人身份证号
     */
    private String idCard;

    /**
     * 被保人手机号
     */
    private String mobile;

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


}