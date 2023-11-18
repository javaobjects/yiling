package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 定额方案类型
 * 1-季度，2-年
 * @Author fan.shen
 * @Date 2022/3/30
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HmcInsuranceBillTypeEnum {

    /**
     * 季度
     */
    QUARTER(1, "季度"),

    /**
     * 年度
     */
    YEAR(2, "年度"),

    ;

    private Integer type;

    private String  name;

    public static HmcInsuranceBillTypeEnum getByType(Integer type) {
        for (HmcInsuranceBillTypeEnum e : HmcInsuranceBillTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

}
