package com.yiling.export.excel.enums;

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
public enum ExcelErrorCodeEnum implements IErrorCode {

    DATA_ERROR(100001,"导入数据插入到数据库错误")
    ;

    private Integer code;
    private String message;
}
