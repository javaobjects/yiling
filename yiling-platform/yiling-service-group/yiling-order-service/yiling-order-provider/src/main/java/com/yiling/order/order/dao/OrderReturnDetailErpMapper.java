package com.yiling.order.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.order.order.entity.OrderReturnDetailErpDO;

/**
 * <p>
 * 退货单明细（erp出库单维度） Dao 接口
 * </p>
 *
 * @author yong.zhang
 * @date 2021-09-23
 */
@Repository
public interface OrderReturnDetailErpMapper extends BaseMapper<OrderReturnDetailErpDO> {

    /**
     * 根据明细，批次号和出库单号统计退货总数量
     *
     * @param detailId
     * @param batchNo
     * @param erpDeliveryNo
     * @return
     */
    Integer sumReturnQualityByErpDeliveryNo(@Param("detailId") Long detailId, @Param("batchNo") String batchNo, @Param("erpDeliveryNo") String erpDeliveryNo,@Param("erpSendOrderId") String erpSendOrderId);

}
