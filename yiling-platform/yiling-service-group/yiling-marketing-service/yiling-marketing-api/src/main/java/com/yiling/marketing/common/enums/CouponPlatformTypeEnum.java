package com.yiling.marketing.common.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yiling.framework.common.enums.PlatformEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 平台限制类型选择值
 *
 * @author: houjie.sun
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum CouponPlatformTypeEnum {

    // PlatformEnum  类型转换
    /**
     * 1-B2B
     */
    B2B(1, "B2B"),
    /**
     * 2-销售助手
     */
    SALES_ASSIST(2, "销售助手"),;

    private Integer code;
    private String  name;

    public static CouponPlatformTypeEnum getByCode(Integer code) {
        for (CouponPlatformTypeEnum e : CouponPlatformTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    /**
     * 平台类型转换转换
     * 由 PlatformEnum 转换为 CouponPlatformTypeEnum
     * @param code
     * @return
     */
    public static CouponPlatformTypeEnum transTypeFromPlatformEnum(Integer code) {
        if (ObjectUtil.equal(PlatformEnum.B2B.getCode(), code)) {
            return CouponPlatformTypeEnum.B2B;
        } else if (ObjectUtil.equal(PlatformEnum.SALES_ASSIST.getCode(), code)) {
            return CouponPlatformTypeEnum.SALES_ASSIST;
        }
        return null;
    }

    /**
     * 是否符合当前的所支持的类型
     * @param code
     * @return
     */
    public static boolean isFitWithNotLimit(Integer code) {
        CouponPlatformTypeEnum[] values = CouponPlatformTypeEnum.values();
        List<Integer> list = Arrays.stream(values).map(CouponPlatformTypeEnum::getCode).distinct().collect(Collectors.toList());
        if (list.contains(code)) {
            return true;
        }
        return false;
    }

    public static String getNameByCodeList(List<Integer> codeList){
        List<String> list = new ArrayList<>();
        Map<Integer, CouponPlatformTypeEnum> map = Arrays.stream(CouponPlatformTypeEnum.values()).collect(Collectors.toMap(e -> e.getCode(), e -> e, (v1, v2) -> v1));
        for (Integer code : codeList) {
            CouponPlatformTypeEnum typeEnum = map.get(code);
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
