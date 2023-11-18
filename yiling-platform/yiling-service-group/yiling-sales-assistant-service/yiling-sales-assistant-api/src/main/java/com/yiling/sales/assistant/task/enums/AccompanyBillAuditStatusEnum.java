package com.yiling.sales.assistant.task.enums;

import lombok.Getter;

/**
 * @author: gxl
 * @date: 2023/1/11
 */
@Getter
public enum AccompanyBillAuditStatusEnum {
    //待审核
    TO_AUDIT(6, "待审核"),
    //人工审核通过但未与ERP系统进行流向查询
    ONE_PASS_AUDIT(1, "审核通过"),
    //
    TWO_PASS_AUDIT(2,"人工审核通过+ERP单据编号+CRM流向存在"),
    FAIL_ONE(3,"资料人工审核失败"),
    FAIL_TWO(4," 资料人工审核通过但ERP系统中单据编号不存在"),
    FAIL_THREE(5,"资料人工审核通过且ERP系统中单据存在但CRM无流向数据"),

    ;
    private Integer status;

    private String desc;

    AccompanyBillAuditStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
