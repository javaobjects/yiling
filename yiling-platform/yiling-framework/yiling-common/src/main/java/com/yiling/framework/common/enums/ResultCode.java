package com.yiling.framework.common.enums;

import javax.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 枚举了一些常用API操作码
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Getter
@AllArgsConstructor
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),

    PARAM_MISS(HttpServletResponse.SC_BAD_REQUEST, "参数缺失"),
    PARAM_VALID_ERROR(HttpServletResponse.SC_BAD_REQUEST, "参数验证错误"),

    UPLOAD_FILE_FAILED(1010, "文件上传失败"),

    EXCEL_PARSING_ERROR(1020, "Excel文件解析失败"),
    EXCEL_DATA_SAVING_FAILED(1021, "数据保存出错"),

    ;

    private final Integer code;
    private final String message;
}
