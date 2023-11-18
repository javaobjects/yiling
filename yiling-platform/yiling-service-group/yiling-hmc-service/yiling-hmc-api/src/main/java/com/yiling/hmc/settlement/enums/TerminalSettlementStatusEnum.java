package com.yiling.hmc.settlement.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
 *
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Getter
@AllArgsConstructor
public enum TerminalSettlementStatusEnum {
    /**
     * 1-待结算
     */
    UN_SETTLEMENT(1, "待结算"),
    /**
     * 2-已结算
     */
    SETTLEMENT(2, "已结算"),
    /**
     * 3-无需结算失效单
     */
    NO_NEED_SETTLEMENT(3, "无需结算失效单"),
    ;

    private final Integer code;
    private final String name;

    public static TerminalSettlementStatusEnum getByCode(Integer code) {
        for (TerminalSettlementStatusEnum e : TerminalSettlementStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
