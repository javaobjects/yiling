package com.yiling.dataflow.statistics.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.statistics.entity.FlowDistributionEnterpriseDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 企业信息配置表 Dao 接口
 * </p>
 *
 * @author handy
 * @date 2023-01-05
 */
@Repository
public interface FlowDistributionEnterpriseMapper extends BaseMapper<FlowDistributionEnterpriseDO> {

    Integer getCountByEidList(@Param("eidList") List<Long> eidList);
}
