package com.yiling.cms.common;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 内容模块错误码枚举
 *
 * @author: lun.yu
 * @date: 2022-06-02
 */
@Getter
@AllArgsConstructor
public enum CmsErrorCode implements IErrorCode {

    /**
     * 会议管理模块 2001开头
     */
    MEETING_NOT_EXIST(200101, "会议不存在"),
    MEETING_STATUS_EDIT_ERROR(200102, "当前会议状态不可编辑"),
    MEETING_STATUS_DELETE_ERROR(200103, "当前会议状态不可删除"),
    MEETING_TIME_ERROR(200104, "会议活动时间错误"),
    MEETING_STATUS_ERROR(200105, "当前会议状态不正确"),
    MEETING_HAVE_CANCEL(200106, "会议已取消"),

    /**
     * 健康测评模块 3001开头
     */
    HEALTH_EVALUATE_RESULT_ERROR(300101, "测评题目必填"),
    ;

    private final Integer code;
    private final String message;
}
