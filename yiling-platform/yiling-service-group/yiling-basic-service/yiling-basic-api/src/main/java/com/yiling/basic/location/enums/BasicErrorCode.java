package com.yiling.basic.location.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础服务模块错误码枚举
 *
 * @author: xuan.zhou
 * @date: 2021/5/31
 */
@Getter
@AllArgsConstructor
public enum BasicErrorCode implements IErrorCode {

    LOCATION_CODE_ERROR(110101, "省市区编码存在错误"),
    DICT_TYPE_REPEAT(110108, "字典表类型名称一样"),
    DICT_DATA_REPEAT(110109, "字典表内容名称一样"),
    ;

    private Integer code;
    private String message;
}
