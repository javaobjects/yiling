package com.yiling.order.order.service;

import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;
import com.yiling.order.order.entity.OrderDeviceInfoDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单-下单设备信息 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-10-20
 */
public interface OrderDeviceInfoService extends BaseService<OrderDeviceInfoDO> {


     /**
     *  保存用户下单设备信息
     * @param deviceInfoRequest
     * @return
     */
     boolean save(CreateOrderDeviceInfoRequest deviceInfoRequest) ;

}
