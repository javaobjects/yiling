package com.yiling.user.control.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.control.api.ControlApi;
import com.yiling.user.control.dto.GoodsControlDTO;
import com.yiling.user.control.dto.request.BatchSaveCustomerControlRequest;
import com.yiling.user.control.dto.request.DeleteGoodsControlRequest;
import com.yiling.user.control.dto.request.QueryCustomerControlPageRequest;
import com.yiling.user.control.dto.request.SaveCustomerControlRequest;
import com.yiling.user.control.dto.request.SaveRegionControlRequest;
import com.yiling.user.control.service.GoodsControlService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/10/21
 */
@Slf4j
@DubboService
public class ControlApiImpl implements ControlApi {

    @Autowired
    private GoodsControlService goodsControlService;

    @Override
    public GoodsControlDTO getCustomerTypeInfo(Long goodsId, Long eid) {
        return goodsControlService.getCustomerTypeInfo(goodsId, eid);
    }

    @Override
    public GoodsControlDTO getRegionInfo(Long goodsId, Long eid) {
        return goodsControlService.getRegionInfo(goodsId, eid);
    }

    @Override
    public GoodsControlDTO getCustomerInfo(Long goodsId, Long eid) {
        return goodsControlService.getCustomerInfo(goodsId, eid);
    }

    @Override
    public Page<Long> getPageCustomerInfo(QueryCustomerControlPageRequest request) {
        return goodsControlService.getPageCustomerInfo(request);
    }

    @Override
    public Boolean saveRegion(SaveRegionControlRequest request) {
        return goodsControlService.saveRegion(request);
    }

    @Override
    public Boolean saveCustomer(SaveCustomerControlRequest request) {
        return goodsControlService.saveCustomer(request);
    }

    @Override
    public Boolean batchSaveCustomer(BatchSaveCustomerControlRequest request) {
        return goodsControlService.batchSaveCustomer(request);
    }

    @Override
    public Boolean deleteCustomer(DeleteGoodsControlRequest request) {
        return goodsControlService.deleteCustomer(request);
    }

    @Override
    public Map<Long, Integer> getGoodsControlByBuyerEidAndGid(List<Long> goodsIds,Long eid, Long buyerEid) {
        return goodsControlService.getGoodsControlByBuyerEidAndGid(goodsIds,eid,buyerEid);
    }
}
