package com.yiling.hmc.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.entity.OrderReturnDetailDO;

/**
 * <p>
 * 退货单明细表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderReturnDetailService extends BaseService<OrderReturnDetailDO> {

    /**
     * 根据退货单id查询退货单明细
     *
     * @param returnId 退货单id
     * @return 退货单明细
     */
    List<OrderReturnDetailDO> listByReturnId(Long returnId);

}
