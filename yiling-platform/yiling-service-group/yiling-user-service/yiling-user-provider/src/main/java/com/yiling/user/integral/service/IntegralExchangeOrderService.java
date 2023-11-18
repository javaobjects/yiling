package com.yiling.user.integral.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.request.QueryIntegralExchangeOrderPageRequest;
import com.yiling.user.integral.bo.IntegralExchangeOrderItemBO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;
import com.yiling.user.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.user.integral.dto.request.UpdateExpressRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeOrderRequest;
import com.yiling.user.integral.entity.IntegralExchangeOrderDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 积分兑换订单表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface IntegralExchangeOrderService extends BaseService<IntegralExchangeOrderDO> {

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
     * 根据积分兑换商品表ID获取积分兑换订单信息
     *
     * @param request
     * @return key为积分兑换商品表的ID，value为兑换的订单集合
     */
    Map<Long, List<IntegralExchangeOrderDO>> getByExchangeGoodsIdList(QueryIntegralExchangeOrderRequest request);

    /**
     * 修改快递信息并立即兑付
     *
     * @param request
     * @return
     */
    boolean atOnceExchange(UpdateExpressRequest request);
}
