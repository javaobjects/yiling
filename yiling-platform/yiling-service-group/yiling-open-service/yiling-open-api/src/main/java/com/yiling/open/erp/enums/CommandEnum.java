package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandEnum {
    /**
     * 0执行完成1更新版本2重启客户端
     */
    COMPLETE(0,"执行完成"),
    UPDATE_VERSION(1,"更新版本"),
    RESTART_CLIENT(2,"重启客户端"),
    ;

    private Integer code;
    private String desc;

    public static CommandEnum getFromCode(Integer code) {
        for(CommandEnum e: CommandEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
