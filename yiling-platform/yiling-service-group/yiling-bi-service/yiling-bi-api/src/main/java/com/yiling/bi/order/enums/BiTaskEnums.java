package com.yiling.bi.order.enums;

/**
 * @author fucheng.bai
 * @date 2022/9/19
 */
public enum BiTaskEnums {

    BI_ORDER_BACKUP("10000001", "bi_order_backup", "备份ods订单数据");

    private String code;
    private String name;
    private String msg;

    BiTaskEnums(String code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
}
