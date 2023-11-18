package com.yiling.dataflow.crm.enums;

import com.yiling.framework.common.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SSO错误信息
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Getter
@AllArgsConstructor
public enum CrmEnterpriseErrorCode implements IErrorCode {

    REPEAT_ERROR_CODE(100002, "数据存在重复"),
    VERIFY_ERROR_CODE(100001, "数据不能为空")
    ;

    private Integer code;
    private String message;
}
