package com.yiling.goods.standard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/5/26
 */
@Getter
@AllArgsConstructor
public enum StandardGoodsStatusEnum {
    //正常
    NORMAL(0, "正常"),
    //禁止
    FORBID(1, "禁止");

    private Integer code;
    private String  name;
}
