package com.yiling.dataflow.wash.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashListRequest;
import com.yiling.dataflow.wash.dto.request.QueryFlowSaleWashPageRequest;
import com.yiling.dataflow.wash.entity.FlowSaleWashDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向销售清洗表 Mapper 接口
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Repository
public interface FlowSaleWashMapper extends BaseMapper<FlowSaleWashDO> {

    Page<FlowSaleWashDO> listPage(@Param("request") QueryFlowSaleWashPageRequest request, Page<FlowSaleWashDO> page);

    List<FlowSaleWashDO> list(@Param("request") QueryFlowSaleWashListRequest request);

    Page<FlowSaleWashDO> getPageByYearMonth(@Param("request") QueryFlowSaleWashPageRequest request, Page<FlowSaleWashDO> page);
}
