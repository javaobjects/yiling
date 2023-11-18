package com.yiling.user.shop.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.shop.api.ShopFloorApi;
import com.yiling.user.shop.bo.ShopFloorBO;
import com.yiling.user.shop.dto.ShopFloorDTO;
import com.yiling.user.shop.dto.ShopFloorGoodsDTO;
import com.yiling.user.shop.dto.request.QueryFloorGoodsPageRequest;
import com.yiling.user.shop.dto.request.QueryShopFloorPageRequest;
import com.yiling.user.shop.dto.request.SaveShopFloorRequest;
import com.yiling.user.shop.dto.request.UpdateShopFloorStatusRequest;
import com.yiling.user.shop.service.ShopFloorGoodsService;
import com.yiling.user.shop.service.ShopFloorService;

import lombok.extern.slf4j.Slf4j;

/**
 * 店铺楼层 API 实现
 *
 * @author: lun.yu
 * @date: 2023-02-20
 */
@Slf4j
@DubboService
public class ShopFloorApiImpl implements ShopFloorApi {

    @Autowired
    ShopFloorService shopFloorService;
    @Autowired
    ShopFloorGoodsService shopFloorGoodsService;

    @Override
    public Page<ShopFloorBO> queryListPage(QueryShopFloorPageRequest request) {
        return shopFloorService.queryListPage(request);
    }

    @Override
    public boolean saveFloor(SaveShopFloorRequest request) {
        return shopFloorService.saveFloor(request);
    }

    @Override
    public boolean updateStatus(UpdateShopFloorStatusRequest request) {
        return shopFloorService.updateStatus(request);
    }

    @Override
    public boolean deleteFloor(Long id, Long currentUserId) {
        return shopFloorService.deleteFloor(id, currentUserId);
    }

    @Override
    public ShopFloorDTO get(Long id) {
        return PojoUtils.map(shopFloorService.getById(id), ShopFloorDTO.class);
    }

    @Override
    public Page<ShopFloorGoodsDTO> queryFloorGoodsPage(QueryFloorGoodsPageRequest request) {
        return shopFloorGoodsService.queryFloorGoodsPage(request);
    }

    @Override
    public List<ShopFloorGoodsDTO> queryFloorGoodsList(Long floorId) {
        return shopFloorGoodsService.queryFloorGoodsList(floorId);
    }

    @Override
    public boolean existFloorFlag(Long eid) {
        return shopFloorService.existFloorFlag(eid);
    }

    @Override
    public List<ShopFloorDTO> getShopFloorList(Long eid) {
        return shopFloorService.getShopFloorList(eid);
    }

}
