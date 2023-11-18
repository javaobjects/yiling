package com.yiling.sjms.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * '拓展信息类型 1-企业库原有信息 2-锁定机构录入
 *
 * @author dexi.yao
 * @date 2023/02/27
 */
@Getter
@AllArgsConstructor
public enum AgencyLockFormDataTypeEnum {
    /**
     * 企业库原有信息
     */
    BUSINESS_STORE(1, "企业库原有信息"),
    /**
     * 锁定机构录入
     */
    LOCK_INPUT(2, "锁定机构录入"),
    ;
    private Integer code;
    private String name;


    public static AgencyLockFormDataTypeEnum getByCode(Integer code) {
        for (AgencyLockFormDataTypeEnum e : AgencyLockFormDataTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
