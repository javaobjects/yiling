package com.yiling.open.erp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * erp监控1小时请求达到阈值，给实施负责人发送短信提醒文案枚举
 *
 * @author: houjie.sun
 * @date: 2022/3/10
 */
@Getter
@AllArgsConstructor
public enum ErpMonitorCountReminderTypeEnum {

    MONITOR_REMINDER("monitor_reminder", "监控达到阈值", "商业公司在{}达到了请求数量阈值，任务编号{}，请及时处理"),
    ;

    private String code;
    private String name;
    private String templateContent;

    public static ErpMonitorCountReminderTypeEnum getByCode(String code) {
        for (ErpMonitorCountReminderTypeEnum e : ErpMonitorCountReminderTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
