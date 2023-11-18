package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2022/10/17
 */
@Getter
@AllArgsConstructor
public enum ClientFlowModeEnum {
    // 0-未设置 1-工具 2-ftp 3-第三方接口 4-以岭平台接口
    DEFAULT(0,"未设置"),
    TOOLS(1,"工具"),
    FTP(2,"ftp"),
    THIRD(3,"第三方接口"),
    INTERFACE(4,"以岭平台接口"),
    MANUAL(10,"手工"),
    ;

    private Integer code;
    private String desc;

    public static ClientFlowModeEnum getFromCode(Integer code) {
        for(ClientFlowModeEnum e: ClientFlowModeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
