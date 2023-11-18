package com.yiling.dataflow.backup.service;

import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.CheckAgencyBackupRequest;

/**
 * 基础档案备份
 */
public interface AgencyBackupService {

    /**
     * @param request
     */
    boolean agencyEsbInfoBackUp(AgencyBackRequest request);

    boolean checkAgencyBackupByYearMonth(CheckAgencyBackupRequest request);

    void esbInfoBackup(AgencyBackRequest request);
}
