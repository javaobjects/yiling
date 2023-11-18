package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 CrmGoodsTagTypeEnum
 * @描述
 * @创建时间 2023/5/26
 * @修改人 shichen
 * @修改时间 2023/5/26
 **/
@Getter
@AllArgsConstructor
public enum CrmGoodsTagTypeEnum {

    NOT_LOCK(1,"非锁标签"),
    GROUP_PURCHASE(2,"团购标签");

    private Integer type;
    private String name;

    public static CrmGoodsTagTypeEnum getByType(Integer type) {
        for(CrmGoodsTagTypeEnum e: CrmGoodsTagTypeEnum.values()) {
            if(e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }
}
