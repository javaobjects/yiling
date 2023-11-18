package com.yiling.dataflow.backup.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.backup.api.DataFlowBackupApi;
import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;
import com.yiling.dataflow.backup.service.DataFlowBackupService;

/**
 * @author: houjie.sun
 * @date: 2022/7/18
 */
@DubboService
public class DataFlowBackupApiImpl implements DataFlowBackupApi {

    @Autowired
    private DataFlowBackupService dataFlowBackupService;

    @Override
    public void dataFlowBackup(DataFlowBackupRequest request) {
        dataFlowBackupService.dataFlowBackup(request);
    }

    @Override
    public List<String> getTableNamesByPrefix(String tableNamePrefix) {
        return dataFlowBackupService.getTableNamesByPrefix(tableNamePrefix);
    }

    @Override
    public void dataFlowBackupNew(DataFlowBackupRequest request) {
        dataFlowBackupService.dataFlowBackupNew(request);
    }
}
