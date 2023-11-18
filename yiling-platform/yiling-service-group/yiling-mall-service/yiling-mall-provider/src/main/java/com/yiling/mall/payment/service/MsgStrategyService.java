package com.yiling.mall.payment.service;

import com.yiling.framework.rocketmq.enums.MqAction;

/**
 * 支付回调消息策略
 *
 * @author zhigang.guo
 * @date: 2022/9/15
 */
public interface MsgStrategyService<T,O> {

    /**
     * 获取消息类型
     *
     * @return
     */
    O getMsgTradeType();


    /**
     * 处理支付消息
     *
     * @param t 支付消息
     * @return
     */
    MqAction processMessage(T t);

}
