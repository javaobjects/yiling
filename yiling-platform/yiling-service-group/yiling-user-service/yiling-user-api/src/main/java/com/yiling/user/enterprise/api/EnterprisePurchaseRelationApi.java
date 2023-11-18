package com.yiling.user.enterprise.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.CountSellerChannelDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePurchaseRelationDTO;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;

/**
 * 采购关系API
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
public interface EnterprisePurchaseRelationApi {
    /**
     * 查询企业采购关系分页列表
     *
     * @param request
     * @return
     */
    Page<EnterprisePurchaseRelationDTO> pageList(QueryPurchaseRelationPageListRequest request);

    /**
     * 查询企业采购关系分页列表(按照下单时间倒序排序)
     *
     * @param request
     * @return
     */
    Page<EnterprisePurchaseRelationDTO> pageListByOrderTime(QueryPurchaseRelationPageListRequest request);

    /**
     * 添加渠道商的采购关系
     * @param request
     * @return
     */
    boolean addPurchaseRelation(SavePurchaseRelationFormRequest request);

    /**
     * 移除渠道商的采购关系
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
    Page<EnterprisePurchaseRelationDTO> canPurchaseEnterprisePageList(QueryPurchaseRelationPageListRequest request);

    /**
     * 统计渠道商采购销购销售渠道商的类型及个数
     *
     * @param request
     * @return
     */
    CountSellerChannelDTO countSellerChannel(QueryPurchaseRelationPageListRequest request);

    /**
     * 根据采购商eid获取可采购企业ID列表
     * @param buyerEid 采购商企业ID
     * @return  销售商企业ID集合
     */
    List<Long> listSellerEidsByBuyerEid(Long buyerEid);

    /**
     * 根据采购商eid获取可采购企业ID列表
     * @param buyerEid 采购商企业ID
     * @return 销售商企业集合
     */
    List<EnterprisePurchaseRelationDTO> listSellesByBuyerEid(Long buyerEid);


    /**
     * 根据采购商eid获取可采购企业信息分页列表
     * @param request
     * @return
     */
    Page<EnterpriseDTO> getSellerEnterprisePageList(QueryPurchaseRelationPageListRequest request);

    /**
     * 校验采购商eid的采购关系是否存在供应商企业ID列表
     * @param buyerEid      采购商企业ID
     * @param sellerEids    供应商企业ID列表
     * @return  如果全部包含则true，否则false
     */
    Boolean checkPurchaseRelation(Long buyerEid, List<Long> sellerEids);
}
