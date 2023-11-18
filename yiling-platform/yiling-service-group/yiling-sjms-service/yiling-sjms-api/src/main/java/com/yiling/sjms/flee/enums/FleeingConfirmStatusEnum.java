package com.yiling.sjms.flee.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 确认状态：1-待提交 2-提交成功
 *
 * @author: yong.zhang
 * @date: 2023/3/20 0020
 */
@Getter
@AllArgsConstructor
public enum FleeingConfirmStatusEnum {

    /**
     * 待提交
     */
    TO_BE_SUBMIT(1, "待提交"),
    /**
     * 已提交
     */
    SUBMITTED(2, "已提交"),
    /**
     * 已提交/未清洗
     */
    SUBMITTED_UNCLEANED(3, "已提交/未清洗"),
    ;

    private final Integer code;
    private final String name;

    public static FleeingConfirmStatusEnum getByCode(Integer code) {
        for (FleeingConfirmStatusEnum e : FleeingConfirmStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
