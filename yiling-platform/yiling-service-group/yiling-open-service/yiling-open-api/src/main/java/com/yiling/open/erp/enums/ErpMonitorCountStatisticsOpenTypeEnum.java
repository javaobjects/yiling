package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ERP监控查询页签类型
 *
 * @author: houjie.sun
 * @date: 2022/10/20
 */
@Getter
@AllArgsConstructor
public enum ErpMonitorCountStatisticsOpenTypeEnum {

    ALL_QUERY(0,"不限制条件，查询全部相应数据"),
    ERP_CLIENT_STATUS(1,"关闭对接企业"),
    ERP_HART_BEAT(2,"24小时无心跳企业"),
    NO_FLOW_SALE(3,"当月无销售企业"),
    EXCEPTION_FLOW_SALE(4,"销售异常数据"),
    EXCEPTION_FLOW_PURCHASE(5,"采购异常数据"),
    ;

    private Integer code;
    private String desc;

    public static ErpMonitorCountStatisticsOpenTypeEnum getFromCode(Integer code) {
        for(ErpMonitorCountStatisticsOpenTypeEnum e: ErpMonitorCountStatisticsOpenTypeEnum.values()) {
            if(e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
