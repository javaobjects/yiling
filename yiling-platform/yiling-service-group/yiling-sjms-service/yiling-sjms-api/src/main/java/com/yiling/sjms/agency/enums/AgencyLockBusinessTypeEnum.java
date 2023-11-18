package com.yiling.sjms.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型：1-机构锁定申请 2-扩展信息修改申请
 *
 * @author dexi.yao
 * @date 2023/02/28
 */
@Getter
@AllArgsConstructor
public enum AgencyLockBusinessTypeEnum {

    //机构锁定申请
    LOCK(1,"机构锁定申请"),
    //扩展信息修改申请
    UPDATE(2,"扩展信息修改申请"),
    ;
    private Integer code;
    private String name;


    public static AgencyLockBusinessTypeEnum getByCode(Integer code) {
        for (AgencyLockBusinessTypeEnum e : AgencyLockBusinessTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
