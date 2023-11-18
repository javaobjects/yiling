package com.yiling.dataflow.gb.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.gb.dto.request.SubstractGbAppealFlowRelatedRequest;
import com.yiling.dataflow.gb.entity.GbAppealFlowRelatedDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向团购申诉申请关联流向表 Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-05-12
 */
@Repository
public interface GbAppealFlowRelatedMapper extends BaseMapper<GbAppealFlowRelatedDO> {

    int substract(@Param("request") SubstractGbAppealFlowRelatedRequest request);

    int deleteById(@Param("id") Long id);

}
