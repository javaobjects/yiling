package com.yiling.bi.dayunhe.service;

import java.math.BigDecimal;

import com.yiling.bi.dayunhe.dto.request.QueryDayunheSalePageRequest;
import com.yiling.bi.dayunhe.entity.DayunheSaleOrderDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-14
 */
public interface DayunheSaleOrderService extends BaseService<DayunheSaleOrderDO> {
    Integer getFlowSaleExistsCount(QueryDayunheSalePageRequest request);
    BigDecimal getFlowSaleExistsQuantity(QueryDayunheSalePageRequest request);
}
