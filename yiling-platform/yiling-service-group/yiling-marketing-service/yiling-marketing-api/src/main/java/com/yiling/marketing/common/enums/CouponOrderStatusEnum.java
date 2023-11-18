package com.yiling.marketing.common.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: houjie.sun
 * @date: 2021/11/5
 */
@Getter
@AllArgsConstructor
public enum CouponOrderStatusEnum {

    // OrderStatusEnum 类型转换
    // 订单状态：-10-已取消, 10-待审核, 20-待发货, 25-部分发货, 30-已发货, 40-已收货, 100-已完成
    /**
     * 1-已发货
     */
    DELIVERED(30, "已发货"),;

    private Integer code;
    private String  name;

    public static CouponOrderStatusEnum getByCode(Integer code) {
        for (CouponOrderStatusEnum e : CouponOrderStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 是否符合当前的所支持的类型
     * @param code
     * @return
     */
    public static boolean isFitWithNotLimit(Integer code) {
        CouponOrderStatusEnum[] values = CouponOrderStatusEnum.values();
        List<Integer> list = Arrays.stream(values).map(CouponOrderStatusEnum::getCode).distinct().collect(Collectors.toList());
        if (list.contains(code)) {
            return true;
        }
        return false;
    }
}
