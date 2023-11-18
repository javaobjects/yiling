package com.yiling.sjms.flow.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: gxl
 * @date: 2022/1/11
 */
@Getter
@AllArgsConstructor
public enum MonthFlowErrorEnum implements IErrorCode {
    WASH_CONTRO_CLOSED(110000, "流向收集工作未开始/已结束"),
    MONTH_NOT_EQUAL(110001,"一个申请中只能上传相同月份的数据，请检查"),
    DATA_CONTAIN_ILLEGAL(110002,"数据检查未通过，无法提交审核"),
    ;

    private final Integer code;
    private final String message;

    public static MonthFlowErrorEnum getFromCode(Integer code) {
        for (MonthFlowErrorEnum e : MonthFlowErrorEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
