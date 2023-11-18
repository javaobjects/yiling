package com.yiling.basic.location.enums;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * IP归属地来源枚举
 *
 * @author: xuan.zhou
 * @date: 2022/10/19
 */
@Getter
@AllArgsConstructor
public enum IPLocationDataSourceEnum {

    IP2REGION("ip2region", "ip2region"),
    BAIDU("baidu", "百度"),
    ;

    private String code;
    private String name;
}
