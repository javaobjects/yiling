package com.yiling.user.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 企业会员日志操作类型枚举
 *
 * @author: lun.yu
 * @date: 2022-11-22
 */
@Getter
@AllArgsConstructor
public enum EnterpriseMemberLogOpTypeEnum {

    // 操作类型：1-开通 2-续费 3-退费 4-修改推广方
    OPEN(1, "开通"),
    RENEWAL(2, "续费"),
    RETURN(3, "退费"),
    UPDATE_PROMOTER(4, "修改推广方"),
    CANCEL_IMPORT_RECORD(5, "取消导入记录"),
    ;

    private final Integer code;
    private final String name;

    public static EnterpriseMemberLogOpTypeEnum getByCode(Integer code) {
        for (EnterpriseMemberLogOpTypeEnum e : EnterpriseMemberLogOpTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
