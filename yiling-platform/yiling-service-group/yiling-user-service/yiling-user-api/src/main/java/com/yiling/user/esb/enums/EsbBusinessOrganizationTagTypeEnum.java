package com.yiling.user.esb.enums;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ESB业务架构标签类型枚举类
 *
 * @author: lun.yu
 * @date: 2023-04-12
 */
@Getter
@AllArgsConstructor
public enum EsbBusinessOrganizationTagTypeEnum {

    /**
     * 打标类型：1-事业部打标 2-业务省区打标 3-区办打标
     */
    BUSINESS_TAG(1, "事业部打标"),
    PROVINCE_TAG(2, "业务省区打标"),
    REGION_TAG(3, "区办打标"),
    ;

    private final Integer code;
    private final String name;

    public static EsbBusinessOrganizationTagTypeEnum getByCode(Integer code) {
        for (EsbBusinessOrganizationTagTypeEnum e : EsbBusinessOrganizationTagTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
