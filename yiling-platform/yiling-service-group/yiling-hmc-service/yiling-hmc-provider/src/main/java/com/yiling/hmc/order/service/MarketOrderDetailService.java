package com.yiling.hmc.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.entity.MarketOrderDetailDO;

/**
 * <p>
 * HMC订单明细表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
public interface MarketOrderDetailService extends BaseService<MarketOrderDetailDO> {

    /**
     * 根据订单id集合查询订单明细
     *
     * @param orderIds
     * @return
     */
    List<MarketOrderDetailDTO> queryByOrderIdList(List<Long> orderIds);

    /**
     * 根据商品名称模糊查询订单明细
     *
     * @param goodsName
     * @return
     */
    List<MarketOrderDetailDTO> queryByGoodsNameList(String goodsName);
}
