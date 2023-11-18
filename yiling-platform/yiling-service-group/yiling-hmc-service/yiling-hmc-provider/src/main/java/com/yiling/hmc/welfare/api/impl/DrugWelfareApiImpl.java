package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareApi;
import com.yiling.hmc.welfare.dto.DrugWelfareDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfarePageRequest;
import com.yiling.hmc.welfare.dto.request.DrugWelfareUpdateRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareDO;
import com.yiling.hmc.welfare.service.DrugWelfareService;

/**
 * 药品福利计划API
 *
 * @author: fan.shen
 * @date: 2022-09-26
 */
@DubboService
public class DrugWelfareApiImpl implements DrugWelfareApi {

    @Autowired
    private DrugWelfareService service;

    @Override
    public List<DrugWelfareDTO> getByIdList(List<Long> idList) {
        return PojoUtils.map(service.getByIdList(idList), DrugWelfareDTO.class);
    }

    @Override
    public DrugWelfareDTO queryById(Long id) {
        return PojoUtils.map(service.getById(id), DrugWelfareDTO.class);
    }

    @Override
    public Page<DrugWelfareDTO> pageList(DrugWelfarePageRequest request) {
        Page<DrugWelfareDO> doPage = service.pageList(request);
        return PojoUtils.map(doPage, DrugWelfareDTO.class);
    }

    @Override
    public boolean updateDrugWelfare(DrugWelfareUpdateRequest request) {
        return service.updateDrugWelfare(request);
    }
}