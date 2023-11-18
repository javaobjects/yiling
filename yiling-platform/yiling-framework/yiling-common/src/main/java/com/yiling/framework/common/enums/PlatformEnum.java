package com.yiling.framework.common.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/16
 */
@Getter
@AllArgsConstructor
public enum PlatformEnum {

    POP(1, "POP", ListUtil.empty()),
    B2B(2, "B2B", ListUtil.empty()),
    SALES_ASSIST(3, "销售助手", ListUtil.toList("sales_assistant")),
    // INTERNET_HOSPITAL(4, "互联网医院", ListUtil.empty()),
    HMC(5, "健康管理中心", ListUtil.toList("hmc")),

    /**
     * 商家中心-中台数据管理（开通其他平台时，自动开通此平台）
     */
    DATA_CENTER(5, "数据中台", ListUtil.empty()),
    ;

    private Integer      code;
    private String       name;

    /**
     * 平台对应的管理员角色编码列表
     */
    private List<String> roleCodeList;

    public static PlatformEnum getByCode(Integer code) {
        for (PlatformEnum e : PlatformEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static PlatformEnum getByName(String name) {
        for (PlatformEnum e : PlatformEnum.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
