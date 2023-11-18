package com.yiling.dataflow.statistics.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.statistics.api.FlowDistributionEnterpriseApi;
import com.yiling.dataflow.statistics.dto.FlowDistributionEnterpriseDTO;
import com.yiling.dataflow.statistics.service.FlowDistributionEnterpriseService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2023/2/7
 */
@DubboService
@Slf4j
public class FlowDistributionEnterpriseApiImpl implements FlowDistributionEnterpriseApi {

    @Autowired
    private FlowDistributionEnterpriseService flowDistributionEnterpriseService;

    @Override
    public List<FlowDistributionEnterpriseDTO> getListByCodeList(List<String> codeList) {
        return PojoUtils.map(flowDistributionEnterpriseService.getListByCodeList(codeList), FlowDistributionEnterpriseDTO.class);
    }

    @Override
    public List<FlowDistributionEnterpriseDTO> getListByEidList(List<Long> eidList) {
        return PojoUtils.map(flowDistributionEnterpriseService.getListByEidList(eidList), FlowDistributionEnterpriseDTO.class);
    }

    @Override
    public Integer getCountByEidList(List<Long> eidList) {
        return flowDistributionEnterpriseService.getCountByEidList(eidList);
    }

    @Override
    public List<FlowDistributionEnterpriseDTO> getListByCrmIdListAndEnameLevel(List<Long> crmIdList, String enameLevel) {
        return PojoUtils.map(flowDistributionEnterpriseService.getListByCrmIdListAndEnameLevel(crmIdList, enameLevel), FlowDistributionEnterpriseDTO.class);
    }

    @Override
    public List<FlowDistributionEnterpriseDTO> listByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList) {
        Assert.notNull(crmEnterpriseIdList, "参数 crmEnterpriseIdList 不能为空");
        return PojoUtils.map(flowDistributionEnterpriseService.listByCrmEnterpriseIdList(crmEnterpriseIdList), FlowDistributionEnterpriseDTO.class);
    }
}
