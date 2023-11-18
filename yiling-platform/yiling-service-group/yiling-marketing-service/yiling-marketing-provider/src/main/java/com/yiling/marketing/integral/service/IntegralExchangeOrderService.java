package com.yiling.marketing.integral.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.marketing.integral.entity.IntegralExchangeOrderDO;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;

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
     * 根据条件查询兑换订单
     *
     * @param request
     * @return
     */
    List<IntegralExchangeOrderDTO> getExchangeOrderByCond(QueryIntegralExchangeOrderRequest request);

}
