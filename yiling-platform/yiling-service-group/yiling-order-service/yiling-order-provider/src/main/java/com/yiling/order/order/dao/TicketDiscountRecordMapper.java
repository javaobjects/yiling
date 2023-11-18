package com.yiling.order.order.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.TicketDiscountRecordDO;

/**
 * <p>
 * 票折信息 Dao 接口
 * </p>
 *
 * @author wei.wang
 * @date 2021-07-02
 */
@Repository
public interface TicketDiscountRecordMapper extends BaseMapper<TicketDiscountRecordDO> {
    /**
     *
     * @param ticketDiscountNo 票折编号
     * @param usedAmount 使用金额
     */
    int updateUsedAmount(@Param("ticketDiscountNo")String ticketDiscountNo, @Param("usedAmount")BigDecimal usedAmount);
    /**
     *
     * @param ticketDiscountNo 票折编号
     * @param usedAmount 退还金额
     */
    int reduceUsedAmount(@Param("ticketDiscountNo")String ticketDiscountNo, @Param("usedAmount")BigDecimal usedAmount);

}
