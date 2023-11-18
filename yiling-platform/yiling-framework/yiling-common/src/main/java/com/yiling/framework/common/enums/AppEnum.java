package com.yiling.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用枚举
 *
 * @author: xuan.zhou
 * @date: 2021/8/9
 */
@Getter
@AllArgsConstructor
public enum AppEnum {

    MALL_ADMIN(1, "mall_admin", "商家后台", true),
    ADMIN(2, "admin", "运营后台", true),
    B2B_APP(3, "b2b_app", "B2B移动端", false),
    SALES_ASSISTANT_APP(4, "sales_assistant_app", "销售助手APP", false),
    HMC_MINI_PROGRAM(5, "hmc_mini_program", "健康管理中心小程序", false),
    HMC_GZH(6, "hmc_gzh", "健康管理中心公众号", false),
    SJMS(7, "sjms", "神机妙算系统", true),
    MR_APP(8, "mr_app", "医药代表APP", false);
    ;

    private Integer appId;
    private String appCode;
    private String appName;
    private boolean checkPermissionsFlag;

    public static AppEnum getByAppId(Integer appId) {
        for (AppEnum e : AppEnum.values()) {
            if (e.getAppId().equals(appId)) {
                return e;
            }
        }
        return null;
    }
}
