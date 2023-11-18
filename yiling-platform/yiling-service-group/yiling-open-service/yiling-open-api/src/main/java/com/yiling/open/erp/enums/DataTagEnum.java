package com.yiling.open.erp.enums;

import java.util.Arrays;

/**
 * @author fucheng.bai
 * @date 2023/2/23
 */
public enum DataTagEnum {

    NORMAL(0, "正常"),
    EXC_ADD(1, "新增"),
    EXC_UPDATE(2, "修改"),
    DELETE(3, "删除");

    private Integer code;
    private String text;

    DataTagEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public static String getTextByCode(Integer code) {
        return Arrays.stream(DataTagEnum.values()).filter(d -> d.getCode().equals(code)).map(DataTagEnum::getText).findAny().orElse("");
    }
}
