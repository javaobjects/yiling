package com.yiling.dataflow.flowcollect.dao;

import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.entity.FlowCollectHeartStatisticsDetailDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 日流向心跳统计明细表 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Repository
public interface FlowCollectHeartStatisticsDetailMapper extends BaseMapper<FlowCollectHeartStatisticsDetailDO> {

    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(@Param("fchIds") List<Long> fchIds);

    boolean deleteDetailByFchIds(@Param("fchIds") List<Long> fchIds);
}
