package com.yiling.framework.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OSS Bucket 枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@Getter
@AllArgsConstructor
public enum BucketEnum {
    //公有Bucket
    PUBLIC("yl-public", "公有Bucket", true),
    //私有Bucket
    PRIVATE("yl-private-file", "私有Bucket", false),
    ;

    private String code;
    private String name;

    /**
     * 是否为公有 Bucket
     */
    private Boolean publicFlag;

    public static BucketEnum getByCode(String code) {
        for (BucketEnum e : BucketEnum.values()) {
            if (e.getCode().equalsIgnoreCase(code)) {
                return e;
            }
        }
        return null;
    }
}
