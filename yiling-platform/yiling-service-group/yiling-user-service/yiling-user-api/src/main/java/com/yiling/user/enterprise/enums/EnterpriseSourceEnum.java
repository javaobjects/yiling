package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业来源枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/28
 */
@Getter
@AllArgsConstructor
public enum EnterpriseSourceEnum {

    //后台导入
    IMPORT(1, "后台导入"),
    // WEB注册
    WEB_REGIST(2, "WEB注册"),
    // APP注册
    APP_REGIST(3, "APP注册"),
    // ERP对接
    ERP_SYNC(4, "ERP对接"),
    // b2b-app注册
    B2B_APP_REGIST(5, "b2b-app注册"),
    // 销售助手添加
    SALE_ASSISTANT_ADD(6, "销售助手添加"),
    ;

    private Integer code;
    private String name;

    public static EnterpriseSourceEnum getByCode(Integer code) {
        for (EnterpriseSourceEnum e : EnterpriseSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
