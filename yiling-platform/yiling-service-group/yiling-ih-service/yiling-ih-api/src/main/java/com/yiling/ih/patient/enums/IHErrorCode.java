package com.yiling.ih.patient.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/7/5
 */
@Getter
@AllArgsConstructor
public enum IHErrorCode implements IErrorCode {

    ID_CARD_OCR(10000, "身份证识别失败"),

    ACTIVITY_DOC_PATIENT(10001, "医带患添加医生失败"),

    UPDATE_ACTIVITY_DOCTOR_ERROR(10002, "更新活动医生状态异常"),

    ACTIVITY_GET_PATIENT_ERROR(10003, "医带患获取患者详情异常"),

    ACTIVITY_DOC_PATIENT_AUDIT_ERROR(10004, "医带患患者审核异常"),

    ACTIVITY_DOC_PATIENT_QUERY_ERROR(10005, "查询医生信息异常"),

    ACTIVITY_DOC_IMPORT_ERROR(10005, "批量导入医生信息异常"),

    ACTIVITY_DOC_REMOVE_PATIENT(10001, "八子补肾活动移除活动医生失败"),

    SYNC_PHARMACY_ERROR(10002, "同步终端药店异常"),

    ;

    private final Integer code;
    private final String message;
}
