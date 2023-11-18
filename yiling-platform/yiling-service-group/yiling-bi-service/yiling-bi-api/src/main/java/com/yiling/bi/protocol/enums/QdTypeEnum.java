package com.yiling.bi.protocol.enums;

import java.util.Arrays;

/**
 * @author fucheng.bai
 * @date 2023/1/5
 */
public enum QdTypeEnum {

    ONLY_UNIFY("1", "统谈统签"),
    UNIFY_AND_SEPARATE("2", "统谈分签"),
    ONLY_ALONE("3", "单谈单签"),
    ;


    QdTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static String getKey(String value) {
        return Arrays.stream(values()).filter(q -> q.getValue().equals(value)).findAny().map(QdTypeEnum::getKey).orElse(null);
    }

}
