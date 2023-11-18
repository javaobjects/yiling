package com.yiling.open.erp.enums;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 采购流向订单来源
 *
 * @author: houjie.sun
 * @date: 2022/3/4
 */
public enum FlowPurchaseOrderSourceEnum {

    POP("1", "POP采购"),
    OTHERS("2", "其它渠道采购");

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    FlowPurchaseOrderSourceEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static FlowPurchaseOrderSourceEnum getByCode(Integer code) {
        for (FlowPurchaseOrderSourceEnum e : FlowPurchaseOrderSourceEnum.values()) {
            if (ObjectUtil.equal(e.getCode(), code)) {
                return e;
            }
        }
        return null;
    }

    public static FlowPurchaseOrderSourceEnum getByName(String name) {
        for (FlowPurchaseOrderSourceEnum e : FlowPurchaseOrderSourceEnum.values()) {
            if (ObjectUtil.equal(e.getName(), name)) {
                return e;
            }
        }
        return null;
    }

}
