package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * erp封存流向类型 枚举类
 *
 * @author: houjie.sun
 * @date: 2022/4/18
 */
@Getter
@AllArgsConstructor
public enum ErpFlowSealedTypeEnum {

    PURCHASE(1,"采购流向"),
    SALE(2,"销售流向"),
    SHOP_SALE(3,"连锁纯销流向"),
    ;

    private Integer code;
    private String desc;

    public static ErpFlowSealedTypeEnum getFromCode(Integer code) {
        for(ErpFlowSealedTypeEnum e: ErpFlowSealedTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
