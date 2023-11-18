package com.yiling.user.shop.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.entity.EnterpriseSalesAreaDO;
import com.yiling.user.shop.dto.ShopDTO;
import com.yiling.user.shop.dto.ShopListItemDTO;
import com.yiling.user.shop.dto.ShopPaymentMethodDTO;
import com.yiling.user.shop.dto.request.QueryShopRequest;
import com.yiling.user.shop.dto.request.SaveShopRequest;
import com.yiling.user.shop.dto.request.UpdateAnnouncementRequest;
import com.yiling.user.shop.entity.ShopDO;

/**
 * <p>
 * 店铺设置表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
public interface ShopService extends BaseService<ShopDO> {

    /**
     * 获取店铺设置信息
     * @param currentEid
     * @return
     */
    ShopDTO getShop(Long currentEid);

    /**
     * 获取店铺关联区域Json
     * @param shopId
     * @return
     */
    EnterpriseSalesAreaDO getShopAreaJson(Long shopId);

    /**
     * 设置店铺
     * @param request
     * @return
     */
    Boolean setShop(SaveShopRequest request);

    /**
     * 获取店铺支付方式
     * @param shopId
     * @return
     */
    List<ShopPaymentMethodDTO> getShopPayMethods(Long shopId);

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
     * 批量获取店铺设置信息
     * @param currentEidList
     * @return
     */
    List<ShopDTO> listShopByEidList(List<Long> currentEidList);

    /**
     * 判断店铺是否可以销售给当前登录企业
     * @param customerEid 当前登录的企业ID
     * @param eidList 卖方企业ID（支持多个卖家）
     * @return key 表示卖方企业ID , value 表示 是否可以销售
     */
    Map<Long,Boolean> checkSaleAreaByCustomerEid(Long customerEid , List<Long> eidList);

    /**
     * 根据当前登录企业的销售区域，获取可以买哪些企业的接口
     * @param eid
     * @return
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
