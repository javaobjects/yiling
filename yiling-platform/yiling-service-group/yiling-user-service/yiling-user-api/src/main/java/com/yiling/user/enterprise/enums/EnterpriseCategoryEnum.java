package com.yiling.user.enterprise.enums;

import java.util.List;

import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业分类枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/19
 */
@Getter
@AllArgsConstructor
public enum EnterpriseCategoryEnum {
    //工业
    INDUSTRY(1, "工业", ListUtil.toList("b2b_industry"), ListUtil.toList("datacenter")),
    //商业
    BUSINESS(2, "商业", ListUtil.toList("b2b_business"), ListUtil.toList("datacenter")),
    //终端
    TERMINAL(3, "终端", ListUtil.toList("b2b_terminal"), ListUtil.toList("datacenter_terminal"));

    private Integer            code;
    private String             name;

    /**
     * 企业类型对应绑定的B2B角色编码列表
     */
    private final List<String> b2bRoleCodeList;

    /**
     * 企业类型对应绑定的数据中台角色编码列表
     */
    private List<String> dataCenterRoleCodeList;
}
