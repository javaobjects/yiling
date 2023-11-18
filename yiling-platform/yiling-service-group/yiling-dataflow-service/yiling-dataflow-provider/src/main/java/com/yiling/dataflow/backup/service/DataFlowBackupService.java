package com.yiling.dataflow.backup.service;

import java.util.List;

import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;

/**
 * 线上ERP流向数据备份清理服务类
 *
 * @author: houjie.sun
 * @date: 2022/7/15
 */
public interface DataFlowBackupService {

    /**
     * 线上ERP流向数据备份清理
     *
     * @return
     */
    void dataFlowBackup(DataFlowBackupRequest request);

    /**
     * 根据表名前缀获取表名列表
     *
     * @param tableNamePrefix
     * @return
     */
    List<String> getTableNamesByPrefix(String tableNamePrefix);

    /**
     * 线上ERP流向数据备份清理
     *
     * @return
     */
    void dataFlowBackupNew(DataFlowBackupRequest request);

}
