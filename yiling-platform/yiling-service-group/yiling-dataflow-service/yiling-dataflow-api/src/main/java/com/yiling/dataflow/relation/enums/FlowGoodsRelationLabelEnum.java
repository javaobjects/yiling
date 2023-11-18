package com.yiling.dataflow.relation.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品关系标签枚举类
 *
 * @author: houjie.sun
 * @date: 2022/12/27
 */
@Getter
@AllArgsConstructor
public enum FlowGoodsRelationLabelEnum {

    EMPTY(0, "无标签"),
    YILING(1, "以岭品"),
    OTHER(2, "非以岭品"),
    CHINESE_MEDICINE(3, "中药饮片"),
    ;

    private Integer code;
    private String desc;

    public static FlowGoodsRelationLabelEnum getFromCode(Integer code) {
        for (FlowGoodsRelationLabelEnum e : FlowGoodsRelationLabelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static Map<Integer, String> getCodeMap() {
        Map<Integer, String> flowGoodsRelationLabelMap = Arrays.stream(FlowGoodsRelationLabelEnum.values())
                .collect(Collectors.toMap(o -> o.getCode(), o -> o.getDesc(), (k1, k2) -> k1));
        return flowGoodsRelationLabelMap;
    }

    public static List<Integer> getCodeList() {
        return Arrays.stream(FlowGoodsRelationLabelEnum.values()).map(FlowGoodsRelationLabelEnum::getCode).distinct().collect(Collectors.toList());
    }

}
