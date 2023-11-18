package com.yiling.framework.rocketmq.enums;

import lombok.Getter;

/**
 * 延迟消息级别枚举：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
 *
 * @author: xuan.zhou
 * @date: 2020/8/14
 */
@Getter
public enum MqDelayLevel {

    ONE_SECOND(1, "1s"),
    FIVE_SECONDS(2, "5s"),
    TEN_SECONDS(3, "10s"),
    THIRTY_SECONDS(4, "30s"),
    ONE_MINUTE(5, "1m"),
    TWO_MINUTES(6, "2m"),
    THREE_MINUTES(7, "3m"),
    FOUR_MINUTES(8, "4m"),
    FIVE_MINUTES(9, "5m"),
    SIX_MINUTES(10, "6m"),
    SEVEN_MINUTES(11, "7m"),
    EIGHT_MINUTES(12, "8m"),
    NINE_MINUTES(13, "9m"),
    TEN_MINUTES(14, "10m"),
    TWENTY_MINUTES(15, "20m"),
    THIRTY_MINUTES(16, "30m"),
    ONE_HOUR(17, "1h"),
    TWO_HOURS(18, "2h"),
    ;

    private Integer level;
    private String name;

    MqDelayLevel(Integer level, String name) {
        this.level = level;
        this.name = name;
    }

    public static MqDelayLevel getByLevel(Integer level) {
        for (MqDelayLevel e : MqDelayLevel.values()) {
            if (e.getLevel().equals(level)) {
                return e;
            }
        }
        return null;
    }
}
