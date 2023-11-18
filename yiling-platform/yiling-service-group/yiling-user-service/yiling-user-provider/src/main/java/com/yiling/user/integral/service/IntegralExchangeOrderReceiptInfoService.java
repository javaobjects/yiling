package com.yiling.user.integral.service;

import java.util.List;
import java.util.Map;

import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.UpdateReceiptAddressRequest;
import com.yiling.user.integral.entity.IntegralExchangeOrderReceiptInfoDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分兑换订单收货信息表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface IntegralExchangeOrderReceiptInfoService extends BaseService<IntegralExchangeOrderReceiptInfoDO> {

    /**
     * 根据兑换订单ID获取收货信息
     *
     * @param exchangeOrderId
     * @return
     */
    IntegralExchangeOrderReceiptInfoDTO getByExchangeOrderId(Long exchangeOrderId);

    /**
     * 根据兑换订单ID批量获取收货信息
     *
     * @param exchangeOrderIdList
     * @return
     */
    Map<Long, IntegralExchangeOrderReceiptInfoDTO> getByExchangeOrderIdList(List<Long> exchangeOrderIdList);

    /**
     * 修改收货地址
     *
     * @param request
     * @return
     */
    boolean updateAddress(UpdateReceiptAddressRequest request);
}
