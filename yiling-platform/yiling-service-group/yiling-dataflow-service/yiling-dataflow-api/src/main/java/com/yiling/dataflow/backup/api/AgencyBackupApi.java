package com.yiling.dataflow.backup.api;

import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.CheckAgencyBackupRequest;

/**
 * 基础档案备份
 */
public interface AgencyBackupApi {
    /**
     * @param request
     */
    void agencyInfoBackup(AgencyBackRequest request);

    boolean checkAgencyBackupByYearMonth(CheckAgencyBackupRequest request);

    void esbInfoBackup(AgencyBackRequest request);
}
