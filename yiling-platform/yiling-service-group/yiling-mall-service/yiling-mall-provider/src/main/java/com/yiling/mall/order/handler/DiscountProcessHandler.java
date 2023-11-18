package com.yiling.mall.order.handler;

import com.yiling.mall.order.bo.CalOrderDiscountContextBO;

/** 处理订单优惠
 * @author zhigang.guo
 * @date: 2022/9/22
 */
@FunctionalInterface
public interface DiscountProcessHandler {


    /**
     *  处理营销折扣计算handler
     * @param discountContextBo 上下文环境
     * @param handlerChain 过滤器链
     */
    void handRequest(CalOrderDiscountContextBO discountContextBo, DiscountHandlerChain handlerChain);

}
