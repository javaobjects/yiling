package com.yiling.hmc.gzh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC公众号关注状态枚举类
 * @Author fan.shen
 * @Date 2022-09-15
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcGzhSubscribeStatusEnum {

    /**
     * 订阅
     */
    SUBSCRIBE(1, "订阅"),

    /**
     * 取消订阅
     */
    UN_SUBSCRIBE(2, "取消订阅"),

    ;

    private Integer type;

    private String  name;


}
