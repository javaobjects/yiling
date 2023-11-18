package com.yiling.marketing.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付方式
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponPayMethodTypeEnum {

    // 3现在不支持
    /**
     * 1-在线支付
     */
    ONLINE(1, "在线支付"),

    /**
     * 2-货到付款
     */
    OFFLINE(2, "货到付款"),

    /**
     * 3-账期
     */
     PAYMENT_DAYS(3, "账期"),
    ;

    private Integer  code;
    private String   name;

    public static CouponPayMethodTypeEnum getByCode(Integer code) {
        for (CouponPayMethodTypeEnum e : CouponPayMethodTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 订单支付方式类型转换
     * 由 PaymentMethodEnum 转换为 CouponPayMethodTypeEnum
     * @param code
     * @return
     */
    public static CouponPayMethodTypeEnum transTypeFromPaymentMethodEnum(Integer code) {
        if (ObjectUtil.isNull(code)) {
            return null;
        }
        if (ObjectUtil.equal(4, code)) {
            return CouponPayMethodTypeEnum.ONLINE;
        }else if(ObjectUtil.equal(1, code)){
            return CouponPayMethodTypeEnum.OFFLINE;
        } else if(ObjectUtil.equal(2, code)){
            return CouponPayMethodTypeEnum.PAYMENT_DAYS;
        }
        return null;
    }

    /**
     * 优惠券支付方式类型转换
     * 由 CouponPayMethodTypeEnum 转换为 PaymentMethodEnum
     * @return
     */
    public static List<Integer> transTypeToPaymentMethodEnum() {
        List<Integer> list = new ArrayList();
        list.add(4);
        list.add(1);
        list.add(2);
        return list;
    }

    //    PaymentMethodEnum  类型转换
    //    OFFLINE(1L, "线下支付"),
    //    PAYMENT_DAYS(2L, "账期"),
    //    PREPAYMENT(3L, "预付款"),
    //    NLINE(4L, "在线支付"),

    /**
     * 是否符合当前的所支持的类型
     * @param code
     * @return
     */
    public static boolean isFitWithNotLimit(Integer code) {
        CouponPayMethodTypeEnum[] values = CouponPayMethodTypeEnum.values();
        List<Integer> list = Arrays.stream(values).map(CouponPayMethodTypeEnum::getCode).distinct().collect(Collectors.toList());
        if (list.contains(code)) {
            return true;
        }
        return false;
    }

    public static String getNameByCodeList(List<Integer> codeList){
        List<String> list = new ArrayList<>();
        Map<Integer, CouponPayMethodTypeEnum> map = Arrays.stream(CouponPayMethodTypeEnum.values()).collect(Collectors.toMap(e -> e.getCode(), e -> e, (v1, v2) -> v1));
        for (Integer code : codeList) {
            CouponPayMethodTypeEnum typeEnum = map.get(code);
            if (ObjectUtil.isNotNull(typeEnum)) {
                list.add(typeEnum.getName());
            }
        }
        if(CollUtil.isNotEmpty(list)){
            return String.join(",", list);
        }
        return null;
    }

}
