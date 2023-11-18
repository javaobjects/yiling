package com.yiling.dataflow.crm.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 辖区医院重复
 *
 */
@Getter
@AllArgsConstructor
public enum CrmEnterpriseRelationManorErrorCode implements IErrorCode {

    REPEAT_ERROR_CODE(100002, "数据存在重复"),
    VERIFY_ERROR_CODE(100001, "数据不能为空")
    ;

    private Integer code;
    private String message;
}
