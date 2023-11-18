package com.yiling.dataflow.crm.dao;

import java.util.List;

import com.yiling.dataflow.crm.dto.request.UpdateCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Dao 接口
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
@Repository
public interface CrmEnterpriseRelationShipMapper extends BaseMapper<CrmEnterpriseRelationShipDO> {

    List<Long> getCrmEnterprisePostCode();

    boolean updateBackUpBatchByPostCode(@Param("request") UpdateCrmEnterpriseRelationShipRequest relationShipRequest, @Param("tableName") String tableSuffix);

    List<Long> getExitGroupId(@Param("ids")List<Long> groupIds);

    List<Long> getByGroupIdS(@Param("groupId") Long groupId);

    void updateBackUpBatchForHospital(@Param("tableName")String tableSuffix);
}
