package com.yiling.open.erp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.open.erp.bo.ErpMonitorCountStatisticsBO;
import com.yiling.open.erp.bo.SjmsFlowMonitorNoDataSyncEnterpriseBO;
import com.yiling.open.erp.dto.request.ErpClientParentQueryRequest;
import com.yiling.open.erp.entity.ErpClientDO;

/**
 * @author shuan
 */
@Repository
public interface ErpClientMapper extends BaseMapper<ErpClientDO> {

    ErpClientDO selectByClientKey(@Param("key") String key);

    ErpClientDO selectByClientSecret(@Param("secret") String secret);

    Page<ErpClientDO> getParentPage(Page<ErpClientDO> page, @Param("request") ErpClientParentQueryRequest request);

    ErpMonitorCountStatisticsBO getErpMonitorClientStatusCount();

    ErpMonitorCountStatisticsBO getErpMonitorNoHartBeatCount();

    Integer deployInterfaceCount(@Param("licenseNumberList") List<String> licenseNumberList);

    Integer getSyncStatusOffCountByRkSuIdList(@Param("licenseNumberList") List<String> licenseNumberList);

    Integer getClientStatusOffCountByRkSuIdList(@Param("licenseNumberList") List<String> licenseNumberList);

    Integer getRunningCountByRkSuIdList(@Param("licenseNumberList") List<String> licenseNumberList);

    Integer getNoDatCountByRkSuIdListAndFlowDate(@Param("licenseNumberList") List<String> licenseNumberList, @Param("lastestFlowDateEnd") Date lastestFlowDateEnd);

    Integer getNoDataSyncCountByRkSuIdListAndLastestFlowDate(@Param("licenseNumberList") List<String> licenseNumberList, @Param("lastestFlowDateEnd") Date lastestFlowDateEnd);

    List<SjmsFlowMonitorNoDataSyncEnterpriseBO> getNoDataSyncEnterpriseListByRkSuIdListAndEndDate(@Param("licenseNumberList") List<String> licenseNumberList, @Param("lastestFlowDateBegin") Date lastestFlowDateBegin);

    Integer getNoDatCountByRkSuIdListAndFlowDateByCrmIds(@Param("userDatascopeBO") SjmsUserDatascopeBO userDatascopeBO, @Param("lastestCollectDateEnd") Date lastestCollectDateEnd);

}