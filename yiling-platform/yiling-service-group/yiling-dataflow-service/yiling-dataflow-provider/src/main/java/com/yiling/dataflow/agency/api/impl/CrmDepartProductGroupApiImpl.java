package com.yiling.dataflow.agency.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.agency.api.CrmDepartProductGroupApi;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.request.QueryDepartProductGroupByUploadNameRequest;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.dataflow.agency.service.CrmDepartmentProductRelationService;
import com.yiling.framework.common.util.PojoUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class CrmDepartProductGroupApiImpl implements CrmDepartProductGroupApi {
    @Autowired
    private CrmDepartmentProductRelationService crmDepartmentProductRelationService;
    @Override
    public List<CrmDepartmentProductRelationDTO> getDepartProductGroupListByUploadName(QueryDepartProductGroupByUploadNameRequest request) {

        QueryWrapper<CrmDepartmentProductRelationDO> queryWrapper=new QueryWrapper();
       queryWrapper.lambda().eq(CrmDepartmentProductRelationDO::getSupplyChainRole,request.getSupplyChainRole())
               .in(CrmDepartmentProductRelationDO::getUploadFileName,request.getUploadNames());
        return PojoUtils.map(crmDepartmentProductRelationService.list(queryWrapper),CrmDepartmentProductRelationDTO.class);
    }

    @Override
    public List<CrmDepartmentProductRelationDTO> getByGroupId(Long groupId) {
        return crmDepartmentProductRelationService.getByGroupId(groupId);
    }

    @Override
    public List<CrmDepartmentProductRelationDTO> getByGroupIds(List<Long> groupIds) {
        return crmDepartmentProductRelationService.getByGroupIds(groupIds);
    }
}
