package com.yiling.user.agreementv2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 核心商品组任务量未完成时枚举
 *
 * @author: lun.yu
 * @date: 2022/2/28
 */
@Getter
@AllArgsConstructor
public enum AgreementCoreCommodityGroupFailEnum {

    /**
     * 核心商品组任务量未完成时
     */
    CORE_NOT_CASH(1, "仅当前核心商品组不兑付"),
    MAIN_TASK_NOT_CASH(2, "全商品主任务不兑付"),
    ALL_NOT_CASH(3, "全协议均不兑付"),
    ;

    private final Integer code;
    private final String name;

    public static AgreementCoreCommodityGroupFailEnum getByCode(Integer code) {
        for (AgreementCoreCommodityGroupFailEnum e : AgreementCoreCommodityGroupFailEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
