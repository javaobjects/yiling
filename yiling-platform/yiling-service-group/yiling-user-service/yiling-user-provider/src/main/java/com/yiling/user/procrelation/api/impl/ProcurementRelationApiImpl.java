package com.yiling.user.procrelation.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationByTimePageRequest;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateRelationStatusRequest;
import com.yiling.user.procrelation.entity.ProcurementRelationDO;
import com.yiling.user.procrelation.service.ProcurementRelationService;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author: dexi.yao
 * @date: 2023-05-19
 */
@DubboService
public class ProcurementRelationApiImpl implements ProcurementRelationApi {

    @Autowired
    ProcurementRelationService procurementRelationService;

    @Override
    public Long saveProcurementRelation(SaveProcRelationRequest request) {
        return procurementRelationService.saveProcurementRelation(request);
    }

    @Override
    public Long updateProcurementRelation(UpdateProcRelationRequest request) {
        return procurementRelationService.updateProcurementRelation(request);
    }

    @Override
    public Page<ProcurementRelationDTO> queryProcRelationPage(QueryProcRelationPageRequest request) {
        Page<ProcurementRelationDO> page = procurementRelationService.queryProcRelationPage(request);
        return PojoUtils.map(page, ProcurementRelationDTO.class);
    }

    @Override
    public ProcurementRelationDTO queryProcRelationById(Long id) {
        if (ObjectUtil.isNull(id) || ObjectUtil.equal(0L, id)) {
            return null;
        }
        ProcurementRelationDO relationDO = procurementRelationService.getById(id);
        return PojoUtils.map(relationDO, ProcurementRelationDTO.class);
    }


    @Override
    public Boolean closeProcRelationById(Long relationId, Long opUser) {
        return procurementRelationService.closeProcRelationById(relationId,opUser);
    }

    @Override
    public Boolean deleteProcRelationById(Long relationId, Long opUser) {
        return procurementRelationService.deleteProcRelationById(relationId,opUser);
    }

    @Override
    public void initData() {
        procurementRelationService.initData();
    }

    @Override
    public void updateInProgress() {
        procurementRelationService.updateInProgress();
    }

    @Override
    public void updateExpired() {
        procurementRelationService.updateExpired();
    }

    @Override
    public void initEnterpriseSupplier() {
        procurementRelationService.initEnterpriseSupplier();
    }
}
