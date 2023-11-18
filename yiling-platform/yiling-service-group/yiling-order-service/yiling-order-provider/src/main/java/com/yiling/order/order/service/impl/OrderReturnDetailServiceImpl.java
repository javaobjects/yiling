package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderReturnDetailMapper;
import com.yiling.order.order.entity.OrderReturnDetailDO;
import com.yiling.order.order.service.OrderReturnDetailService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 退货单明细 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Slf4j
@Service
public class OrderReturnDetailServiceImpl extends BaseServiceImpl<OrderReturnDetailMapper, OrderReturnDetailDO> implements OrderReturnDetailService {

    /**
     * 根据退货单id批量获取明细
     *
     * @param returnIds 退货id
     * @return 退货单明细信息
     */
    @Override
    public List<OrderReturnDetailDO> getOrderReturnDetailByReturnIds(List<Long> returnIds) {
        QueryWrapper<OrderReturnDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderReturnDetailDO::getReturnId, returnIds);
        wrapper.lambda().eq(OrderReturnDetailDO::getDelFlag, 0);
        return this.list(wrapper);
    }

    /**
     * 根据退货单id查询退货单明细信息
     *
     * @param returnId 退货单id
     * @return 退货单明细集合
     */
    @Override
    public List<OrderReturnDetailDO> getOrderReturnDetailByReturnId(Long returnId) {
        QueryWrapper<OrderReturnDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderReturnDetailDO::getReturnId, returnId);
        wrapper.lambda().eq(OrderReturnDetailDO::getDelFlag, 0);
        return this.list(wrapper);
    }

    /**
     * 根据退货id和明细id查询退货单明细信息
     *
     * @param returnId 退货单id
     * @param detailId 订单明细id
     * @return 退货单明细信息
     */
    @Override
    public OrderReturnDetailDO queryByReturnIdAndDetailId(Long returnId, Long detailId) {
        QueryWrapper<OrderReturnDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderReturnDetailDO::getReturnId, returnId);
        wrapper.lambda().eq(OrderReturnDetailDO::getDetailId, detailId);
        wrapper.lambda().eq(OrderReturnDetailDO::getDelFlag, 0);
        wrapper.last("LIMIT 1");
        return this.getOne(wrapper);
    }
}
