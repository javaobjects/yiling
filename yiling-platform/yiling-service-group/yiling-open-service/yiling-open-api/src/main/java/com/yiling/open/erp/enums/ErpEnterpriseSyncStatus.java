package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP对接企业信息同步状态
 *
 * @author: houjie.sun
 * @date: 2022/1/14
 */
@Getter
@AllArgsConstructor
public enum ErpEnterpriseSyncStatus {

    UNSYNC(0,"未开启"),
    SYNCING(1,"已开启"),
    ;

    private Integer code;
    private String message;

    public static ErpEnterpriseSyncStatus getByCode(Integer code) {
        for (ErpEnterpriseSyncStatus e : ErpEnterpriseSyncStatus.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
