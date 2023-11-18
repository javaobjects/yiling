package com.yiling.order.order.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.OrderTicketDiscountDO;

/**
 * <p>
 * 订单票折记录 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Repository
public interface OrderTicketDiscountMapper extends BaseMapper<OrderTicketDiscountDO> {

    /**
     * 退还订单票折记录
     * @param id    订单票折记录表id
     * @param refundAmount  退还票折金额
     * @return  数量
     */
    int refundOrderTicketDiscount(@Param("id") Long id, @Param("refundAmount") BigDecimal refundAmount);

}
