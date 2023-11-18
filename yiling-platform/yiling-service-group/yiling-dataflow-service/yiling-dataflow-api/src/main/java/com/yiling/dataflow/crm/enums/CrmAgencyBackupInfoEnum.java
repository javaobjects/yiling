package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CrmAgencyBackupInfoEnum {
    AGENCY(1,"机构档案相关"),
    AGENCY_ORG(2,"组织关系"),
    ;

    private Integer code;
    private String name;

    public static String getFromCode(Integer code) {
        for(CrmAgencyBackupInfoEnum e: CrmAgencyBackupInfoEnum.values()) {
            if(e.getCode().equals(code)) {
                return e.getName();
            }
        }
        return null;
    }
}
