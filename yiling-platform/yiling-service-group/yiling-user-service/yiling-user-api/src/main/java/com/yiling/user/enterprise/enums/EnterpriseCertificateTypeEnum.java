package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业资质类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/21
 */
@Getter
@AllArgsConstructor
public enum EnterpriseCertificateTypeEnum {

    /**
     * 企业资质类型
     */
    BUSINESS_LICENSE(1, "营业执照",true ,true),
    PHARMACEUTICAL_TRADE_LICENSE(2, "药品经营许可证",true,true),

    ENTERPRISE_CHANGE_RECORD(3, "企业变更记录",false,false),
    CORPORATE_IDENTITY_CARD(4, "法人身份证",false,true),
    POWER_OF_ATTORNEY_FROM_LEGAL_PERSON(5, "法人授权委托书",false,true),
    GSP_CERTIFICATE(6, "GSP证书",false,false),
    FOOD_CIRCULATION_PERMIT(7, "食品流通许可证",false,false),
    BUSINESS_CERTIFICATE_MEDICAL_DEVICES_ONE(8, "一类医疗器械经营备案凭证",false,true),
    BUSINESS_CERTIFICATE_MEDICAL_DEVICES_TWO(9, "二类医疗器械经营备案凭证",false,true),
    MEDICAL_DEVICE_BUSINESS_LICENSE(10, "医疗器械经营许可证",false,true),
    MEDICAL_STRUCTURE_PRACTICE_LICENSE(11, "医疗机构执业许可证",true,true),
    SUPPLY_QUALITY_SYSTEM_SURVEY_FORM(12, "供货企业质量保证体系调查表",false,true),
    ACCOUNT_OPENING_PERMIT(13, "开户许可证",false,true),
    DRUG_DISTRIBUTION_GUARANTEE_AGREEMENT(14, "药品经营保障协议",false,true),
    QUALIFIED_SUPPLIER_FILE_BAG(15, "合格供货方档案袋",false,true),
    SAMPLE_SHEET_OF_DELIVERY_ORDER(16, "出库单样本表",false,true),
    CHAPTER_SEAL_STAMPS(17, "印模印章",false,true),
    TAX_BILL_SAMPLE(18, "税票样本",false,true),
    PHARMACEUTICAL_MANUFACTURING_LICENSE(19, "药品生产许可证",false,true),
    GMP_CERTIFICATE(20, "GMP证书",false,true),
    FOOD_PRODUCTION_LICENSE(21, "食品生产许可证",false,true),
    ;

    private final Integer code;
    private final String name;
    /**
     * 是否要收集证照日期
     */
    private final Boolean collectDate;
    /**
     * 是否必填
     */
    private final Boolean mustExist;

    public static EnterpriseCertificateTypeEnum getByCode(Integer code) {
        for (EnterpriseCertificateTypeEnum e : EnterpriseCertificateTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
