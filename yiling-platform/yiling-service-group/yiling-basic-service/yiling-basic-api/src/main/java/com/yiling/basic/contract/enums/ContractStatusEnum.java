package com.yiling.basic.contract.enums;

/**
 * @author fucheng.bai
 * @date 2022/11/14
 */
public enum ContractStatusEnum {

    DRAFT("草稿"),
    SIGNING("签署中"),
    COMPLETE("已完成"),
    REJECTED("已退回"),
    RECALLED("已撤回"),
    EXPIRED("已过期"),
    FILLING("拟定中"),
    TERMINATING("确认作废中"),
    TERMINATED("已作废"),
    DELETE("已删除"),
    FINISHED("强制完成");

    private String desc;

    ContractStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
