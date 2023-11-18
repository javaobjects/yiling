package com.yiling.order.order.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.OrderDetailTicketDiscountDO;

/**
 * <p>
 * 订单明细票折信息 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Repository
public interface OrderDetailTicketDiscountMapper extends BaseMapper<OrderDetailTicketDiscountDO> {

    /**
     * 退还订单明细票折信息
     *
     * @param id    订单明细票折信息id
     * @param refundAmount  退还票折金额
     * @return  数量
     */
    int refundOrderDetailTicketDiscount(@Param("id") Long id, @Param("refundAmount") BigDecimal refundAmount);
}
