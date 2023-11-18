package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业平台操作类型枚举
 *
 * @author: xuan.zhou
 * @date: 2022/3/15
 */
@Getter
@AllArgsConstructor
public enum EnterprisePlatformOpTypeEnum {

    // 开通
    OPEN(1, "开通"),
    // 关闭
    CLOSE(2, "关闭"),
    ;

    private Integer code;
    private String name;

    public static EnterprisePlatformOpTypeEnum getByCode(Integer code) {
        for (EnterprisePlatformOpTypeEnum e : EnterprisePlatformOpTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
