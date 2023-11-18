package com.yiling.open.erp.enums;

import com.yiling.open.erp.util.OpenConstants;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 库存流向订单来源
 *
 * @author: houjie.sun
 * @date: 2022/3/4
 */
public enum FlowGoodsBatchOrderSourceEnum {

    POP("1", "POP库存"),
    OTHERS("2", "其它渠道库存");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FlowGoodsBatchOrderSourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FlowGoodsBatchOrderSourceEnum getByCode(Integer code) {
        for (FlowGoodsBatchOrderSourceEnum e : FlowGoodsBatchOrderSourceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static FlowGoodsBatchOrderSourceEnum getByName(String name) {
        for (FlowGoodsBatchOrderSourceEnum e : FlowGoodsBatchOrderSourceEnum.values()) {
            if (e.getName().equals(name)) {
                return e;
            }
        }
        return null;
    }

}
