package com.yiling.hmc.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.OrderPrescriptionGoodsDTO;
import com.yiling.hmc.order.entity.OrderPrescriptionGoodsDO;
import com.yiling.hmc.wechat.dto.request.OrderPrescriptionGoodsRequest;

import java.util.List;

/**
 * <p>
 * C端兑付订单关联处方商品表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderPrescriptionGoodsService extends BaseService<OrderPrescriptionGoodsDO> {

    /**
     * 获取处方商品信息
     * @param orderId
     * @return
     */
    List<OrderPrescriptionGoodsDTO> getByOrderId(Long orderId);
}
