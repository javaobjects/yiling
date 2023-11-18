package com.yiling.hmc.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.dao.MarketOrderDetailMapper;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.entity.MarketOrderDetailDO;
import com.yiling.hmc.order.service.MarketOrderDetailService;

/**
 * <p>
 * HMC订单明细表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Service
public class MarketOrderDetailServiceImpl extends BaseServiceImpl<MarketOrderDetailMapper, MarketOrderDetailDO> implements MarketOrderDetailService {

    @Override
    public List<MarketOrderDetailDTO> queryByOrderIdList(List<Long> orderIds) {
        LambdaQueryWrapper<MarketOrderDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MarketOrderDetailDO::getOrderId, orderIds);
        return PojoUtils.map(this.list(wrapper), MarketOrderDetailDTO.class);
    }

    @Override
    public List<MarketOrderDetailDTO> queryByGoodsNameList(String goodsName) {
        LambdaQueryWrapper<MarketOrderDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(MarketOrderDetailDO::getGoodsName, goodsName);
        List<MarketOrderDetailDO> list = this.list(wrapper);
        return PojoUtils.map(list, MarketOrderDetailDTO.class);
    }
}
