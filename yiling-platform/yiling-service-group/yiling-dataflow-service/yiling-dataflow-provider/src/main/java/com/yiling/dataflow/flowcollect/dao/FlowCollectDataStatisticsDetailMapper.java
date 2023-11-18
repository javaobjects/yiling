package com.yiling.dataflow.flowcollect.dao;

import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.entity.FlowCollectDataStatisticsDetailDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 日流向数据统计明细表 Dao 接口
 * </p>
 *
 * @author xueli.ji
 * @date 2023-06-15
 */
@Repository
public interface FlowCollectDataStatisticsDetailMapper extends BaseMapper<FlowCollectDataStatisticsDetailDO> {
    List<FlowDayHeartStatisticsDetailBO> listDetailsByFchsIds(@Param("fchIds") List<Long> fchIds);
    boolean deleteDetailByFchIds(@Param("fcdIds") List<Long> fcdIds);
}
