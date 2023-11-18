package com.yiling.bi.protocol.enums;

/**
 * @author fucheng.bai
 * @date 2023/1/6
 */
public enum DisplayItemEnum {

    AUXILIARY("辅助陈列 (协议约定)");


    private String value;

    DisplayItemEnum(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }


}
