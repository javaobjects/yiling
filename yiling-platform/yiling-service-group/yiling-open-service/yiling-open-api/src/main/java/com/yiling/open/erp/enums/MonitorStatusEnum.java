package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 监控状态类型
 *
 * @author: houjie.sun
 * @date: 2022/1/18
 */
@Getter
@AllArgsConstructor
public enum MonitorStatusEnum {

    OFF(0,"关闭"),
    ON(1,"开启")
    ;

    private Integer code;
    private String message;

    public static MonitorStatusEnum getByCode(Integer code) {
        for (MonitorStatusEnum e : MonitorStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
