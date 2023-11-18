package com.yiling.framework.rocketmq.enums;

/**
 * 消息确认枚举
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
public enum MqAction {

    //消费成功确认消息
    CommitMessage,

    //稍后继续消费
    ReconsumeLater;

    MqAction() {
    }
}
