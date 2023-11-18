package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;

/**
 * <p>
 * 退货单明细（erp出库单维度） 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-23
 */
public interface OrderReturnDetailErpService extends BaseService<OrderReturnDetailErpDO> {

    /**
     * 查询出某退货明细出库单已经退货的数量
     *
     * @param detailId
     * @param batchNo
     * @param erpDeliveryNo
     * @return
     */
    Integer sumReturnQualityByErpDeliveryNo(Long detailId, String batchNo, String erpDeliveryNo,String erpSendOrderId);

    /**
     * @param returnIds
     * @return
     */
    List<OrderReturnDetailErpDO> listByReturnIds(List<Long> returnIds);

}
