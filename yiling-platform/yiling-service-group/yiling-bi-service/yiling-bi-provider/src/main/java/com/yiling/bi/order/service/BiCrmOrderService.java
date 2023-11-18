package com.yiling.bi.order.service;

import java.util.List;

import com.yiling.bi.order.entity.BiCrmOrderDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/9/21
 */
public interface BiCrmOrderService extends BaseService<BiCrmOrderDO> {

    void handleFtpCrmOrderData();

    List<BiCrmOrderDO> getNoMatchedBiCrmOrder();

    void batchInsert(List<BiCrmOrderDO> biCrmOrderDOList);

}
