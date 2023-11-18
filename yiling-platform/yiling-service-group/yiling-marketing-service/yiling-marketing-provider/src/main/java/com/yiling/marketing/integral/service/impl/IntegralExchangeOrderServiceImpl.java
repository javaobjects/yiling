package com.yiling.marketing.integral.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.dao.IntegralExchangeOrderMapper;
import com.yiling.marketing.integral.dto.request.QueryIntegralExchangeOrderRequest;
import com.yiling.marketing.integral.entity.IntegralExchangeOrderDO;
import com.yiling.marketing.integral.service.IntegralExchangeOrderService;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.integral.dto.IntegralExchangeOrderDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分兑换订单表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class IntegralExchangeOrderServiceImpl extends BaseServiceImpl<IntegralExchangeOrderMapper, IntegralExchangeOrderDO> implements IntegralExchangeOrderService {


    @Override
    public List<IntegralExchangeOrderDTO> getExchangeOrderByCond(QueryIntegralExchangeOrderRequest request) {
        QueryWrapper<IntegralExchangeOrderDO> wrapper = WrapperUtils.getWrapper(request);
        return PojoUtils.map(this.list(wrapper), IntegralExchangeOrderDTO.class);
    }
}
