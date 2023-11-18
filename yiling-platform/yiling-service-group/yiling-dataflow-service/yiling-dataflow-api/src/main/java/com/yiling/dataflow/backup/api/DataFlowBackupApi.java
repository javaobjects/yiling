package com.yiling.dataflow.backup.api;

import java.util.List;

import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;

/**
 * 线上ERP流向备份和清理
 *
 * @author: houjie.sun
 * @date: 2022/7/18
 */
public interface DataFlowBackupApi {

    void dataFlowBackup(DataFlowBackupRequest request);

    /**
     * 根据表名前缀获取表名列表
     *
     * @param tableNamePrefix
     * @return
     */
    List<String> getTableNamesByPrefix(String tableNamePrefix);

    void dataFlowBackupNew(DataFlowBackupRequest request);
}
