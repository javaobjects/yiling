package com.yiling.sales.assistant.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息角色：1-对用户 2-对企业
 *
 * @author: yong.zhang
 * @date: 2021/9/18
 */
@Getter
@AllArgsConstructor
public enum MessageRoleEnum {
    TO_USER(1, "对用户"),
    TO_ENTERPRISE(2, "对企业"),
    ;
    private Integer code;
    private String  name;
}
