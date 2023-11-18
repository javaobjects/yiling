package com.yiling.user.enterprise.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业渠道枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/7
 */
@Getter
@AllArgsConstructor
public enum EnterpriseChannelEnum {
    //工业
    INDUSTRY(1L, "工业", 1L),
    //工业直属
    INDUSTRY_DIRECT(2L, "工业直属", 2L),
    //一级商
    LEVEL_1(3L, "一级商", 3L),
    //二级商
    LEVEL_2(4L, "二级商", 3L),
    //KA客户
    KA(5L, "KA客户", 5L),
    //专二普一
    Z2P1(6L, "专二普一", 3L),
    ;

    private Long code;
    private String name;
    private Long roleId;

    public static EnterpriseChannelEnum getByCode(Long code) {
        for (EnterpriseChannelEnum e : EnterpriseChannelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static EnterpriseChannelEnum getByName(String name) {
        for (EnterpriseChannelEnum e : EnterpriseChannelEnum.values()) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 是否为工业
     *
     * @param code
     * @return
     */
    public static boolean isIndustry(Long code) {
        return EnterpriseChannelEnum.INDUSTRY == getByCode(code);
    }

    /**
     * 是否为工业直属
     *
     * @param code
     * @return
     */
    public static boolean isIndustryDirect(Long code) {
        return EnterpriseChannelEnum.INDUSTRY_DIRECT == getByCode(code);
    }

    /**
     * 是否为一级商
     *
     * @param code
     * @return
     */
    public static boolean isLevel1(Long code) {
        return EnterpriseChannelEnum.LEVEL_1 == getByCode(code);
    }

    /**
     * 是否为二级商
     *
     * @param code
     * @return
     */
    public static boolean isLevel2(Long code) {
        return EnterpriseChannelEnum.LEVEL_2 == getByCode(code);
    }

    /**
     * 是否为KA用户
     *
     * @param code
     * @return
     */
    public static boolean isKa(Long code) {
        return EnterpriseChannelEnum.KA == getByCode(code);
    }

    /**
     * 是否为专二普一
     *
     * @param code
     * @return
     */
    public static boolean isZ2p1(Long code) {
        return EnterpriseChannelEnum.Z2P1 == getByCode(code);
    }

}
