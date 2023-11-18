package com.yiling.dataflow.crm.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 CrmGoodsErrorCode
 * @描述
 * @创建时间 2023/4/6
 * @修改人 shichen
 * @修改时间 2023/4/6
 **/
@Getter
@AllArgsConstructor
public enum CrmGoodsErrorCode implements IErrorCode {

    REPEAT_ERROR(100002, "数据重复"),
    EMPTY_ERROR(100001, "数据为空"),
    DATA_ERROR(100003,"数据错误")
    ;

    private Integer code;
    private String message;
}
