package com.yiling.sjms.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁定类型 1-打单 2-销售
 *
 * @author dexi.yao
 * @date 2023/02/27
 */
@Getter
@AllArgsConstructor
public enum AgencyLocTypeEnum {
    /**
     * 打单
     */
    make_out(1, "打单"),
    /**
     * 销售
     */
    SALE(2, "销售"),
    ;
    private Integer code;
    private String name;


    public static AgencyLocTypeEnum getByCode(Integer code) {
        for (AgencyLocTypeEnum e : AgencyLocTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
