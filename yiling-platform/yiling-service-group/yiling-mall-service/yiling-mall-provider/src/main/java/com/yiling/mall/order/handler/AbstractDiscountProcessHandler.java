package com.yiling.mall.order.handler;

import com.yiling.mall.order.bo.CalOrderDiscountContextBO;

/**
 * 计算订单优惠
 * @author zhigang.guo
 * @date: 2022/9/22
 */
public abstract class AbstractDiscountProcessHandler {

    /**
     * 处理计算订单优惠计算逻辑
     * @param discountContextBo
     * @param handlerChain
     */
    public void handRequest(CalOrderDiscountContextBO discountContextBo, DiscountHandlerChain handlerChain) {

        processDiscount(discountContextBo);

        handlerChain.processDiscount();
    }


    protected abstract void processDiscount(CalOrderDiscountContextBO discountContextBo);

}
