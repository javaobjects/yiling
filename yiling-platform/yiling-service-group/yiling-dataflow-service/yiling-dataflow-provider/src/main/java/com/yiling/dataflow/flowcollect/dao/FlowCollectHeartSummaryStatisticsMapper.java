package com.yiling.dataflow.flowcollect.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartSummaryStatisticsDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 日流向心跳统计汇总表 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Repository
public interface FlowCollectHeartSummaryStatisticsMapper extends BaseMapper<FlowCollectHeartSummaryStatisticsDO> {
    Page<FlowDayHeartStatisticsBO> pageList(Page<Object> page, @Param("request") QueryDayCollectStatisticsRequest request);
    List<Long> findCrmList();
    boolean deleteByIds(@Param("ids") List<Long> ids);

}
