package com.yiling.user.procrelation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
 *
 * @author: dexi.yao
 * @date: 2021/8/27
 */
@Getter
@AllArgsConstructor
public enum ProcurementRelationStatusEnum {

    // 未开始
    NOT_STARTED(1, "未开始"),
    // 进行中
    IN_PROGRESS(2, "进行中"),
    // 已停用
    DEACTIVATED(3, "已停用"),
    // 已过期
    EXPIRED(4, "已过期");

    private final Integer code;
    private final String name;

    public static ProcurementRelationStatusEnum getByCode(Integer code) {
        for (ProcurementRelationStatusEnum e : ProcurementRelationStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
