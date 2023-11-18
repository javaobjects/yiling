package com.yiling.open.cms.diagnosis.enums;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 提醒患者短信类型枚举
 *
 * @author: fan.shen
 * @date: 2021/6/8
 */
@Getter
@AllArgsConstructor
public enum RemindPatientSmsTypeEnum {

    REMIND_PATIENT_NEW_MSG("remind_patient_new_msg", "医生给患者发送或回复新消息，患者不在会话页面时的提醒"),

    REMIND_PATIENT_10M_WAIT("remind_patient_10m_wait", "音视频咨询到达预约时间前10分钟提醒用户候诊"),

    REMIND_PATIENT_COME_AGAIN("remind_patient_come_again", "音视频咨询到达预约时间，用户未在会话界面时，再次提醒用户进入诊室"),

    REMIND_PATIENT_NOT_THROUGH("remind_patient_not_through", "医生未在问诊单失效前回复患者提问，或患者音视频未接通"),

    REMIND_PATIENT_DOC_REFUSE("remind_patient_doc_refuse", "医生拒绝接诊通知"),

    REMIND_PATIENT_TO_PAY_PRESCRIPTION("remind_patient_to_pay_prescription", "医生在问诊中开了处方，提醒用户去支付"),

    ;

    private String code;
    private String name;

    public static RemindPatientSmsTypeEnum getFromCode(String code) {
        for (RemindPatientSmsTypeEnum e : RemindPatientSmsTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
