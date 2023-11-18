package com.yiling.user.procrelation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 快照类型：1-正在使用 2-历史版本
 *
 * @author: dexi.yao
 * @date: 2021/8/27
 */
@Getter
@AllArgsConstructor
public enum PorcRelationSnapshotTypeEnum {

    // 正在使用
    CURRENT(1, "正在使用"),
    // 历史版本
    HISTORY(2, "历史版本");

    private final Integer code;
    private final String name;

    public static PorcRelationSnapshotTypeEnum getByCode(Integer code) {
        for (PorcRelationSnapshotTypeEnum e : PorcRelationSnapshotTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
