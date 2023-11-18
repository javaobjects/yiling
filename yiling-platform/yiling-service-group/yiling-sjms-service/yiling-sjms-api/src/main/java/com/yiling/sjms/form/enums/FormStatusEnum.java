package com.yiling.sjms.form.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 团购流程状态枚举
 *
 * @author: xuan.zhou
 * @date: 2021/7/16
 */
@Getter
@AllArgsConstructor
public enum FormStatusEnum {

    UNSUBMIT(10, "待提交"),
    AUDITING(20, "审批中"),
    APPROVE(200, "审批通过"),
    REJECT(201, "审批驳回"),
    ;

    private Integer code;
    private String name;

    public static FormStatusEnum getByCode(Integer code) {
        for (FormStatusEnum e : FormStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
