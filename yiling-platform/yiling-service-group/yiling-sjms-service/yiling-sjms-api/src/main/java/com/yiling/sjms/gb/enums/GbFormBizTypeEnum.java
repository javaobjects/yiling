package com.yiling.sjms.gb.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购流程类型
 *
 * @author: wei.wang
 * @date: 2022/12/06
 */
@Getter
@AllArgsConstructor
public enum GbFormBizTypeEnum {

    SUBMIT(1, "团购提报"),
    CANCEL(2, "团购取消"),
    GROUP_BUY_COST(11,"团购费用申请"),
    ;

    private Integer code;
    private String name;

    public static GbFormBizTypeEnum getByCode(Integer code) {
        for (GbFormBizTypeEnum e : GbFormBizTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
