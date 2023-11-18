package com.yiling.dataflow.gb.api.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.gb.api.GbOrderApi;
import com.yiling.dataflow.gb.dto.GbOrderDTO;
import com.yiling.dataflow.gb.dto.request.QueryGbOrderPageRequest;
import com.yiling.dataflow.gb.dto.request.SaveOrUpdateGbOrderRequest;
import com.yiling.dataflow.gb.entity.GbOrderDO;
import com.yiling.dataflow.gb.service.GbOrderService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author: shuang.zhang
 * @date: 2023/5/11
 */
@DubboService
public class GbOrderApiImpl implements GbOrderApi {

    @Autowired
    private GbOrderService gbOrderService;

    @Override
    public boolean save(SaveOrUpdateGbOrderRequest request) {
        return gbOrderService.saveOrUpdate(PojoUtils.map(request, GbOrderDO.class));
    }

    @Override
    public List<Long> saveOrUpdateBatch(List<SaveOrUpdateGbOrderRequest> list) {
        List<GbOrderDO> dataList = PojoUtils.map(list, GbOrderDO.class);
        boolean result = gbOrderService.saveOrUpdateBatch(dataList, 1000);
        if (result) {
            return dataList.stream().map(GbOrderDO::getId).collect(Collectors.toList());
        }
        return ListUtil.empty();
    }

    @Override
    public boolean mateFlow(Long id) {
        return gbOrderService.mateFlow(id);
    }

    @Override
    public GbOrderDTO getById(Long id) {
        return PojoUtils.map(gbOrderService.getById(id),GbOrderDTO.class);
    }

    @Override
    public Page<GbOrderDTO> getGbOrderPage(QueryGbOrderPageRequest request) {
        return PojoUtils.map(gbOrderService.getGbOrderPage(request),GbOrderDTO.class);
    }

    @Override
    public List<GbOrderDTO> getByIdList(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return ListUtil.empty();
        }
        return PojoUtils.map(gbOrderService.getByIdList(idList),GbOrderDTO.class);
    }

    @Override
    public List<GbOrderDTO> listByFormId(Long formId) {
        Assert.notNull(formId, "参数 formId 不能为空");
        return PojoUtils.map(gbOrderService.listByFormId(formId),GbOrderDTO.class);
    }

    @Override
    public List<GbOrderDTO> listByFormIdList(List<Long> formIds) {
        Assert.notEmpty(formIds, "参数 formIds 不能为空");
        return PojoUtils.map(gbOrderService.listByFormIdList(formIds),GbOrderDTO.class);
    }
}
