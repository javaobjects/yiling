package com.yiling.hmc.wechat.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: yong.zhang
 * @date: 2022/4/26
 */
@Getter
@AllArgsConstructor
public enum SmsMedicineReminderEnum {

    MEDICINE_REMINDER("medicine_reminder", "取药提醒", "{}您好，距离您上次使用<{}>，已经超过一个月，小亿管家提醒您，及时取药哦～"),
    ;

    private String code;
    private String name;
    private String templateContent;
}
