package com.yiling.dataflow.backup.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.backup.api.RelationShipBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.service.RelationShipBackupService;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@DubboService
@Slf4j
public class RelationShipBackupApiImpl implements RelationShipBackupApi {
    @Autowired
    private RelationShipBackupService relationShipBackupService;

    @Override
    public Boolean RelationShipBackup(AgencyBackRequest request, List<Long> orgId) {
        return relationShipBackupService.RelationShipBackupByPostcode(request,orgId);
    }
}
