package com.yiling.order.order.api;

import com.yiling.order.order.dto.request.CreateOrderDeviceInfoRequest;

/** 用户下单设备信息Api
 * @author zhigang.guo
 * @date: 2022/10/20
 */
public interface OrderDeviceInfoApi {

    /**
     * 保存下单设备信息
     * @param deviceInfoRequest
     * @return
     */
    boolean save(CreateOrderDeviceInfoRequest deviceInfoRequest);

}
