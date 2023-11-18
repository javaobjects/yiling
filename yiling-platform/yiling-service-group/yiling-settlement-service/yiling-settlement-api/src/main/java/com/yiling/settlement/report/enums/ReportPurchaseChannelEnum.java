package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报表类型：购进渠道：1-大运河采购 2-京东采购 3-库存不足
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportPurchaseChannelEnum {

	//大运河采购
	DYH(1, "大运河采购"),
	//京东采购
    JD(2, "京东采购"),
	//库存不足
    SHORTAGE(3, "库存不足"),
    ;

    private Integer code;
    private String name;

    public static ReportPurchaseChannelEnum getByCode(Integer code) {
        for (ReportPurchaseChannelEnum e : ReportPurchaseChannelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
