package com.yiling.workflow.workflow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程操作类型枚举
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Getter
@AllArgsConstructor
public enum WfActTypeEnum {

    SUBMIT(1, "提交申请", "审批意见"),
    REJECT(2, "驳回", "审批意见"),
    APPROVE(3, "审批通过", "审批意见"),
    FORWARD(4,"转发", "转发提示语"),
    COMMENT(5, "批注", "批注"),
    ;

    private Integer code;
    private String name;
    /**
     * 操作类型对应的文字类型名称
     */
    private String textTypeName;

    public static WfActTypeEnum getByCode(Integer code) {
        for (WfActTypeEnum e : WfActTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
