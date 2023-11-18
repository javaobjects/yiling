package com.yiling.dataflow.flowcollect.dao;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 日流向心跳统计表 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Repository
public interface FlowCollectHeartStatisticsMapper extends BaseMapper<FlowCollectHeartStatisticsDO> {

    Page<FlowDayHeartStatisticsBO> pageList(Page<Object> page, @Param("request") QueryDayCollectStatisticsRequest request);

    List<Long> findCrmList();

    boolean deleteByIds(@Param("ids") List<Long> ids);

}
