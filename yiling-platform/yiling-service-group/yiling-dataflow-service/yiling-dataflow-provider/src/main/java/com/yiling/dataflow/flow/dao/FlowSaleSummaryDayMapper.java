package com.yiling.dataflow.flow.dao;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.flow.entity.FlowSaleSummaryDayDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-10
 */
@Repository
public interface FlowSaleSummaryDayMapper extends BaseMapper<FlowSaleSummaryDayDO> {
    Integer deleteFlowSaleSummaryDayByEidAndSoTime(@Param("eids") List<Long> eids, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Integer updateFlowSaleSummaryDayByTerminalCustomerType(@Param("eids") List<Long> eids, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("terminalCustomerTypeList") List<Integer> terminalCustomerTypeList, @Param("departmentLabel") String departmentLabel, @Param("judgmentLabel") String judgmentLabel, @Param("remarkLabel") String remarkLabel);
}
