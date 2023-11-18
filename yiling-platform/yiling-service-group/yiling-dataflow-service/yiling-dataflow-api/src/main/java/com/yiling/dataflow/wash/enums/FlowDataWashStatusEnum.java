package com.yiling.dataflow.wash.enums;

/**
 * 月流向清洗状态
 * @author fucheng.bai
 * @date 2023/3/4
 */
public enum FlowDataWashStatusEnum {
    NOT_WASH(1, "未清洗"),
    NORMAL(2, "正常"),
    DUPLICATE(3, "疑似重复"),
    DELETE(4, "区间外删除"),
    ;

    private Integer code;
    private String desc;

    FlowDataWashStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
