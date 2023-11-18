package com.yiling.cms.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议活动状态枚举
 *
 * @author: lun.yu
 * @date: 2022-06-01
 */
@Getter
@AllArgsConstructor
public enum MeetingStatusEnum {

    /**
     * 状态：1-未发布（未发布、未发布已过期） 2-已发布（进行中、已结束、未开始）
     */
    NOT_RELEASE(1, "未发布"),
    HAVE_RELEASE(2, "已发布"),
    ;

    private final Integer code;
    private final String name;

    public static MeetingStatusEnum getByCode(Integer code) {
        for (MeetingStatusEnum e : MeetingStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
