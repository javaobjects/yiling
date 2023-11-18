package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 专利药协议类型1-全产品一级(六大专利药产品)、2-专利一级(六大专利药产品之一)、
 * 3-OTC直供(连花36粒)、4-全产品二级(六大专利药产品)、5-专利二级(六大专利药产品之一)、6-不签协议
 */
@Getter
@AllArgsConstructor
public enum CrmPatentAgreementTypeEnum {
    /**
     * 全产品一级(六大专利药产品)
     */
    WHOLE_PRODUCT_FIRST_LEVEL(1, "全产品一级(六大专利药产品)"),
    /**
     * 专利一级(六大专利药产品之一)
     */
    PATENT_FIRST_LEVEL(2, "专利一级(六大专利药产品之一)"),
    /**
     * OTC直供(连花36粒)
     */
    OTC(3, "OTC直供(连花36粒)"),
    /**
     * 全产品二级(六大专利药产品)
     */
    WHOLE_PRODUCT_TWO_LEVEL(4, "全产品二级(六大专利药产品)"),
    /**
     * 5-专利二级(六大专利药产品之一)
     */
    PATENT_TWO_LEVEL(5, "专利二级(六大专利药产品之一)"),
    /**
     * 不签协议
     */
    NO_AGREEMENT(6, "不签协议"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPatentAgreementTypeEnum getByCode(Integer code) {
        for (CrmPatentAgreementTypeEnum e : CrmPatentAgreementTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    public static String getNameByCode(Integer code) {
        for (CrmPatentAgreementTypeEnum e : CrmPatentAgreementTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}
