package com.yiling.open.erp.enums;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/5/8
 */
public enum OperTypeEnum {
    ALL(0, "全量"),
    ADD(1, "添加"),
    UPDATE(2, "修改"),
    DELETE(3, "删除");

    private Integer code;
    private String text;

    OperTypeEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    public static OperTypeEnum getFromCode(Integer code) {
        for(OperTypeEnum e: OperTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
