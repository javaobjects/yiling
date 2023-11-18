package com.yiling.settlement.report.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常原因：1-打单商业,2-锁定终端,3-疑似商业,4-库存不足,5-其他
 *
 * @author: dexi.yao
 * @date: 2021/10/25
 */
@Getter
@AllArgsConstructor
public enum ReportOrderaAnormalReasonEnum {

	//打单商业
	LIST_BUSINESS(1, "打单商业"),
	//锁定终端
    LOCK_TERMINAL(2, "锁定终端"),
	//疑似商业
    DOUBTFUL_BUSINESS(3, "疑似商业"),
	//库存不足
    STOCK_SHORTAGE(4, "库存不足"),
	//其他
    OTHER(5, "其他"),
    ;

    private Integer code;
    private String name;

    public static ReportOrderaAnormalReasonEnum getByCode(Integer code) {
        for (ReportOrderaAnormalReasonEnum e : ReportOrderaAnormalReasonEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
