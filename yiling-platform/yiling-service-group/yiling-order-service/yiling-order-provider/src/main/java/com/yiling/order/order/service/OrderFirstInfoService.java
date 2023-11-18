package com.yiling.order.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderFirstInfoDO;

/**
 * <p>
 * 首单信息表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-05-30
 */
public interface OrderFirstInfoService extends BaseService<OrderFirstInfoDO> {

    /**
     * 查询用户首单信息
     * @param buyerEid 企业EId
     * @param orderType 订单类型
     * @return
     */
    OrderFirstInfoDO queryOrderFirstInfo(Long buyerEid, Integer orderType);

    /**
     * 保存首单用户信息
     * @param orderId
     * @param opUserId
     * @return
     */
    Boolean saveFirstInfo(Long orderId,Long opUserId);
}
