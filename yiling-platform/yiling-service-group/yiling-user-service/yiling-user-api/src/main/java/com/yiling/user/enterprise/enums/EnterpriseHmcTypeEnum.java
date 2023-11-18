package com.yiling.user.enterprise.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业HMC类型枚举
 *
 * @author: xuan.zhou
 * @date: 2022/3/25
 */
@Getter
@AllArgsConstructor
public enum EnterpriseHmcTypeEnum {
    /**
     * 药+险销售
     */
    MEDICINE_INSURANCE(1, "药+险销售", ListUtil.toList("hmc-sales")),

    /**
     * 药+险销售与药品兑付
     */
    MEDICINE_INSURANCE_CHECK(2, "药+险销售与药品兑付", ListUtil.toList("hmc-sales-order")),

    /**
     * 医药代表
     */
    MR(3, "医药代表", ListUtil.toList("hmc-mr")),
    ;

    private final Integer code;
    private final String name;
    private final List<String> roleCodeList;

    public static EnterpriseHmcTypeEnum getByCode(Integer code) {
        for (EnterpriseHmcTypeEnum e : EnterpriseHmcTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static EnterpriseHmcTypeEnum getByName(String name) {
        for (EnterpriseHmcTypeEnum e : EnterpriseHmcTypeEnum.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}