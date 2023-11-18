package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC问诊消息类型枚举
 *
 * @Author fan.shen
 * @Date 2023/5/26
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcDiagnosisMsgTypeEnum {

    /**
     * 图文或极速问诊单支付成功
     */
    DIAGNOSIS_PAY_TIP(1, "diagnosis_pay_tip", "图文或极速问诊单支付成功"),

    /**
     * 电话或视频问诊单支付成功
     */
    DIAGNOSIS_VIDEO_PAY_TIP(2, "diagnosis_video_pay_tip", "电话或视频问诊单支付成功"),

    /**
     * 极速图文问诊预约成功
     */
    FAST_DIAGNOSIS_TEXT_PIC_SUB_TIP(3, "fast_diagnosis_text_pic_sub_tip", "极速图文问诊预约成功"),

    /**
     * 医生开具处方
     */
    DOC_DO_PRESCRIPTION(4, "doc_do_prescription", "医生开具处方"),

    /**
     * 处方还有1小时失效
     */
    PRESCRIPTION_EXPIRE_1H(5, "prescription_expire_1h", "处方还有1小时失效"),

    /**
     * 诊后评价提醒
     */
    COMMENT_AFTER_DIAGNOSIS(6, "COMMENT_AFTER_DIAGNOSIS", "诊后评价提醒"),

    /**
     * 图文问诊支付成功，患者首次给医生发送消息后
     */
    PATIENT_FIRST_SEND_MSG(7, "patient_first_send_msg", "图文问诊支付成功，患者首次给医生发送消息后"),

    ;

    private Integer type;

    private String code;

    private String name;

    public static HmcDiagnosisMsgTypeEnum getByType(Integer type) {
        for (HmcDiagnosisMsgTypeEnum e : HmcDiagnosisMsgTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
