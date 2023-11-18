package com.yiling.dataflow.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.gb.api.GbAppealFlowStatisticApi;
import com.yiling.dataflow.gb.bo.GbAppealFormFlowCountBO;
import com.yiling.dataflow.gb.dto.GbAppealFlowStatisticDTO;
import com.yiling.dataflow.gb.service.GbAppealFlowStatisticService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.lang.Assert;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@DubboService
public class GbAppealFlowStatisticApiImpl implements GbAppealFlowStatisticApi {

    @Autowired
    private GbAppealFlowStatisticService gbAppealFlowStatisticService;

    @Override
    public GbAppealFormFlowCountBO getFlowCountByGbAppealFormId(Long gbAppealFormId) {
        Assert.notNull(gbAppealFormId, "参数 gbAppealFormId 不能为空");
        return gbAppealFlowStatisticService.getFlowCountByGbAppealFormId(gbAppealFormId);
    }

    @Override
    public GbAppealFlowStatisticDTO getByFlowWashId(Long flowWashId) {
        Assert.notNull(flowWashId, "参数 flowWashId 不能为空");
        return PojoUtils.map(gbAppealFlowStatisticService.getByFlowWashId(flowWashId), GbAppealFlowStatisticDTO.class);
    }

    @Override
    public List<GbAppealFlowStatisticDTO> getByFlowWashIdList(List<Long> flowWashIdList) {
        Assert.notEmpty(flowWashIdList, "参数 flowWashIdList 不能为空");
        return PojoUtils.map(gbAppealFlowStatisticService.getByFlowWashIdList(flowWashIdList), GbAppealFlowStatisticDTO.class);
    }
}
