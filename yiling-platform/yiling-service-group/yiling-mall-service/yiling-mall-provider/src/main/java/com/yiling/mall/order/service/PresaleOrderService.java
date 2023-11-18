package com.yiling.mall.order.service;

import com.yiling.mall.order.bo.OrderSubmitBO;
import com.yiling.mall.order.dto.request.PresaleOrderSubmitRequest;
import com.yiling.order.order.enums.PreSalOrderReminderTypeEnum;

/** 预售订单
 * @author zhigang.guo
 * @date: 2022/10/9
 */
public interface PresaleOrderService {


    /**
     * 创建预售订单
     *
     * @param request
     * @return
     */
    OrderSubmitBO preSaleOrderSubmit(PresaleOrderSubmitRequest request);


    /**
     * 预定订单发送短信提醒
     * @param orderNo 订单号
     * @param preSalOrderReminderTypeEnum 短信模板类型
     * @return
     */
     boolean sendPresaleOrderSmsNotice(String orderNo, PreSalOrderReminderTypeEnum preSalOrderReminderTypeEnum) ;

}
