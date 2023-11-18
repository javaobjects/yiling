package com.yiling.ih.patient.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 问诊单回调IH 返回DTO
 */
@Data
@Accessors(chain = true)
public class DiagnosisNotifyIhResponseDTO implements java.io.Serializable {

    private static final long serialVersionUID = 3768586786173659462L;

    /**
     * 状态 1订单金额或状态异常无法更新 2成功
     */
    private Integer status;

}