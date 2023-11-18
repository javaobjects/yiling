package com.yiling.open.backup.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.backup.api.ErpFlowBackupApi;
import com.yiling.open.backup.service.ErpFlowBackupService;

/**
 * @author: houjie.sun
 * @date: 2022/7/18
 */
@DubboService
public class ErpFlowBackupApiImpl implements ErpFlowBackupApi {

    @Autowired
    private ErpFlowBackupService erpFlowBackupService;

    @Override
    public void cleanErpFlowOssFile() {
        erpFlowBackupService.cleanErpFlowOssFile();
    }

    @Override
    public void erpFlowBackup() {
        erpFlowBackupService.erpFlowBackup();
    }

    @Override
    public void erpFlowBackupNew() {
        erpFlowBackupService.erpFlowBackupNew();
    }

}
