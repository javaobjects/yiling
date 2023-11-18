package com.yiling.workflow.workflow.enums;

/**
 * @author: gxl
 * @date: 2022/11/29
 */
public enum  FlowCommentEnum {
    /**
     * 说明
     */
    NORMAL("1", "提交申请"),
    REBACK("2", "驳回"),
    PASS("3", "审批通过"),
    PROCESSING("4","处理中"),
    AUTO_PASS("5", "自动审批通过"),

    ;

    public static FlowCommentEnum getByType(String type) {
        for (FlowCommentEnum e : FlowCommentEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    FlowCommentEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
