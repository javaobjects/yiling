package com.yiling.cms.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议活动展示状态枚举
 *
 * @author: lun.yu
 * @date: 2022-06-01
 */
@Getter
@AllArgsConstructor
public enum MeetingShowStatusEnum {

    /**
     * 数据库状态：1-未发布（未发布、未发布已过期） 2-已发布（进行中、已结束、未开始）
     * 用户展示状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期
     */
    NOT_RELEASE(1, "未发布"),
    DOING(2, "进行中"),
    FINISHED(3,"已结束"),
    NOT_BEGIN(4,"未开始"),
    NOT_RELEASE_EXPIRED(5, "未发布已过期"),
    ;

    private final Integer code;
    private final String name;

    public static MeetingShowStatusEnum getByCode(Integer code) {
        for (MeetingShowStatusEnum e : MeetingShowStatusEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
