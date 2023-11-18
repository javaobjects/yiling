package com.yiling.sjms.agency.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yong.zhang
 * @date: 2023/3/1 0001
 */
@Getter
@AllArgsConstructor
public enum AgencyFormErrorCode implements IErrorCode {

    /**
     * 当前机构列表中存在重复的机构
     */
    AGENCY_REPEAT(170001, "当前机构列表中存在重复的机构"),
    /**
     * 表单数据不存在
     */
    FORM_NOT_FIND(170002, "表单数据不存在"),
    /**
     * 机构表单数据不存在
     */
    AGENCY_NOT_FIND(170003, "机构表单数据不存在"),
    /**
     * 当前状态不允许归档
     */
    STATUS_NOT_ALLOW_ARCHIVE(170004, "当前状态不允许归档"),
    ;
    private final Integer code;
    private final String message;
}
