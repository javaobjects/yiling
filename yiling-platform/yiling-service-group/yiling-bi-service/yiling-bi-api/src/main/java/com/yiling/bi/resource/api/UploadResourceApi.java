package com.yiling.bi.resource.api;

import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.dto.UploadResourceLogDTO;
import com.yiling.bi.resource.dto.request.UpdateResourceLogRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/9/26
 */
public interface UploadResourceApi {

    String importInputLsflRecord(String id);

    UploadResourceLogDTO getUploadResourceLogById(String id);

    UploadResourceDTO getUploadResourceByDataId(String dataId);

    boolean updateUploadResourceLog(UpdateResourceLogRequest request);
}
