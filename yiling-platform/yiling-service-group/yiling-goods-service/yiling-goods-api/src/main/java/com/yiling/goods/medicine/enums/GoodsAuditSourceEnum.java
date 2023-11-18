package com.yiling.goods.medicine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2021/5/24
 */
@Getter
@AllArgsConstructor
public enum GoodsAuditSourceEnum {
    //pop平台
    POP(1, "POP"),
    //B2B平台
    B2B(2, "B2B"),
    //ERP平台
    ERP(3, "ERP"),
    //医院问诊平台
    INQUIRY(4, "INQUIRY");


    private Integer code;
    private String  name;
}
