package com.yiling.dataflow.sale.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态 1-未配置 2-已配置 3-配置中
 *
 * @author: dexi.yao
 * @date: 2022/12/27
 */
@Getter
@AllArgsConstructor
public enum CrmSaleDepartmentTargetConfigStatusEnum {

    //未配置
    UN_SPLIT(1, "未配置"),
    //已配置
    COMPLETE_SPLIT(2, "已配置"),
    //配置中
    COPE_SPLIT(3, "配置中"),
    //等待生成模版
    WAIT_SPLIT(4, "等待生成模版"),
    //模板生成失败
    FAIL(5, "配置失败"),
    ;

    private Integer code;
    private String desc;

    public static CrmSaleDepartmentTargetConfigStatusEnum getFromCode(Integer code) {
        for (CrmSaleDepartmentTargetConfigStatusEnum e : CrmSaleDepartmentTargetConfigStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static Map<Integer, String> getCodeMap() {
        Map<Integer, String> flowGoodsRelationLabelMap = Arrays.stream(CrmSaleDepartmentTargetConfigStatusEnum.values())
                .collect(Collectors.toMap(o -> o.getCode(), o -> o.getDesc(), (k1, k2) -> k1));
        return flowGoodsRelationLabelMap;
    }

    public static List<Integer> getCodeList() {
        return Arrays.stream(CrmSaleDepartmentTargetConfigStatusEnum.values()).map(CrmSaleDepartmentTargetConfigStatusEnum::getCode).distinct().collect(Collectors.toList());
    }

}
