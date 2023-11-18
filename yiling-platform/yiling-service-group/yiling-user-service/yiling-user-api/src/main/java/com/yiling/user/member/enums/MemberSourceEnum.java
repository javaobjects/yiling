package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员数据来源枚举
 *
 * @author: lun.yu
 * @date: 2022-07-15
 */
@Getter
@AllArgsConstructor
public enum MemberSourceEnum {

    // 数据来源：1-B2B-自然流量 2-B2B-企业推广 3-销售助手
    B2B_NATURE(1, "B2B-自然流量"),
    B2B_ENTERPRISE(2, "B2B-企业推广"),
    ASSISTANT(3, "销售助手"),
    IMPORT_GIVE(4, "导入-赠送"),
    IMPORT_P2P(5, "导入-公对公打款"),
    ;

    private final Integer code;
    private final String name;

    public static MemberSourceEnum getByCode(Integer code) {
        for (MemberSourceEnum e : MemberSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
