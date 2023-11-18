package com.yiling.dataflow.flow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.entity.FlowEnterpriseConnectMonitorDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorMapper
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Repository
public interface FlowEnterpriseConnectMonitorMapper extends BaseMapper<FlowEnterpriseConnectMonitorDO> {

    /**
     * 统计各省份有效直连和无效直连数量
     * @param supplierLevel
     * @return
     */
    List<FlowEnterpriseConnectStatisticBO> countConnectionStatusByProvince(@Param("supplierLevel") Integer supplierLevel);
}
