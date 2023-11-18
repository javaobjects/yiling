package com.yiling.user.system.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.service.EnterpriseEmployeeService;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.AddOrRemoveMrSalesGoodsRequest;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;
import com.yiling.user.system.dto.request.UpdateMrSalesGoodsRequest;
import com.yiling.user.system.service.MrSalesGoodsDetailService;

/**
 * 医药代表 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@DubboService
public class MrApiImpl implements MrApi {

    @Autowired
    MrSalesGoodsDetailService mrSalesGoodsDetailService;
    @Autowired
    EnterpriseEmployeeService enterpriseEmployeeService;

    @Override
    public Page<MrBO> pageList(QueryMrPageListRequest request) {
        return enterpriseEmployeeService.mrPageList(request);
    }

    @Override
    public List<MrBO> listByIds(List<Long> ids) {
        return enterpriseEmployeeService.mrListByIds(ids);
    }

    @Override
    public MrBO getById(Long id) {
        return enterpriseEmployeeService.getMrById(id);
    }

    @Override
    public Map<Long, List<Long>> listGoodsIdsByEmployeeIds(List<Long> employeeIds) {
        return mrSalesGoodsDetailService.listByEmployeeIds(employeeIds);
    }

    @Override
    public List<Long> listGoodsIdsByEmployeeIdAndGoodsIds(Long employeeId, List<Long> goodsIds) {
        return mrSalesGoodsDetailService.listByEmployeeIdAndGoodsIds(employeeId, goodsIds);
    }

    @Override
    public List<Long> listEmoloyeeIdsByGoodsIds(List<Long> goodsIds) {
        return mrSalesGoodsDetailService.listEmoloyeeIdsByGoodsIds(goodsIds);
    }

    @Override
    public Boolean addOrRemoveSalesGoods(AddOrRemoveMrSalesGoodsRequest request) {
        return mrSalesGoodsDetailService.addOrRemove(request);
    }

    @Override
    public Boolean updateSalesGoods(UpdateMrSalesGoodsRequest request) {
        return mrSalesGoodsDetailService.update(request);
    }
}
