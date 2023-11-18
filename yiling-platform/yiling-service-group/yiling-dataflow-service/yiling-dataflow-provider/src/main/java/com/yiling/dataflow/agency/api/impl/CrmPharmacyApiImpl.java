package com.yiling.dataflow.agency.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.dataflow.agency.api.CrmPharmacyApi;
import com.yiling.dataflow.agency.dto.CrmPharmacyDTO;
import com.yiling.dataflow.agency.dto.request.RemoveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmPharmacyRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 零售机构档案扩展表 API
 *
 * @author: yong.zhang
 * @date: 2023/2/14 0014
 */
@DubboService
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrmPharmacyApiImpl implements CrmPharmacyApi {

    private final CrmPharmacyService crmPharmacyService;

    @Override
    public List<CrmPharmacyDTO> listByCrmEnterpriseId(List<Long> crmEnterpriseIdList) {
        List<CrmPharmacyDO> doList = crmPharmacyService.listByCrmEnterpriseId(crmEnterpriseIdList);
        return PojoUtils.map(doList, CrmPharmacyDTO.class);
    }

    @Override
    public CrmPharmacyDTO queryByEnterpriseId(Long crmEnterpriseId) {
        CrmPharmacyDO crmPharmacyDO = crmPharmacyService.queryByEnterpriseId(crmEnterpriseId);
        return PojoUtils.map(crmPharmacyDO, CrmPharmacyDTO.class);
    }

    @Override
    public boolean saveCrmPharmacy(SaveCrmPharmacyRequest request) {
        return crmPharmacyService.saveCrmPharmacy(request);
    }

    @Override
    public boolean removeByEnterpriseId(RemoveCrmPharmacyRequest request) {
        return crmPharmacyService.removeByEnterpriseId(request);
    }

    @Override
    public boolean updateCrmPharmacyBatch(List<UpdateCrmPharmacyRequest> requests) {
        if (CollUtil.isEmpty(requests)){
            return true;
        }
        List<CrmPharmacyDO> list = PojoUtils.map(requests,CrmPharmacyDO.class);
        LambdaQueryWrapper<CrmPharmacyDO> queryWrapper = Wrappers.lambdaQuery();
        List<Long> enterpriseIdList = requests.stream().map(UpdateCrmPharmacyRequest::getCrmEnterpriseId).collect(Collectors.toList());
        if(CollUtil.isEmpty(enterpriseIdList)){
            return false;
        }
        queryWrapper.in(CrmPharmacyDO::getCrmEnterpriseId,enterpriseIdList);
        List<CrmPharmacyDO> crmSupplierDOList = crmPharmacyService.list(queryWrapper);
        Map<Long, Long> collect = crmSupplierDOList.stream().collect(Collectors.toMap(CrmPharmacyDO::getCrmEnterpriseId, CrmPharmacyDO::getId));
        list.forEach(crmSupplierDO -> {
            crmSupplierDO.setId(collect.get(crmSupplierDO.getCrmEnterpriseId()));
        });
        return crmPharmacyService.saveOrUpdateBatch(list);
    }
}
