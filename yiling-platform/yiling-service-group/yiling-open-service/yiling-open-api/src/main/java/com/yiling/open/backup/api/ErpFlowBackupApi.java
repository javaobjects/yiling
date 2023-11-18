package com.yiling.open.backup.api;

/**
 * ERP流向备份和清理
 *
 * @author: houjie.sun
 * @date: 2022/7/18
 */
public interface ErpFlowBackupApi {

    void cleanErpFlowOssFile();

    void erpFlowBackup();

    void erpFlowBackupNew();

}
