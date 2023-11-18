package com.yiling.dataflow.backup.api;

import java.util.List;

import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.CheckAgencyBackupRequest;

/**
 * 基础档案备份
 */
public interface RelationShipBackupApi {
    /**
     *
     * @param request
     */
    Boolean RelationShipBackup(AgencyBackRequest request, List<Long> orgId);
}
