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
public enum MemberRefundStatusEnum {

	//未退款
	UN_REFUND(1, "未退款"),
	//已退款
	REFUND(2, "已退款"),
    ;

    private Integer code;
    private String name;

    public static MemberRefundStatusEnum getByCode(Integer code) {
        for (MemberRefundStatusEnum e : MemberRefundStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
