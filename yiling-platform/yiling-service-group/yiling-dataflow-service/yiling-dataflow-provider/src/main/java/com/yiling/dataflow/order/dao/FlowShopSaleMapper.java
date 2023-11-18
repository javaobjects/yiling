package com.yiling.dataflow.order.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.DeleteFlowShopSaleByUnlockRequest;
import com.yiling.dataflow.order.dto.request.QueryFlowShopSaleListPageRequest;
import com.yiling.dataflow.order.entity.FlowShopSaleDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向销售明细信息表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-06
 */
@Repository
public interface FlowShopSaleMapper extends BaseMapper<FlowShopSaleDO> {

    Page<FlowShopSaleDO> page(Page<FlowShopSaleDO> page, @Param("request") QueryFlowShopSaleListPageRequest request);

    Integer deleteFlowShopSaleBydEidAndSoTime(@Param("request") DeleteFlowShopSaleByUnlockRequest request);
}
