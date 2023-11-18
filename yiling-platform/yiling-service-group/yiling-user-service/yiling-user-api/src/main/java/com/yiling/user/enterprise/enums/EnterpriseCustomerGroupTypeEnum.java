package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业客户分组，分组类型枚举类
 * @author: houjie.sun
 * @date: 2021/9/8
 */
@Getter
@AllArgsConstructor
public enum EnterpriseCustomerGroupTypeEnum {

    // 1-平台创建
    SYSTEM(1, "平台创建"),
    // 2-ERP同步
    ERP_SYNC(2, "ERP同步"),;

    private Integer code;
    private String  name;

    public static EnterpriseCustomerGroupTypeEnum getByCode(Integer code) {
        for (EnterpriseCustomerGroupTypeEnum e : EnterpriseCustomerGroupTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
