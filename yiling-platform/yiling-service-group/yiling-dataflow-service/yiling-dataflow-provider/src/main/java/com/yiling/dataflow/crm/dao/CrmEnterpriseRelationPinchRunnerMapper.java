package com.yiling.dataflow.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipPinchRunnerRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationPinchRunnerDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2023-04-19
 */
@Repository
public interface CrmEnterpriseRelationPinchRunnerMapper extends BaseMapper<CrmEnterpriseRelationPinchRunnerDO> {

    List<Long> getBusinessSuperiorPostCode();

    boolean updateBackUpBatchByBusinessSuperiorPostCode(@Param("request") UpdateCrmEnterpriseRelationShipPinchRunnerRequest request, @Param("tableName") String tableName);

}
