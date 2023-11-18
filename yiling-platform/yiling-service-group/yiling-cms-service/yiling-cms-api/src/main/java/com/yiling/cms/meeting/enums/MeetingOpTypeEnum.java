package com.yiling.cms.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议操作类型枚举
 *
 * @author: lun.yu
 * @date: 2022-06-06
 */
@Getter
@AllArgsConstructor
public enum MeetingOpTypeEnum {

    /**
     * 操作类型：1-发布 2-取消发布 3-删除
     */
    RELEASE(1, "发布"),
    CANCEL_RELEASE(2, "取消发布"),
    DELETE(3, "删除"),
    ;

    private final Integer code;
    private final String name;

    public static MeetingOpTypeEnum getByCode(Integer code) {
        for (MeetingOpTypeEnum e : MeetingOpTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
