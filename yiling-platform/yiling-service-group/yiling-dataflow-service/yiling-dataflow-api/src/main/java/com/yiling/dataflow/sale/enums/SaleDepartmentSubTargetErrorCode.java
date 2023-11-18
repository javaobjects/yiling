package com.yiling.dataflow.sale.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 机构锁定错误码
 *
 * @author dexi.yao
 * @date 2023/02/27
 */
@Getter
@AllArgsConstructor
public enum SaleDepartmentSubTargetErrorCode implements IErrorCode {

	//销售目标金额不匹配
    AMOUNT_ERR(220001, "销售目标金额不匹配"),
    DATA_EMPTY_ERR(220002,"数据不能为空"),
    CONFIG_STATUS_ERR(220003,"配置数据已经在配置中请稍后修改"),
    ;

    private final Integer code;
    private final String  message;
}
