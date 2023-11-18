package com.yiling.dataflow.order.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.dto.request.QueryFlowGoodsBatchListPageRequest;
import com.yiling.dataflow.order.entity.FlowGoodsBatchDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * ERP库存汇总同步数据 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-11
 */
@Repository
public interface FlowGoodsBatchMapper extends BaseMapper<FlowGoodsBatchDO> {

    /**
     * 硬删除
     * @param eids
     * @return
     */
    Integer deleteFlowGoodsBatchByEids(@Param("eids") List<Long> eids, @Param("createTime") Date createTime);

    Page<FlowGoodsBatchDO> page(Page<FlowGoodsBatchDO> page, @Param("request") QueryFlowGoodsBatchListPageRequest request);

    List<Long> getNotStatisticsTotalNumberEidList();

    List<FlowGoodsBatchDO> getNotStatisticsList();

    /**
     * 物理删除 del_flag = 1 的
     *
     * @return
     */
    Integer deleteByDelFlagAndEid(@Param("eid") Long eid);

    Integer getSpecificationIdCount(Long specificationId);

    Date getMaxGbTimeByEid(@Param("eid") Long eid);
}
