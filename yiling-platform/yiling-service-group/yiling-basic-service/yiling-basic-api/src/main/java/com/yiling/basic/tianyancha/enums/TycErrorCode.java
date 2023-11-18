package com.yiling.basic.tianyancha.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 TycErrorCode
 * @描述
 * @创建时间 2022/1/11
 * @修改人 shichen
 * @修改时间 2022/1/11
 **/
@Getter
@AllArgsConstructor
public enum TycErrorCode implements IErrorCode {
    OK(0,"天眼查接口请求成功"),
    CLOSE(1000,"天眼查功能已关闭"),
    NO_DATA(300000,"无数据"),
    REQUEST_FAIL(300001,"天眼查接口请求失败"),
    ACCOUNT_INVALID(300002,"账号失效"),
    ACCOUNT_EXPIRE(300003,"账号过期"),
    API_BUSY(300004,"访问频率过快"),
    NO_PERMISSION(300005,"无权限访问此api"),
    NOT_ENOUGH(300006,"余额不足"),
    NOT_QUANTITY(300007,"剩余次数不足"),
    MISS_PARAM(300008,"缺少必要参数"),
    ACCOUNT_INFO_ERROR(300009,"账号信息有误"),
    NOT_FOUND_URL(300010,"URL不存在"),
    IP_NO_PERMISSION(300011,"此IP无权限访问此api"),
    IN_REPORT_GENERATION(300012,"报告生成中"),
    ;

    private Integer code;
    private String message;

    public static TycErrorCode getByCode(Integer code) {
        for (TycErrorCode e : TycErrorCode.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
