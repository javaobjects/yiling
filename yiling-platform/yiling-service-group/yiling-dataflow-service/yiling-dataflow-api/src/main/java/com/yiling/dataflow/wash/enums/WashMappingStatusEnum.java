package com.yiling.dataflow.wash.enums;

/**
 * 月流向对照状态
 * @author fucheng.bai
 * @date 2023/3/6
 */
public enum WashMappingStatusEnum {
    BOTH_MISMATCH(1, "两者未匹配"),
    GOODS_MISMATCH(2, "商品未匹配"),
    CUSTOM_MISMATCH(3, "客户未匹配"),
    MATCH_SUCCESS(4, "匹配成功"),
    ;

    private Integer code;
    private String desc;

    WashMappingStatusEnum(Integer code, String desc) {
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
