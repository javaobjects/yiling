package com.yiling.dataflow.relation.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.relation.dto.request.QueryFlowCustomerMappingListPageRequest;
import com.yiling.dataflow.relation.entity.FlowCustomerMappingDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-07-18
 */
@Repository
public interface FlowCustomerMappingMapper extends BaseMapper<FlowCustomerMappingDO> {
    Page<FlowCustomerMappingDO> page(Page<FlowCustomerMappingDO> page, @Param("request") QueryFlowCustomerMappingListPageRequest request);
}
