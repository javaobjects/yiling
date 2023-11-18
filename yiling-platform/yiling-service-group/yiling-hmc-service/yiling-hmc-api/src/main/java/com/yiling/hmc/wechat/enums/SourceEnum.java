package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 注册来源枚举类
 *
 * @Author fan.shen
 * @Date 2022/3/28
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SourceEnum {

    /**
     * 自然流量
     */
    NATURE(1, "自然流量"),

    /**
     * 店员或销售
     */
    STAFF_SELLER(2, "店员或销售"),

    /**
     * 扫药盒二维码
     */
    BOX_CODE(3, "扫药盒二维码"),

    /**
     * 医生推荐
     */
    DOCTOR(4, "医生推荐"),

    /**
     * 用户推荐
     */
    USER_INVITE(5, "用户推荐"),

    /**
     * 以岭互联网医院
     */
    YL_INTERNET_HOSPITAL(6, "以岭互联网医院"),

    ;

    private Integer type;

    private String name;

    /**
     * 2-店员或销售、 5-用户推荐
     *
     * @param type
     * @return
     */
    public static Boolean checkIsStaffOrUserType(Integer type) {
        if (STAFF_SELLER.getType().equals(type) || USER_INVITE.getType().equals(type)) {
            return true;
        }
        return false;
    }

    public static SourceEnum getByType(Integer type) {
        for (SourceEnum e : SourceEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
