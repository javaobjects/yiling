package com.yiling.open.backup.service;

/**
 * op库ERP流向数据备份清理服务类
 *
 * @author: houjie.sun
 * @date: 2022/7/12
 */
public interface ErpFlowBackupService {

    /**
     * 清理erp流向上传到OSS的文件
     */
    void cleanErpFlowOssFile();

    /**
     * op库ERP流向数据备份清理
     */
    void erpFlowBackup();

    /**
     * op库ERP流向数据备份清理
     */
    void erpFlowBackupNew();

}
