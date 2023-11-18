package com.yiling.user.system.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 权限应用枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/31
 */
@Getter
@AllArgsConstructor
public enum PermissionAppEnum {

    // 运营后台
    ADMIN(1, "运营后台"),

    // 商家后台整体，普通用户的角色所属应用ID为这个
    MALL_ADMIN(100, "商家后台整体"),
    // 商家后台-POP管理
    MALL_ADMIN_POP(2, "POP管理"),
    // 商家后台-B2B管理
    MALL_ADMIN_B2B(3, "B2B管理"),
    // 商家后台-互联网医院
    // MALL_ADMIN_HOSPITAL(4, "互联网医院"),
    // 商家后台-中台数据管理
    MALL_ADMIN_DATA_CENTER(5, "企业数据管理"),
    // 商家后台-销售助手
    MALL_ADMIN_SALES_ASSISTANT(6, "销售助手"),
    // 商家后台-健康管理中心
    MALL_ADMIN_HMC(9, "健康管理中心"),

    // B2B移动端
    B2B_APP(7, "B2B移动端"),

    // 销售助手APP
    SALES_ASSISTANT_APP(8, "销售助手APP"),

    // 神机妙算系统
    SJMS(10, "神机妙算系统"),

    // 医药代表APP
    MR_APP(11, "医药代表APP"),
    ;

    private Integer code;
    private String name;

    public static PermissionAppEnum getByCode(Integer code) {
        for (PermissionAppEnum e : PermissionAppEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 获取商家后台所有子系统
     *
     * @return java.util.List<com.yiling.user.system.enums.PermissionAppEnum>
     * @author xuan.zhou
     * @date 2022/5/12
     **/
    public static List<PermissionAppEnum> getMallAdminAll() {
        return ListUtil.toList(PermissionAppEnum.MALL_ADMIN_DATA_CENTER, PermissionAppEnum.MALL_ADMIN_POP, PermissionAppEnum.MALL_ADMIN_B2B, PermissionAppEnum.MALL_ADMIN_SALES_ASSISTANT, PermissionAppEnum.MALL_ADMIN_HMC);
    }
}
