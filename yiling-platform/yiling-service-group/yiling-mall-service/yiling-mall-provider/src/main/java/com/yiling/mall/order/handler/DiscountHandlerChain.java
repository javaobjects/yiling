package com.yiling.mall.order.handler;

import java.util.ArrayList;
import java.util.List;

import com.yiling.mall.order.bo.CalOrderDiscountContextBO;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 订单优惠计算调用链
 *
 * @author zhigang.guo
 * @date: 2022/9/22
 */
public class DiscountHandlerChain {

    public int index = 0;

    private CalOrderDiscountContextBO calOrderDiscountContextBO;

    private List<DiscountProcessHandler> discountProcessHandlerList = new ArrayList<DiscountProcessHandler>();


    public DiscountHandlerChain(CalOrderDiscountContextBO calOrderDiscountContextBO) {

        this.calOrderDiscountContextBO = calOrderDiscountContextBO;
    }

    /**
     * 添加订单优惠计算链
     * @param discountProcessHandler
     * @return
     */
    public DiscountHandlerChain addHandler(DiscountProcessHandler discountProcessHandler) {

        if (discountProcessHandler == null) {

            return null;
        }

        this.discountProcessHandlerList.add(discountProcessHandler);

        return this;
    }

    /**
     * 添加订单优惠计算handler
     * @param discountProcessHandlerList
     * @return
     */
    public DiscountHandlerChain addHandler(List<DiscountProcessHandler> discountProcessHandlerList) {

        if (CollectionUtil.isEmpty(discountProcessHandlerList)) {

            return null;
        }

        this.discountProcessHandlerList.addAll(discountProcessHandlerList);

        return this;
    }


    /**
     * 执行折扣计算
     */
    public void processDiscount() {

        if (index == discountProcessHandlerList.size()) {
            return;
        }

        discountProcessHandlerList.get(index++).handRequest(calOrderDiscountContextBO, this);
    }

}
