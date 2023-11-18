package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 同步状态：1-待同步 2-同步失败 3-同步成功
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportSyncTaskStatusEnum {

    //待同步
    UN_SYNC(1, "待同步"),
    //同步失败
    FAIL(2, "同步失败"),
    //同步成功
    SUCCESS(3, "同步成功"),
    ;
    private Integer code;
    private String name;

    public static ReportSyncTaskStatusEnum getByCode(Integer code) {
        for (ReportSyncTaskStatusEnum e : ReportSyncTaskStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
