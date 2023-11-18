package com.yiling.hmc.gzh.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * HMC公众号欢迎语发布状态枚举类
 * @Author fan.shen
 * @Date 2022-09-15
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum GzhGreetingPublishStatusEnum {

    /**
     * 已发布
     */
    PUBLISHED(1, "已发布"),

    /**
     * 未发布
     */
    UN_PUBLISHED(2, "未发布"),

    ;

    private Integer type;

    private String  name;


}
