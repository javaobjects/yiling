package com.yiling.dataflow.wash.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowPurchaseWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowPurchaseWashDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 采购流向清洗表 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Repository
public interface FlowPurchaseWashMapper extends BaseMapper<FlowPurchaseWashDO> {

    Page<FlowPurchaseWashDO> listPage(@Param("request") QueryFlowPurchaseWashPageRequest request, Page<FlowPurchaseWashDO> page);

    List<FlowPurchaseWashDO> list(@Param("request") QueryFlowPurchaseWashListRequest request);

    Page<FlowPurchaseWashDO> getPageByYearMonth(@Param("request") QueryFlowPurchaseWashPageRequest request, Page<FlowPurchaseWashDO> page);
}
