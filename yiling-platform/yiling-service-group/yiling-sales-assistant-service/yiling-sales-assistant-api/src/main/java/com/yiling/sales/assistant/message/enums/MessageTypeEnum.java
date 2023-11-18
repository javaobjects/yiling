package com.yiling.sales.assistant.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务类型，1-业务进度，2-发布任务，3-促销政策，4-学术下方
 * 
 * @author: yong.zhang
 * @date: 2022/1/14
 */
@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
                             /**
                              * 业务进度
                              */
                             PROGRESS(1, "业务进度"),
                             /**
                              * 发布任务
                              */
                             TASK(2, "发布任务"),
                             /**
                              * 促销政策
                              */
                             PROMOTION(3, "促销政策"),
                             /**
                              * 学术下方
                              */
                             ACADEMIC(4, "学术下方"),;

    private final Integer code;
    private final String  name;

    public static MessageTypeEnum getByCode(Integer code) {
        for (MessageTypeEnum e : MessageTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
