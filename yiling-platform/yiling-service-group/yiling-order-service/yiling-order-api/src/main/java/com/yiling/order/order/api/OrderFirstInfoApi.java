package com.yiling.order.order.api;

import com.yiling.order.order.dto.OrderFirstInfoDTO;
import com.yiling.order.order.enums.OrderTypeEnum;

/** 用户首单信息api
 * @author zhigang.guo
 * @date: 2022/5/30
 */
public interface OrderFirstInfoApi {

    /**
     * 通过首单记录校验客户是否为新客
     * @param buyerEid 企业Eid
     * @param orderTypeEnum 订单类型
     * @return
     */
    Boolean checkNewVisitor(Long buyerEid, OrderTypeEnum orderTypeEnum);

    OrderFirstInfoDTO queryOrderFirstInfo(Long buyerEid, OrderTypeEnum orderTypeEnum);

    /**
     * 保存用户首单信息
     * @param orderId 用户ID
     * @param opUserId 创建人
     * @return
     */
    Boolean saveFirstInfo(Long orderId,Long opUserId);
}
