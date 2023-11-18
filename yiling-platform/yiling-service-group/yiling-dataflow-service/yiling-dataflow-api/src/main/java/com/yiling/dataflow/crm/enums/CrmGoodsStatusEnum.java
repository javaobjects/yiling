package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 CrmGoodsStatusEnum
 * @描述
 * @创建时间 2023/4/12
 * @修改人 shichen
 * @修改时间 2023/4/12
 **/
@Getter
@AllArgsConstructor
public enum CrmGoodsStatusEnum {
    NORMAL(0,"正常"),
    INVALID(1,"无效");

    private Integer code;
    private String name;

    public static CrmGoodsStatusEnum getByCode(Integer code) {
        for(CrmGoodsStatusEnum e: CrmGoodsStatusEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
