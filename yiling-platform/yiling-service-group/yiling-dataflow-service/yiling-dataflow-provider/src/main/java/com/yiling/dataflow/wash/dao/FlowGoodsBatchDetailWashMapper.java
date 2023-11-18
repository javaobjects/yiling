package com.yiling.dataflow.wash.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchDetailWashPageRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowGoodsBatchDetailWashDO;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * ERP库存汇总同步数据 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Repository
public interface FlowGoodsBatchDetailWashMapper extends BaseMapper<FlowGoodsBatchDetailWashDO> {

    Page<FlowGoodsBatchDetailWashDO> listPage(@Param("request") QueryFlowGoodsBatchDetailWashPageRequest request, Page<FlowGoodsBatchDetailWashDO> page);

    List<FlowGoodsBatchDetailWashDO> list(@Param("request") QueryFlowGoodsBatchDetailWashListRequest request);

    Page<FlowGoodsBatchDetailWashDO> getPageByYearMonth(@Param("request") QueryFlowGoodsBatchDetailWashPageRequest request, Page<FlowGoodsBatchDetailWashDO> page);
}
