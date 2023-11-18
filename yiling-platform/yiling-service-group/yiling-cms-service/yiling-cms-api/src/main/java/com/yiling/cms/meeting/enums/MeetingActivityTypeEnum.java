package com.yiling.cms.meeting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议活动类型枚举
 *
 * @author: lun.yu
 * @date: 2022-06-02
 */
@Getter
@AllArgsConstructor
public enum MeetingActivityTypeEnum {

    /**
     * 活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它
     */
    DEPARTMENT_MEETING(1, "科室会"),
    ROUND_TABLE_MEETING(2, "圆桌会"),
    DISEASE_MEETING(3, "络病大会"),
    PATIENT_EDUCATION_SEMINAR(4, "患者教育讲座"),
    OTHER(5, "其它"),
    ;

    private final Integer code;
    private final String name;

    public static MeetingActivityTypeEnum getByCode(Integer code) {
        for (MeetingActivityTypeEnum e : MeetingActivityTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
