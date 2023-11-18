package com.yiling.dataflow.crm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author fucheng.bai
 * @date 2023/5/31
 */
@Getter
@AllArgsConstructor
public enum CrmDrugstoreRelStatusEnum {

    DISABLE(1, "已停用"),
    NOT_EFFECT(2, "未生效"),
    EFFECTING(3, "生效中"),
    EXPIRED(4, "已过期"),
    ;

    private Integer code;
    private String name;
}
