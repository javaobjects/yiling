package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 医院性质 1公立 2私立 3厂办
 *
 * @author: shixing.sun
 * @date: 2023/2/17
 */
@Getter
@AllArgsConstructor
public enum CrmHosptialAttributeEnum {


    SYNTHESIZE(1, "综合医院"),

    MAJOR(2, "专科医院"),

    ARMY(3, "部队医院"), CHILDREN(4, "儿童医院"), REGION(5, "县级医院"), FACTORY(6, "厂矿职工医院"), MEDIA(7, "中医医院"), SHEQU(8, "社区卫生服务站"), COUNTRY(9, "乡镇卫生院"), CUN(10, "村卫生室"), FUYOU(11, "妇保院"), JIKONG(12, "疾控中心"), SIREN(13, "私人终端"), SHQUWEISHENG(14, "社区卫生服务中心"), GAOXIAO(15, "高校医院"), OTHER(16, "其他"),
    ;

    private final Integer code;
    private final String name;

    public static CrmHosptialAttributeEnum getByCode(Integer code) {
        for (CrmHosptialAttributeEnum e : CrmHosptialAttributeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
