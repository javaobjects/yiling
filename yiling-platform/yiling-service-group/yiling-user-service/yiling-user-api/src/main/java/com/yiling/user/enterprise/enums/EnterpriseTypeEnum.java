package com.yiling.user.enterprise.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业类型枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Getter
@AllArgsConstructor
public enum EnterpriseTypeEnum {
    //工业
    INDUSTRY(1, "工业", EnterpriseCategoryEnum.INDUSTRY,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.SUPPLY_QUALITY_SYSTEM_SURVEY_FORM,
                    EnterpriseCertificateTypeEnum.ACCOUNT_OPENING_PERMIT,
                    EnterpriseCertificateTypeEnum.DRUG_DISTRIBUTION_GUARANTEE_AGREEMENT,
                    EnterpriseCertificateTypeEnum.QUALIFIED_SUPPLIER_FILE_BAG,
                    EnterpriseCertificateTypeEnum.SAMPLE_SHEET_OF_DELIVERY_ORDER,
                    EnterpriseCertificateTypeEnum.TAX_BILL_SAMPLE,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_MANUFACTURING_LICENSE,
                    EnterpriseCertificateTypeEnum.GMP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_PRODUCTION_LICENSE
            )
    ),
    //商业
    BUSINESS(2, "商业", EnterpriseCategoryEnum.BUSINESS,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.GSP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT,
                    EnterpriseCertificateTypeEnum.SUPPLY_QUALITY_SYSTEM_SURVEY_FORM,
                    EnterpriseCertificateTypeEnum.ACCOUNT_OPENING_PERMIT,
                    EnterpriseCertificateTypeEnum.DRUG_DISTRIBUTION_GUARANTEE_AGREEMENT,
                    EnterpriseCertificateTypeEnum.QUALIFIED_SUPPLIER_FILE_BAG,
                    EnterpriseCertificateTypeEnum.SAMPLE_SHEET_OF_DELIVERY_ORDER,
                    EnterpriseCertificateTypeEnum.CHAPTER_SEAL_STAMPS,
                    EnterpriseCertificateTypeEnum.TAX_BILL_SAMPLE
            )
    ),
    //连锁总店
    CHAIN_BASE(3, "连锁总店", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.SUPPLY_QUALITY_SYSTEM_SURVEY_FORM,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.GSP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
    //连锁直营
    CHAIN_DIRECT(4, "连锁直营", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD, 
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.SUPPLY_QUALITY_SYSTEM_SURVEY_FORM,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.GSP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
    //连锁加盟
    CHAIN_JOIN(5, "连锁加盟", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD, 
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.SUPPLY_QUALITY_SYSTEM_SURVEY_FORM,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.GSP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
    //单体药房
    PHARMACY(6, "单体药房", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.PHARMACEUTICAL_TRADE_LICENSE,
                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD,
                    EnterpriseCertificateTypeEnum.GSP_CERTIFICATE,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
    //医疗机构
    HOSPITAL(7, "医疗机构", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
    //诊所
    CLINIC(8, "诊所", EnterpriseCategoryEnum.TERMINAL,
            ListUtil.toList(
                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
            )
    ),
//    //民营医院
//    HOSPITAL_PRIVATE(9, "民营医院", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.BUSINESS_LICENSE,
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT,
//                    EnterpriseCertificateTypeEnum.ENTERPRISE_CHANGE_RECORD
//            )
//    ),
//    //三级医院
//    HOSPITAL_LEVEL_3(10, "三级医院", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
//    //二级医院
//    HOSPITAL_LEVEL_2(11, "二级医院", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
//    //社区中心
//    COMMUNITY_CENTER(12, "社区中心", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
//    //乡镇卫生院
//    HEALTH_CENTER(13, "乡镇卫生院", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
//    //社区站/村卫生所
//    COMMUNITY_STATION(14, "社区站/村卫生所", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
//    //县人民/中医院
//    HOSPITAL_PEOPLE(15, "县人民/中医院", EnterpriseCategoryEnum.TERMINAL,
//            ListUtil.toList(
//                    EnterpriseCertificateTypeEnum.MEDICAL_STRUCTURE_PRACTICE_LICENSE,
//                    EnterpriseCertificateTypeEnum.POWER_OF_ATTORNEY_FROM_LEGAL_PERSON,
//                    EnterpriseCertificateTypeEnum.CORPORATE_IDENTITY_CARD,
//                    EnterpriseCertificateTypeEnum.FOOD_CIRCULATION_PERMIT
//            )
//    ),
    ;

    private final Integer                             code;
    private final String                              name;
    private final EnterpriseCategoryEnum              category;

    /**
     * 企业类型对应的资质列表
     */
    private final List<EnterpriseCertificateTypeEnum> certificateTypeEnumList;

    public static EnterpriseTypeEnum getByCode(Integer code) {
        for (EnterpriseTypeEnum e : EnterpriseTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static EnterpriseTypeEnum getByName(String name) {
        for (EnterpriseTypeEnum e : EnterpriseTypeEnum.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 是否为工业
     *
     * @return
     */
    public boolean isIndustry() {
        return category == EnterpriseCategoryEnum.INDUSTRY;
    }

    /**
     * 是否为商业
     *
     * @return
     */
    public boolean isBusiness() {
        return category == EnterpriseCategoryEnum.BUSINESS;
    }

    /**
     * 是否为终端
     *
     * @return
     */
    public boolean isTerminal() {
        return category == EnterpriseCategoryEnum.TERMINAL;
    }
}
