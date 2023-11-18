package com.yiling.user.integral.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.IntegralExchangeOrderReceiptInfoDTO;
import com.yiling.user.integral.dto.request.UpdateExpressRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeOrderRequest;
import com.yiling.user.integral.dto.request.UpdateReceiptAddressRequest;

/**
 * 积分兑换订单 API
 *
 * @author: lun.yu
 * @date: 2023-01-11
 */
public interface IntegralExchangeOrderApi {

    /**
     * 积分兑换订单分页列表
     *
     * @param request
     * @return
     */
    Page<IntegralExchangeOrderItemBO> queryListPage(QueryIntegralExchangeOrderPageRequest request);

    /**
     * 兑付
     *
     * @param request
     * @return
     */
    boolean exchange(UpdateIntegralExchangeOrderRequest request);

    /**
     * 根据ID获取兑换订单信息
     *
     * @param id
     * @return
     */
    IntegralExchangeOrderDTO getById(Long id);

    /**
     * 根据订单ID获取真实物品的收货信息
     *
     * @param orderId
     * @return
     */
    IntegralExchangeOrderReceiptInfoDTO getReceiptInfoByOrderId(Long orderId);

    /**
     * 修改收货地址
     *
     * @param request
     * @return
     */
    boolean updateAddress(UpdateReceiptAddressRequest request);

    /**
     * 修改快递信息并立即兑付
     *
     * @param request
     * @return
     */
    boolean atOnceExchange(UpdateExpressRequest request);
}
