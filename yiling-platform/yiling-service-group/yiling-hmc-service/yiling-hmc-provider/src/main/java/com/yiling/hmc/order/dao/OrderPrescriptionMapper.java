package com.yiling.hmc.order.dao;

import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.hmc.order.entity.OrderPrescriptionDO;

/**
 * <p>
 * C端兑付订单关联处方表 Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Repository
public interface OrderPrescriptionMapper extends BaseMapper<OrderPrescriptionDO> {

}
