package com.yiling.sjms.sjsp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购性质枚举
 *
 * @author: dexi.yao
 * @date: 2023/03/03
 */
@Getter
@AllArgsConstructor
public enum DataApprovePlateEnum {
    /**
     * 1-商业公司
     */
    SUPPLIER(1, "商业公司"),
    /**
     * 2-医疗机构
     */
    HOSPITAL(2, "医疗机构"),
    /**
     * 3-零售机构
     */
    PHARMACY(3, "零售机构"),
    ;

    private Integer code;
    private String name;

    public static DataApprovePlateEnum getByCode(Integer code) {
        for (DataApprovePlateEnum e : DataApprovePlateEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
