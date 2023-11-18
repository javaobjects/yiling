package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderReturnDetailDO;

/**
 * <p>
 * 退货单明细 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
public interface OrderReturnDetailService extends BaseService<OrderReturnDetailDO> {

    /**
     * 根据退货单id批量获取明细
     *
     * @param returnIds 退货单id集合
     * @return 退货单明细信息集合
     */
    List<OrderReturnDetailDO> getOrderReturnDetailByReturnIds(List<Long> returnIds);

    /**
     * 根据退货单id获取退货单明细
     *
     * @param returnId 退货单id
     * @return 退货单明细信息集合
     */
    List<OrderReturnDetailDO> getOrderReturnDetailByReturnId(Long returnId);

    /**
     * 根据退货单编号和订单明细id查询退货单明细信息
     *
     * @param returnId 退货单id
     * @param detailId 订单明细id
     * @return 退货单明细信息
     */
    OrderReturnDetailDO queryByReturnIdAndDetailId(Long returnId, Long detailId);
}
