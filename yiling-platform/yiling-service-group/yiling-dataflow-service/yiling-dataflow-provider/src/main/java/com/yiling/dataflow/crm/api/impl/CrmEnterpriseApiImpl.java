package com.yiling.dataflow.crm.api.impl;

import java.util.List;

import com.yiling.dataflow.crm.dto.request.QueryDataScopeRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyCountRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.bo.CrmEnterpriseIdAndNameBO;
import com.yiling.dataflow.crm.bo.CrmEnterprisePartBO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.PermitAgencyLockApplyRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterpriseBackUpPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.SaveAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.dataflow.crm.dto.request.UpdateAgencyEnterpriseRequest;
import com.yiling.dataflow.crm.enums.CrmEnterpriseErrorCode;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.exception.BusinessException;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.util.PojoUtils;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
@DubboService
@Slf4j
public class CrmEnterpriseApiImpl implements CrmEnterpriseApi {

    @Autowired
    private CrmEnterpriseService crmEnterpriseService;


    @Override
    public Page<CrmEnterpriseSimpleDTO> getCrmEnterpriseSimplePage(QueryCrmEnterprisePageRequest request) {
        return crmEnterpriseService.getCrmEnterpriseSimplePage(request);
    }

    @Override
    public Long saveCrmEnterpriseSimple(SaveCrmEnterpriseRequest request) {
        if (StrUtil.isEmpty(request.getLicenseNumber()) || StrUtil.isEmpty(request.getName())) {
            throw new BusinessException(CrmEnterpriseErrorCode.VERIFY_ERROR_CODE);
        }
        CrmEnterpriseDTO crmEnterpriseByLicenseNumber = null;
        if(!"0".equals(request.getLicenseNumber())){
            crmEnterpriseByLicenseNumber = crmEnterpriseService.getCrmEnterpriseByLicenseNumber(request.getLicenseNumber(),false);
        }

        CrmEnterpriseDTO crmEnterpriseByName = crmEnterpriseService.getCrmEnterpriseCodeByName(request.getName(),false);
        if (crmEnterpriseByLicenseNumber != null || crmEnterpriseByName != null) {
            throw new BusinessException(CrmEnterpriseErrorCode.REPEAT_ERROR_CODE);
        }
        return crmEnterpriseService.saveCrmEnterpriseSimple(request);
    }

    @Override
    public void saveAgencyEnterpriseList(List<SaveAgencyEnterpriseRequest> requestList) {
        crmEnterpriseService.saveAgencyEnterpriseList(requestList);
    }

    @Override
    public void updateAgencyEnterpriseList(List<UpdateAgencyEnterpriseRequest> requestList) {
        crmEnterpriseService.updateAgencyEnterpriseList(requestList);
    }

    @Override
    public Integer updateCrmEnterpriseSimple(UpdateAgencyEnterpriseRequest request) {
        return crmEnterpriseService.updateCrmEnterpriseSimple(request);
    }

    @Override
    public CrmEnterpriseDTO getCrmEnterpriseCodeByName(String name,boolean isEffect) {
        return crmEnterpriseService.getCrmEnterpriseCodeByName(name,isEffect);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseCodeByNameList(List<String> nameList) {
        return crmEnterpriseService.getCrmEnterpriseCodeByNameList(nameList);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseByCodeList(List<String> codeList) {
        return crmEnterpriseService.getCrmEnterpriseByCodeList(codeList);
    }

    @Override
    public List<CrmEnterpriseDTO> listByIdsAndName(List<Long> ids, String name) {
        return crmEnterpriseService.listByIdsAndName(ids, name);
    }

    @Override
    public int getCountBySupplyChainRole(Integer supplyChainRole) {
        return crmEnterpriseService.getCountBySupplyChainRole(supplyChainRole);
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterpriseInfoPage(QueryCrmAgencyPageListRequest request) {
        return crmEnterpriseService.getCrmEnterpriseInfoPage(request);
    }

    @Override
    public Page<CrmEnterpriseDTO> pageListSuffixBackUpInfo(QueryCrmEnterpriseBackUpPageRequest request) {
        return PojoUtils.map(crmEnterpriseService.pageListSuffixBackUpInfo(request, "wash_" + request.getSoMonth()), CrmEnterpriseDTO.class);
    }

    @Override
    public CrmEnterpriseDTO getCrmEnterpriseById(Long id) {
        return PojoUtils.map(crmEnterpriseService.getById(id), CrmEnterpriseDTO.class);
    }

    @Override
    public CrmEnterpriseDTO getCrmEnterpriseBackById(Long id,String tableSuffix){
        return PojoUtils.map(crmEnterpriseService.getSuffixByCrmEnterpriseId(id,tableSuffix), CrmEnterpriseDTO.class);
    }

    @Override
    public CrmEnterpriseDTO getFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request) {
        return PojoUtils.map(crmEnterpriseService.getFirstCrmEnterpriseInfo(request), CrmEnterpriseDTO.class);
    }

    @Override
    public CrmEnterpriseDTO getBakFirstCrmEnterpriseInfo(QueryCrmAgencyCountRequest request, String tableSuffix) {
        return PojoUtils.map(crmEnterpriseService.getBakFirstCrmEnterpriseInfo(request,tableSuffix), CrmEnterpriseDTO.class);
    }

    @Override
    public Page<CrmEnterpriseIdAndNameBO> getCrmEnterpriseIdAndNameByName(QueryCrmEnterpriseByNamePageListRequest request) {
        return crmEnterpriseService.getCrmEnterpriseIdAndNameByName(request,request.getTableSuffix());
    }

    @Override
    public Page<CrmEnterprisePartBO> getCrmEnterprisePartInfoByName(QueryCrmEnterpriseByNamePageListRequest request) {
        return crmEnterpriseService.getCrmEnterprisePartInfoByName(request,request.getTableSuffix());
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterpriseByName(QueryCrmEnterpriseByNamePageListRequest request) {
        return crmEnterpriseService.getCrmEnterpriseByName(request);
    }

    @Override
    public Page<CrmEnterpriseDTO> getCrmEnterpriseByNameLikeRight(QueryCrmEnterpriseByNamePageListRequest request) {
        return PojoUtils.map(crmEnterpriseService.getCrmEnterpriseByNameLikeRight(request), CrmEnterpriseDTO.class);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseListById(List<Long> crmEnterPriseIds) {
        return crmEnterpriseService.getCrmEnterpriseListById( crmEnterPriseIds);
    }

    @Override
    public List<CrmEnterpriseDTO> listByEidList(List<Long> eidList) {
        return crmEnterpriseService.listByEidList(eidList);
    }

    @Override
    public Boolean permitAgencyLockApply(List<PermitAgencyLockApplyRequest> request) {
        return crmEnterpriseService.permitAgencyLockApply(request);
    }

    @Override
    public List<CrmEnterpriseDTO> getDistributorEnterpriseByIds(SjmsUserDatascopeBO datascopeBO) {
        return crmEnterpriseService.getDistributorEnterpriseByIds(datascopeBO);
    }

    @Override
    public List<CrmEnterpriseDTO> getCrmEnterpriseListByDataScope(QueryDataScopeRequest request) {
        return crmEnterpriseService.getCrmEnterpriseListByDataScope(request);
    }
}
