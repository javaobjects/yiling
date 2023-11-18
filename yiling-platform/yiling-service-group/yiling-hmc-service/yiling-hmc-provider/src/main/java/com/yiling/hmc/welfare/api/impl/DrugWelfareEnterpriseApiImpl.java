package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareEnterpriseApi;
import com.yiling.hmc.welfare.dto.DrugWelfareEnterpriseDTO;
import com.yiling.hmc.welfare.dto.EnterpriseListDTO;
import com.yiling.hmc.welfare.dto.request.DeleteDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareEnterprisePageRequest;
import com.yiling.hmc.welfare.dto.request.SaveDrugWelfareEnterpriseRequest;
import com.yiling.hmc.welfare.service.DrugWelfareEnterpriseService;

/**
 * 药品福利计划关联企业API
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@DubboService
public class DrugWelfareEnterpriseApiImpl implements DrugWelfareEnterpriseApi {

    @Autowired
    private DrugWelfareEnterpriseService enterpriseService;

    @Override
    public List<DrugWelfareEnterpriseDTO> getByEid(Long eId) {
        return PojoUtils.map(enterpriseService.getByEid(eId), DrugWelfareEnterpriseDTO.class);
    }

    @Override
    public List<DrugWelfareEnterpriseDTO> getByWelfareId(Long welfareId) {
        return PojoUtils.map(enterpriseService.getByWelfareId(welfareId), DrugWelfareEnterpriseDTO.class);
    }

    @Override
    public List<EnterpriseListDTO> getEnterpriseList() {
        return PojoUtils.map(enterpriseService.getEnterpriseList(), EnterpriseListDTO.class);
    }

    @Override
    public Page<DrugWelfareEnterpriseDTO> pageList(DrugWelfareEnterprisePageRequest request) {
        return PojoUtils.map(enterpriseService.pageList(request), DrugWelfareEnterpriseDTO.class);
    }

    @Override
    public Boolean saveDrugWelfareEnterprise(SaveDrugWelfareEnterpriseRequest request) {
        return enterpriseService.saveDrugWelfareEnterprise(request);
    }

    @Override
    public Boolean deleteDrugWelfareEnterprise(DeleteDrugWelfareEnterpriseRequest request) {
        return enterpriseService.deleteDrugWelfareEnterprise(request);
    }

}