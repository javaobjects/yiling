package com.yiling.bi.dayunhe.service.impl;

import java.math.BigDecimal;

import com.yiling.bi.dayunhe.dto.request.QueryDayunheSalePageRequest;
import com.yiling.bi.dayunhe.entity.DayunheSaleOrderDO;
import com.yiling.bi.dayunhe.dao.DayunheSaleOrderMapper;
import com.yiling.bi.dayunhe.service.DayunheSaleOrderService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-10-14
 */
@Service
public class DayunheSaleOrderServiceImpl extends BaseServiceImpl<DayunheSaleOrderMapper, DayunheSaleOrderDO> implements DayunheSaleOrderService {

    @Override
    public Integer getFlowSaleExistsCount(QueryDayunheSalePageRequest request) {
        return this.baseMapper.getFlowSaleExistsCount(request);
    }

    @Override
    public BigDecimal getFlowSaleExistsQuantity(QueryDayunheSalePageRequest request) {
        return this.baseMapper.getFlowSaleExistsQuantity(request);
    }
}
