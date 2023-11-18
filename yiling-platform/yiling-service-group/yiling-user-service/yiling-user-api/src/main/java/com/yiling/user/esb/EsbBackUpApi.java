package com.yiling.user.esb;

import com.yiling.user.esb.dto.EsbBackUpRecordDTO;
import com.yiling.user.esb.dto.request.EsbBackUpRequest;

import java.util.List;

/**
 * esb备份接口
 */
public interface EsbBackUpApi {
    List<EsbBackUpRecordDTO> esbInfoBackup(EsbBackUpRequest request);
}
