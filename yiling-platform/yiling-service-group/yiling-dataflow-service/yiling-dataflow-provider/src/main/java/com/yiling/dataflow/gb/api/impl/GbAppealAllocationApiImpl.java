package com.yiling.dataflow.gb.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.api.GbAppealAllocationApi;
import com.yiling.dataflow.gb.dto.GbAppealAllocationDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbAppealFormAllocationPageRequest;
import com.yiling.dataflow.gb.service.GbAppealAllocationService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.lang.Assert;

/**
 * @author: houjie.sun
 * @date: 2023/5/15
 */
@DubboService
public class GbAppealAllocationApiImpl implements GbAppealAllocationApi {

    @Autowired
    private GbAppealAllocationService gbAppealAllocationService;

    @Override
    public Page<GbAppealAllocationDTO> flowStatisticListPage(QueryGbAppealFormAllocationPageRequest request) {
        return PojoUtils.map(gbAppealAllocationService.flowStatisticListPage(request), GbAppealAllocationDTO.class);
    }

    @Override
    public List<GbAppealAllocationDTO> listByAppealFormIdAndAllocationType(Long appealFormId, Integer allocationType) {
        Assert.notNull(appealFormId, "参数 appealFormId 不能为空");
        Assert.notNull(allocationType, "参数 allocationType 不能为空");
        return PojoUtils.map(gbAppealAllocationService.listByAppealFormIdAndAllocationType(appealFormId, allocationType), GbAppealAllocationDTO.class);
    }

    @Override
    public GbAppealAllocationDTO getById(Long id) {
        return PojoUtils.map(gbAppealAllocationService.getById(id), GbAppealAllocationDTO.class);
    }
}
