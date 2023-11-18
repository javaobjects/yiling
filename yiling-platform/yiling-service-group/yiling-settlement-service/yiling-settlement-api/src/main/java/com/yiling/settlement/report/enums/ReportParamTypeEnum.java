package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数类型(参数类型：1-商品类型 2-促销活动 3-阶梯规则 4-会员返利)
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportParamTypeEnum {

	//商品类型
	GOODS(1, "商品类型"),
	//促销活动
	ACTIVITY(2, "促销活动"),
	//阶梯规则
	LADDER(3, "阶梯规则"),
	//会员返利
	MEMBER(4, "会员返利"),
    ;

    private Integer code;
    private String name;

    public static ReportParamTypeEnum getByCode(Integer code) {
        for (ReportParamTypeEnum e : ReportParamTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
