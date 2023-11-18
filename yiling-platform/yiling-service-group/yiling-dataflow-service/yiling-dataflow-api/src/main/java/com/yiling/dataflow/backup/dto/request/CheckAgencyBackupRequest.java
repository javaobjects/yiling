package com.yiling.dataflow.backup.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

@Data
public class CheckAgencyBackupRequest extends BaseRequest {
    /**
     * 年月格式为yyyyMM
     */
    private Long yearMonth;

}
