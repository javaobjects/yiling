package com.yiling.user.shop.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseSalesAreaDTO;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.dto.request.SaveShopRequest;
import com.yiling.user.shop.dto.request.UpdateAnnouncementRequest;
import com.yiling.user.system.dto.PaymentMethodDTO;

/**
 * 店铺 API
 *
 * @author: lun.yu
 * @date: 2021/10/15
 */
public interface ShopApi {

    /**
     * 获取店铺设置信息
     * @param currentEid
     * @return
     */
    ShopDTO getShop(Long currentEid);

    /**
     * 批量获取店铺设置信息
     * @param currentEidList
     * @return
     */
    List<ShopDTO> listShopByEidList(List<Long> currentEidList);

    /**
     * 获取店铺开通区域Json
     * @param shopId
     * @return
     */
    EnterpriseSalesAreaDTO getShopAreaJson(Long shopId);

    /**
     * 获取店铺支付方式
     * @param shopId
     * @return
     */
    List<PaymentMethodDTO> listShopPaymentMethods(Long shopId);

    /**
     * 设置店铺
     * @param request
     * @return
     */
    Boolean setShop(SaveShopRequest request);

    /**
     * 店铺分页列表
     * @param request
     * @return
     */
    Page<ShopListItemDTO> queryShopListPage(QueryShopRequest request);

    /**
     * 店铺分页列表
     * @param request
     * @return
     */
    Page<ShopListItemDTO> queryPurchaseShopListPage(QueryShopRequest request);

    /**
     * 获取店铺详情
     * @param shopId
     * @return
     */
    ShopListItemDTO getShopDetail(Long shopId);

    /**
     * 判断店铺是否可以销售给当前登录企业
     * @param customerEid 当前登录的企业ID
     * @param eidList 卖方企业ID（支持多个卖家）
     * @return key 表示卖方企业ID , value 表示 是否可以销售
     */
    Map<Long,Boolean> checkSaleAreaByCustomerEid(Long customerEid , List<Long> eidList);

    /**
     * 根据当前登录企业的区域信息，获取可以买哪些企业的接口
     * @param eid 当前登录的企业ID
     * @return 返回可以买的企业集合
     */
    List<Long> getSellEidByEidSaleArea(Long eid);

    /**
     * 修改公告
     * @param request
     * @return
     */
    Boolean updateShopAnnouncement(UpdateAnnouncementRequest request);

    /**
     * 获取店铺公告
     * @param currentEid
     * @return
     */
    ShopDTO getShopAnnouncement(Long currentEid);
}
