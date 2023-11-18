package com.yiling.dataflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.flow.entity.SyncFlowSaleDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-12-21
 */
@Repository
public interface SyncFlowSaleMapper extends BaseMapper<SyncFlowSaleDO> {

    Integer deleteSyncFlowSaleByIds(@Param("ids") List<Long> ids);

    Integer deleteByFlowSaleIds(@Param("flowSaleIds") List<Long> flowSaleIds);
}
