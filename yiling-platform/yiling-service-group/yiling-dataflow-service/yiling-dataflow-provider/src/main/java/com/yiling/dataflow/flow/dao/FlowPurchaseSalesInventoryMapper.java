package com.yiling.dataflow.flow.dao;

import java.util.List;

import com.yiling.dataflow.flow.entity.FlowPurchaseSalesInventoryDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-14
 */
@Repository
public interface FlowPurchaseSalesInventoryMapper extends BaseMapper<FlowPurchaseSalesInventoryDO> {
        Integer deleteFlowPurchaseSalesInventoryByEidAndYearAndMonth(@Param("eids")List<Long> eids,@Param("year")String year,@Param("month")String month);
}
