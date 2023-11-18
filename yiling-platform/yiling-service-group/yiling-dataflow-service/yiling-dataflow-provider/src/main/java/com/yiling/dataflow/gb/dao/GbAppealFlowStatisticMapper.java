package com.yiling.dataflow.gb.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowStatisticRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowStatisticDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向团购统计表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
@Repository
public interface GbAppealFlowStatisticMapper extends BaseMapper<GbAppealFlowStatisticDO> {

    int substract(@Param("request") SubstractGbAppealFlowStatisticRequest request);

    GbAppealFormFlowCountBO getFlowCountByGbAppealFormId(@Param("gbAppealFormId") Long gbAppealFormId);
}
