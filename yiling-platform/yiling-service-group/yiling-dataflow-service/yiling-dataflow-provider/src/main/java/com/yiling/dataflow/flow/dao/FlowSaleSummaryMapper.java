package com.yiling.dataflow.flow.dao;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.entity.FlowSaleSummaryDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
@Repository
public interface FlowSaleSummaryMapper extends BaseMapper<FlowSaleSummaryDO> {
    Integer deleteFlowSaleSummaryByEidAndSoId(@Param("eids") List<Long> eids, @Param("soId")String soId);
    Integer deleteFlowSaleSummaryByFlowSaleId(@Param("flowSaleId")Long flowSaleId);
    Integer deleteFlowSaleSummaryByEidAndSoTime(@Param("eids") List<Long> eids, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    Page<FlowSaleSummaryDO> page(Page<FlowSaleSummaryDO> page, @Param("request") QueryFlowSaleSummaryRequest request);
}
