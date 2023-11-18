package com.yiling.hmc.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.OrderRelateUserDTO;
import com.yiling.hmc.order.entity.OrderRelateUserDO;
import com.yiling.hmc.order.enums.HmcOrderRelateUserTypeEnum;
import com.yiling.hmc.wechat.dto.request.SaveOrderRelateUserRequest;

/**
 * <p>
 * 订单相关人表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-27
 */
public interface OrderRelateUserService extends BaseService<OrderRelateUserDO> {

    /**
     * 保存订单相关人
     *
     * @param request
     * @return
     */
    Long add(SaveOrderRelateUserRequest request);

    /**
     * 查询关联用户
     *
     * @param orderId 订单id
     * @param userTypeEnum 联系人类型
     * @return 订单相关人信息
     */
    OrderRelateUserDTO queryByOrderIdAndRelateType(Long orderId, HmcOrderRelateUserTypeEnum userTypeEnum);

}
