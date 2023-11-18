package com.yiling.user.enterprise.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.bo.CountSellerChannelBO;
import com.yiling.user.enterprise.bo.PurchaseRelationBO;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterprisePurchaseRelationDO;

/**
 * <p>
 * 企业采购关系 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
public interface EnterprisePurchaseRelationService extends BaseService<EnterprisePurchaseRelationDO> {
    /**
     * 企业采购关系分页列表
     *
     * @param request
     * @return
     */
    Page<PurchaseRelationBO> pageList(QueryPurchaseRelationPageListRequest request);

    /**
     * 企业采购关系分页列表(按照下单时间进行倒序排序)
     *
     * @param request
     * @return
     */
    Page<PurchaseRelationBO> pageListByOrderTime(QueryPurchaseRelationPageListRequest request);

    /**
     * 渠道商添加采购关系
     * @param request
     * @return
     */
    boolean addPurchaseRelation(SavePurchaseRelationFormRequest request);

    /**
     * 渠道商移除采购关系
     * @param request
     * @return
     */
    Boolean removePurchaseRelation(RemovePurchaseRelationFormRequest request);

    /**
     * 查询渠道商可供采购企业分页列表
     *
     * @param request
     * @return
     */
    Page<PurchaseRelationBO> canPurchaseEnterprisePageList(QueryPurchaseRelationPageListRequest request);

    /**
     * 统计渠道商采购销购销售渠道商的类型及个数
     *
     * @param request
     * @return
     */
    List<CountSellerChannelBO> countSellerChannel(QueryPurchaseRelationPageListRequest request);

    /**
     * 获取采购商采购关系信息列表
     *
     * @param buyerEid 采购商ID
     * @return
     */
    List<EnterprisePurchaseRelationDO> listByBuyerEid(Long buyerEid);

    /**
     * 校验采购商eid的采购关系是否存在供应商企业ID列表
     * @param buyerEid      采购商企业ID
     * @param sellerEids    供应商企业ID列表
     * @return  如果全部包含则true，否则false
     */
    Boolean checkPurchaseRelation(Long buyerEid, List<Long> sellerEids);

    /**
     * 根据采购商eid获取可采购企业信息分页列表
     * @param request
     * @return
     */
    Page<EnterpriseDO> querySellerEnterprisePageList(QueryPurchaseRelationPageListRequest request);
}
