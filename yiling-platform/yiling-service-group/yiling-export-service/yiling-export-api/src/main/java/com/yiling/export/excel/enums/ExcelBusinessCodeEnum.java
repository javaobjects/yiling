package com.yiling.export.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum ExcelBusinessCodeEnum {
    //平台发商品券
    SEND_COUPON("send_coupon", "giveCouponExcelImportService"),
    //平台发会员券
    SEND_MEMBER_COUPON("send_member_coupon", "giveCouponExcelImportService");

    private String code;
    private String className;

    public static ExcelBusinessCodeEnum getByCode(Integer code) {
        for (ExcelBusinessCodeEnum e : ExcelBusinessCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
