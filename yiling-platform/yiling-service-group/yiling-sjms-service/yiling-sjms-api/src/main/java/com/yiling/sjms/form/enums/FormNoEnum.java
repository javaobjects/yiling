package com.yiling.sjms.form.enums;

import com.yiling.basic.no.enums.INoEnum;
import com.yiling.basic.no.enums.MiddelPartMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FormNoEnum  implements INoEnum {
    EXTEND_INFO_CHANGE(6,"EC",MiddelPartMode.DATESTR,6),
    ENTERPRISE_ADD(8,"EA",MiddelPartMode.DATESTR,6),
    ENTERPRISE_UPDATE(3,"EU",MiddelPartMode.DATESTR,6),
    ENTERPRISE_LOCK(4,"EL",MiddelPartMode.DATESTR,6),
    ENTERPRISE_UNLOCK(5,"EN",MiddelPartMode.DATESTR,6),
    ENTERPRISE_REL_SHIP_CHANGE(7,"ER",MiddelPartMode.DATESTR,6),
    GOODS_FLEEING(9,"GF",MiddelPartMode.DATESTR,6),
    SALES_APPEAL(10,"SA",MiddelPartMode.DATESTR,6),
    FEE_APPLICATION(11,"FA",MiddelPartMode.DATESTR,6),
    HOSPITAL_MANOR_CHANGE(12,"HM",MiddelPartMode.DATESTR,6),
    HOSPITAL_DRUGSTORE_RELATION_ADD(13, "HD", MiddelPartMode.DATESTR,6),
    FIX_MONTH_FLOW(14,"FM",MiddelPartMode.DATESTR,6)
    ;
    //注意formType需要和FormTypeEnum code定义一致
    private Integer formType;
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

    public static FormNoEnum getByCode(Integer formType) {
        for (FormNoEnum e : FormNoEnum.values()) {
            if (e.getFormType().equals(formType)) {
                return e;
            }
        }
        return null;
    }
}
