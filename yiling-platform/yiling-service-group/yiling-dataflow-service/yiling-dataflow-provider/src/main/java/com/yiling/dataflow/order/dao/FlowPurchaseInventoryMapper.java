package com.yiling.dataflow.order.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryListPageRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventorySettlementDetailRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向商品库存信息 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-26
 */
@Repository
public interface FlowPurchaseInventoryMapper extends BaseMapper<FlowPurchaseInventoryDO> {

    Integer updateQuantityById(@Param("id") Long id, @Param("poQuantity") BigDecimal poQuantity, @Param("opUserId") Long opUserId, @Param("opTime") Date opTime);

    List<FlowPurchaseInventoryDO> getListByEidAndYlGoodsIdAndGoodsInSn(@Param("requestList") List<QueryFlowPurchaseInventorySettlementDetailRequest> requestList);

    Page<FlowPurchaseInventoryDO> listPage(Page<FlowPurchaseInventoryDO> page, @Param("request") QueryFlowPurchaseInventoryListPageRequest request);
}
