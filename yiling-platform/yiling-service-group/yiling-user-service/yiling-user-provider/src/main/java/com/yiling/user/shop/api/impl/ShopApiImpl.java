package com.yiling.user.shop.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.shop.api.ShopApi;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.dto.request.SaveShopRequest;
import com.yiling.user.shop.dto.request.UpdateAnnouncementRequest;
import com.yiling.user.shop.service.ShopPaymentMethodService;
import com.yiling.user.shop.service.ShopService;
import com.yiling.user.system.dto.PaymentMethodDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 店铺 API 实现
 *
 * @author: lun.yu
 * @date: 2021/10/20
 */
@Slf4j
@DubboService
public class ShopApiImpl implements ShopApi {

    @Autowired
    ShopService              shopService;
    @Autowired
    ShopPaymentMethodService shopPaymentMethodService;

    @Override
    public ShopDTO getShop(Long currentEid) {
        return shopService.getShop(currentEid);
    }

    @Override
    public List<ShopDTO> listShopByEidList(List<Long> currentEidList) {
        return shopService.listShopByEidList(currentEidList);
    }

    @Override
    public EnterpriseSalesAreaDTO getShopAreaJson(Long shopId) {
        return PojoUtils.map(shopService.getShopAreaJson(shopId),EnterpriseSalesAreaDTO.class);
    }

    @Override
    public List<PaymentMethodDTO> listShopPaymentMethods(Long eid) {
        return PojoUtils.map(shopPaymentMethodService.listByEid(eid), PaymentMethodDTO.class);
    }

    @Override
    public Boolean setShop(SaveShopRequest request) {
        return shopService.setShop(request);
    }

    @Override
    public Page<ShopListItemDTO> queryShopListPage(QueryShopRequest request) {
        return shopService.queryShopListPage(request);
    }

    @Override
    public Page<ShopListItemDTO> queryPurchaseShopListPage(QueryShopRequest request) {
        return shopService.queryPurchaseShopListPage(request);
    }

    @Override
    public ShopListItemDTO getShopDetail(Long shopId) {
        return shopService.getShopDetail(shopId);
    }

    @Override
    public Map<Long,Boolean> checkSaleAreaByCustomerEid(Long customerEid , List<Long> eidList) {
        return shopService.checkSaleAreaByCustomerEid(customerEid,eidList);
    }

    @Override
    public List<Long> getSellEidByEidSaleArea(Long eid) {
        return shopService.getSellEidByEidSaleArea(eid);
    }

    @Override
    public Boolean updateShopAnnouncement(UpdateAnnouncementRequest request) {
        return shopService.updateShopAnnouncement(request);
    }

    @Override
    public ShopDTO getShopAnnouncement(Long currentEid) {
        return shopService.getShopAnnouncement(currentEid);
    }
}
