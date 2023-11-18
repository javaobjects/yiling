package com.yiling.dataflow.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.gb.api.GbAppealFlowRelatedApi;
import com.yiling.dataflow.gb.dto.GbAppealFlowRelatedDTO;
import com.yiling.dataflow.gb.service.GbAppealFlowRelatedService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.lang.Assert;

/**
 * @author: houjie.sun
 * @date: 2023/5/16
 */
@DubboService
public class GbAppealFlowRelatedApiImpl implements GbAppealFlowRelatedApi {

    @Autowired
    private GbAppealFlowRelatedService gbAppealFlowRelatedService;

    @Override
    public List<GbAppealFlowRelatedDTO> getByAppealFormIdList(List<Long> appealFormIdList) {
        Assert.notEmpty(appealFormIdList, "参数 appealFormIdList 不能为空");
        return PojoUtils.map(gbAppealFlowRelatedService.getByAppealFormIdList(appealFormIdList), GbAppealFlowRelatedDTO.class);
    }

    @Override
    public List<GbAppealFlowRelatedDTO> getListByAppealFormId(Long appealFormId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        return PojoUtils.map(gbAppealFlowRelatedService.getListByAppealFormId(appealFormId), GbAppealFlowRelatedDTO.class);
    }

    @Override
    public GbAppealFlowRelatedDTO getByAppealFormIdAndFlowWashId(Long appealFormId, Long flowWashId) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(flowWashId, "参数 flowWashId 不能为空");
        return PojoUtils.map(gbAppealFlowRelatedService.getByAppealFormIdAndFlowWashId(appealFormId, flowWashId), GbAppealFlowRelatedDTO.class);
    }
}
