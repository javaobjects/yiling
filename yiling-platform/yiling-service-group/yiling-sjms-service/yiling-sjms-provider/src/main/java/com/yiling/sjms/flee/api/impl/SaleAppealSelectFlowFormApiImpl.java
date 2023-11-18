package com.yiling.sjms.flee.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flee.api.SaleAppealSelectFlowFormApi;
import com.yiling.sjms.flee.dto.SaleAppealSelectFlowFormDTO;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.entity.SaleAppealSelectFlowFormDO;
import com.yiling.sjms.flee.service.SaleAppealSelectFlowFormService;

import lombok.RequiredArgsConstructor;

/**
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SaleAppealSelectFlowFormApiImpl implements SaleAppealSelectFlowFormApi {
    private final SaleAppealSelectFlowFormService saleAppealSelectFlowFormService;
    @Override
    public boolean saveSelectAppealFlowData(SaveSelectAppealFlowFormRequest request) {
        return saleAppealSelectFlowFormService.saveSelectAppealFlowData(request);
    }

    @Override
    public boolean removeById(RemoveSelectAppealFlowFormRequest request) {
        return saleAppealSelectFlowFormService.removeById(request);
    }

    @Override
    public List<SaleAppealSelectFlowFormDTO> selectAppealFlowPageList(Long formId, Integer type) {
        return PojoUtils.map(saleAppealSelectFlowFormService.ListByFormId(formId, type), SaleAppealSelectFlowFormDTO.class);
    }

    @Override
    public List<SaleAppealSelectFlowFormDTO> selectAppealFlowList(Long formId, Integer type) {
        List<SaleAppealSelectFlowFormDO> saleAppealSelectFlowFormDOList = saleAppealSelectFlowFormService.ListByFormIdAndType(formId, type);
        return saleAppealSelectFlowFormDOList.stream().map(saleAppealSelectFlowFormDO -> {
            SaleAppealSelectFlowFormDTO saleAppealSelectFlowFormDTO = PojoUtils.map(saleAppealSelectFlowFormDO, SaleAppealSelectFlowFormDTO.class);
            saleAppealSelectFlowFormDTO.setEnterpriseName(saleAppealSelectFlowFormDO.getOrgName());
            return saleAppealSelectFlowFormDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public String saveAppealFlowDataAll(SaveSelectAppealFlowFormRequest request) {
        return saleAppealSelectFlowFormService.saveAppealFlowDataAll(request);
    }
}
