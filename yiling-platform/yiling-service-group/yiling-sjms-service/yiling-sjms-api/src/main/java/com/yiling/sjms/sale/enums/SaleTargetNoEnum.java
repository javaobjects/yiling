package com.yiling.sjms.sale.enums;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.basic.no.enums.MiddelPartMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SaleTargetNoEnum implements INoEnum {
    sale_target_no(1,"SQ",MiddelPartMode.DATE_YYYY,4),
    ;
    private Integer type;
    private String prefix;
    private MiddelPartMode middelPartMode;
    private Integer randomNum;
    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public MiddelPartMode getMiddelPartMode() {
        return middelPartMode;
    }

    @Override
    public Integer getRandomNum() {
        return randomNum;
    }
    public static SaleTargetNoEnum getByCode(Integer type) {
        for (SaleTargetNoEnum e : SaleTargetNoEnum.values()) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
