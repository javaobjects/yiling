package com.yiling.ih.user.feign.dto.response;

import com.yiling.ih.common.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询是否入组
 *
 * @author: fan.shen
 * @date: 2022/9/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePatientRelResponse extends BaseResponse {

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 患者名称
     */
    private String patientName;


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
     * 手机号
     */
    private String mobile;
}