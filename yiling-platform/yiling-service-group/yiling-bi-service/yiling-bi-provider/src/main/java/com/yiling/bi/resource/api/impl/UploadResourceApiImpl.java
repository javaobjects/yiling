package com.yiling.bi.resource.api.impl;

import com.yiling.bi.resource.api.UploadResourceApi;
import com.yiling.bi.resource.dto.UploadResourceDTO;
import com.yiling.bi.resource.dto.UploadResourceLogDTO;
import com.yiling.bi.resource.dto.request.UpdateResourceLogRequest;
import com.yiling.bi.resource.service.UploadResourceLogService;
import com.yiling.bi.resource.service.UploadResourceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/9/26
 */
@DubboService
public class UploadResourceApiImpl implements UploadResourceApi {

    @Autowired
    private UploadResourceLogService uploadResourceLogService;
    @Autowired
    private UploadResourceService uploadResourceService;

    @Override
    public String importInputLsflRecord(String id) {
        return uploadResourceLogService.importInputLsflRecord(id);
    }

    @Override
    public UploadResourceLogDTO getUploadResourceLogById(String id) {
        return uploadResourceLogService.getUploadResourceLogById(id);
    }

    @Override
    public UploadResourceDTO getUploadResourceByDataId(String dataId) {
        return uploadResourceService.getUploadResourceByDataId(dataId);
    }

    @Override
    public boolean updateUploadResourceLog(UpdateResourceLogRequest request) {
        return uploadResourceLogService.updateUploadResourceLog(request);
    }
}
