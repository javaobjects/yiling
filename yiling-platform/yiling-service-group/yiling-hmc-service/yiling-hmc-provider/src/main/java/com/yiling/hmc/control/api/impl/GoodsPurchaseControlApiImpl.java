package com.yiling.hmc.control.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.control.api.GoodsPurchaseControlApi;
import com.yiling.hmc.control.bo.GoodsPurchaseControlBO;
import com.yiling.hmc.control.dto.GoodsPurchaseControlDTO;
import com.yiling.hmc.control.dto.request.AddGoodsPurchaseRequest;
import com.yiling.hmc.control.dto.request.QueryGoodsPurchaseControlPageRequest;
import com.yiling.hmc.control.dto.request.UpdateGoodsPurchaseRequest;
import com.yiling.hmc.control.service.GoodsPurchaseControlService;

/**
 * 药品进货渠道管控
 * @author: gxl
 * @date: 2022/3/31
 */
@DubboService
public class GoodsPurchaseControlApiImpl implements GoodsPurchaseControlApi {

    @Autowired
    private GoodsPurchaseControlService goodsPurchaseControlService;

    @Override
    public void add(AddGoodsPurchaseRequest addGoodsPurchaseRequest) {
        goodsPurchaseControlService.add(addGoodsPurchaseRequest);
    }

    @Override
    public GoodsPurchaseControlDTO getOneById(Long id) {
        return goodsPurchaseControlService.getOneById(id);
    }

    @Override
    public void update(UpdateGoodsPurchaseRequest request) {
        goodsPurchaseControlService.update(request);
    }

    @Override
    public Page<GoodsPurchaseControlDTO> queryPage(QueryGoodsPurchaseControlPageRequest request) {
        return goodsPurchaseControlService.queryPage(request);
    }
    @Override
    public Integer getByGoodControlId(Long controlId){
       return goodsPurchaseControlService.getByGoodControlId(controlId);
    }

    @Override
    public List<GoodsPurchaseControlDTO> queryByControlIds(List<Long> controlIdList) {
        return goodsPurchaseControlService.queryByControlIds(controlIdList);
    }

    @Override
    public List<GoodsPurchaseControlBO> queryGoodsPurchaseControlList(List<Long> sellSpecificationsIdList) {
        return goodsPurchaseControlService.queryGoodsPurchaseControlList(sellSpecificationsIdList);
    }
}