package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业客户信息数据来源枚举
 *
 * @author: houjie.sun
 * @date: 2021/9/1
 */
@Getter
@AllArgsConstructor
public enum EnterpriseCustomerSourceEnum {

    // 后台导入
    IMPORT(1, "后台导入"),
    // ERP对接
    ERP_SYNC(2, "ERP对接"),
    // 开通POP
    OPEN_POP(3, "开通POP"),
    // 协议生成
    AGREEMENT_CREATE(4, "协议生成"),
    // 线上采购
    ONLINE_PURCHASE(5,"线上采购"),;

    private Integer code;
    private String  name;

    public static EnterpriseCustomerSourceEnum getByCode(Integer code) {
        for (EnterpriseCustomerSourceEnum e : EnterpriseCustomerSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
