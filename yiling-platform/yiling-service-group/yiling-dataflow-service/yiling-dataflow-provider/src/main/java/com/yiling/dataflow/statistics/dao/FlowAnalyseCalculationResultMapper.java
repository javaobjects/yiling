package com.yiling.dataflow.statistics.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.statistics.dto.StockWarnDTO;
import com.yiling.dataflow.statistics.dto.StockWarnIconDTO;
import com.yiling.dataflow.statistics.dto.request.StockWarnIconRequest;
import com.yiling.dataflow.statistics.dto.request.StockWarnPageRequest;
import com.yiling.dataflow.statistics.entity.FlowAnalyseCalculationResultDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 天数计算结果表 Dao 接口
 * </p>
 *
 * @author handy
 * @date 2023-01-11
 */
@Repository
public interface FlowAnalyseCalculationResultMapper extends BaseMapper<FlowAnalyseCalculationResultDO> {

    Page<StockWarnIconDTO> getSaleDaysIconWarn(@Param("page") Page<Object> page, @Param("request") StockWarnIconRequest request);

    Page<StockWarnDTO> getStockWarnPage(@Param("page") Page<Object> page,@Param("request") StockWarnPageRequest request);

    List<StockWarnDTO> getSaleDaysIconWarnList(@Param("request")StockWarnPageRequest request, @Param("specificationIds") List<String> specificationIds);

    int deleteFlowAnalyseCalculationResult();
}
