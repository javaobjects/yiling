package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
 *
 * @author: yong.zhang
 * @date: 2023/2/17 0017
 */
@Getter
@AllArgsConstructor
public enum CrmPharmacyLabelAttributeEnum {

    /**
     * 1-社区店
     */
    COMMUNITY(1, "社区店"),
    /**
     * 2-商圈店
     */
    TRADING_AREA(2, "商圈店"),
    /**
     * 3-院边店
     */
    COURTYARD(3, "院边店"),
    /**
     * 4-电商店
     */
    E_COMMERCE(4, "电商店"),
    ;

    private final Integer code;
    private final String name;

    public static CrmPharmacyLabelAttributeEnum getByCode(Integer code) {
        for (CrmPharmacyLabelAttributeEnum e : CrmPharmacyLabelAttributeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
