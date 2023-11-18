package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业审核类型枚举
 *
 * @author: lun.yu
 * @date: 2021/11/3
 */
@Getter
@AllArgsConstructor
public enum EnterpriseAuthTypeEnum {

    //审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
    FIRST(1, "首次认证"),
    UPDATE(2, "资质更新"),
    REJECT_AND_UPDATE(3, "驳回后再次认证"),
    ;

    private final Integer code;
    private final String name;

    public static EnterpriseAuthTypeEnum getByCode(Integer code) {
        for (EnterpriseAuthTypeEnum e : EnterpriseAuthTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
