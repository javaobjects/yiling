package com.yiling.framework.common.log.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志系统标识枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/10
 */
@Getter
@AllArgsConstructor
public enum SystemEnum {

    /**
     * 系统日志枚举
     */
    ADMIN_SYSTEM("admin_system", "运营后台-基础系统"),
    ADMIN_DATA_CENTER("admin_data_center", "运营后台-数据中台子系统"),
    ADMIN_POP("admin_pop", "运营后台-POP子系统"),
    ADMIN_B2B("admin_b2b", "运营后台-B2B子系统"),
    ADMIN_SALES_ASSISTANT("admin_sales_assistant", "运营后台-销售助手子系统"),
    ADMIN_ERP("admin_erp", "运营后台-ERP对接管理子系统"),
    ADMIN_HMC("admin_hmc", "运营后台-健康管理中心子系统"),
    ADMIN_CMS("admin_cms", "运营后台-内容管理子系统"),

    MALL_ADMIN_DATA_CENTER("mall_admin_data_center", "商家中心-数据中台子系统"),
    MALL_ADMIN_POP("mall_admin_pop", "商家中心-POP子系统"),
    MALL_ADMIN_B2B("mall_admin_b2b", "商家中心-B2B子系统"),
    MALL_ADMIN_SALES_ASSISTANT("mall_admin_sales_assistant", "商家中心-销售助手子系统"),
    MALL_ADMIN_HMC("mall_admin_hmc", "商家中心-健康管理中心子系统"),

    SJMS_WEB("sjms_web", "神机妙算系统"),

    POP_WEB("pop_web", "POP前台"),
    B2B_WEB("b2b_web", "B2B前台"),
    HMC_WEB("hmc_web", "HMC前台"),
    ;

    private String code;
    private String name;

    public static SystemEnum getByCode(String code) {
        for (SystemEnum e : SystemEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
