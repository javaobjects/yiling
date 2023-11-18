package com.yiling.order.order.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.OrderCouponUseDO;

/**
 * <p>
 * 订单优惠劵使用记录表 Dao 接口
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
@Repository
public interface OrderCouponUseMapper extends BaseMapper<OrderCouponUseDO> {

    List<Map<String, Long>> getCountByCouponActivityId(@Param("couponActivityIdList") List<Long> couponActivityIdList);
}
