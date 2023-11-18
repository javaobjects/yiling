package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: shuang.zhang
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum CustomerErrorEnum {

    SYSTEM_ERROR(1, "系统异常"),
    ADDRESS_ERROR(2, "省市区地址无法匹配"),
    CUSTOMER_TYPE_ERROR(3, "无对应终端类型"),
    NAME_ERROR(4, "无客户名称"),
    LICENSE_NO_ERROR(5, "无统一社会信用代码/医疗机构执业许可证"),
    LICENSE_NO_NOT_MATCH(6, "统一社会信用代码/医疗机构执业许可证不匹配"),
    NOT_MATCH(7, "未匹配到客户信息"),
    CUSTOMER_TYPE_NOT_MATCH(8, "终端类型为工业、商业，无法同步"),
    NOT_FOUND_ENTERPRISE(9,"商业信息不存在"),
    ;

    private Integer code;
    private String  desc;

    public static CustomerErrorEnum getFromCode(Integer code) {
        for (CustomerErrorEnum e : CustomerErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
