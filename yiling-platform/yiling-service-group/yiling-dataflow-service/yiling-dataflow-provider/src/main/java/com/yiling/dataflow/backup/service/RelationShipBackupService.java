package com.yiling.dataflow.backup.service;

import java.util.List;

import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;

/**
 * 线上ERP流向数据备份清理服务类
 *
 * @author: houjie.sun
 * @date: 2022/7/15
 */
public interface RelationShipBackupService {

    /**
     * 线上ERP流向数据备份清理
     * @param request
     * @param orgId 业务部负责人关系
     *
     * @return
     */
    Boolean RelationShipBackup(AgencyBackRequest request, List<Long> orgId);

    /**
     * 线上ERP流向数据备份清理
     * @param request
     * @param orgId 业务部负责人关系
     *
     * @return
     */
    Boolean RelationShipBackupByPostcode(AgencyBackRequest request, List<Long> orgId);
}
