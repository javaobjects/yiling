package com.yiling.dataflow.crm.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shuan
 */

@Getter
@AllArgsConstructor
public enum CrmEnterpriseEnameLevelEnum {

    FIRST_LEVEL(1,"一级经销商"),
    SECOND_LEVEL(2,"二级经销商"),
    CHAIN_BUSINESS(3,"连锁商业"),
    UNRATED_BUSINESS(4,"未分级经销商"),
    CLOUD_BUSINESS(5,"云仓商业"),
    STANDARD_FIRST_LEVEL(6,"准一级经销商")
    ;

    private Integer code;
    private String name;

    public static CrmEnterpriseEnameLevelEnum getFromCode(Integer code) {
        for(CrmEnterpriseEnameLevelEnum e: CrmEnterpriseEnameLevelEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static CrmEnterpriseEnameLevelEnum getFromName(String name) {
        for(CrmEnterpriseEnameLevelEnum e: CrmEnterpriseEnameLevelEnum.values()) {
            if(ObjectUtil.equal(e.getName(), name)) {
                return e;
            }
        }
        return null;
    }
}
