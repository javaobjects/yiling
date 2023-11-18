package com.yiling.user.enterprise.api.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterprisePurchaseRelationApi;
import com.yiling.user.enterprise.bo.CountSellerChannelBO;
import com.yiling.user.enterprise.bo.PurchaseRelationBO;
import com.yiling.user.enterprise.dto.CountSellerChannelDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePurchaseRelationDTO;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;
import com.yiling.user.enterprise.entity.EnterprisePurchaseRelationDO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.service.EnterprisePurchaseRelationService;

import cn.hutool.core.collection.CollUtil;

/**
 * 企业采购关系apiImpl
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@DubboService
public class EnterprisePurchaseRelationApiImpl implements EnterprisePurchaseRelationApi {

    @Autowired
    private EnterprisePurchaseRelationService enterprisePurchaseRelationService;

    @Override
    public Page<EnterprisePurchaseRelationDTO> pageList(QueryPurchaseRelationPageListRequest request) {
        Page<PurchaseRelationBO> page = enterprisePurchaseRelationService.pageList(request);
        return PojoUtils.map(page, EnterprisePurchaseRelationDTO.class);
    }

    @Override
    public Page<EnterprisePurchaseRelationDTO> pageListByOrderTime(QueryPurchaseRelationPageListRequest request) {

        Page<PurchaseRelationBO> page = enterprisePurchaseRelationService.pageListByOrderTime(request);

        return PojoUtils.map(page, EnterprisePurchaseRelationDTO.class);
    }

    @Override
    public boolean addPurchaseRelation(SavePurchaseRelationFormRequest request) {
        return enterprisePurchaseRelationService.addPurchaseRelation(request);
    }

    @Override
    public Boolean removePurchaseRelation(RemovePurchaseRelationFormRequest request) {
        return enterprisePurchaseRelationService.removePurchaseRelation(request);
    }

    @Override
    public Page<EnterprisePurchaseRelationDTO> canPurchaseEnterprisePageList(QueryPurchaseRelationPageListRequest request) {
        return PojoUtils.map(enterprisePurchaseRelationService.canPurchaseEnterprisePageList(request), EnterprisePurchaseRelationDTO.class);
    }

    @Override
    public CountSellerChannelDTO countSellerChannel(QueryPurchaseRelationPageListRequest request) {
        CountSellerChannelDTO countSellerChannelDTO = new CountSellerChannelDTO();
        List<CountSellerChannelBO> countSellerChannelBoList = enterprisePurchaseRelationService.countSellerChannel(request);
        if (CollUtil.isEmpty(countSellerChannelBoList)) {
            return countSellerChannelDTO;
        }

        Map<Long, Integer> collectMap = countSellerChannelBoList.stream().collect(Collectors.groupingBy(CountSellerChannelBO::getChannelId, Collectors.summingInt(CountSellerChannelBO::getChannelNum)));
        Integer total = countSellerChannelBoList.stream().mapToInt(CountSellerChannelBO::getChannelNum).sum();
        countSellerChannelDTO.setTotal(total);

        collectMap.forEach((channelId,channelNum)->{
            if (EnterpriseChannelEnum.isIndustry(channelId)) {
                countSellerChannelDTO.setIndustryCount(channelNum);
            } else if (EnterpriseChannelEnum.isIndustryDirect(channelId)) {
                countSellerChannelDTO.setIndustryDirectCount(channelNum);
            } else if (EnterpriseChannelEnum.isLevel1(channelId)) {
                countSellerChannelDTO.setLevel1Count(channelNum);
            } else if (EnterpriseChannelEnum.isLevel2(channelId)) {
                countSellerChannelDTO.setLevel2Count(channelNum);
            } else if (EnterpriseChannelEnum.isZ2p1((channelId))) {
                countSellerChannelDTO.setZ2p1Count(channelNum);
            }
        });

        return countSellerChannelDTO;
    }

    @Override
    public List<Long> listSellerEidsByBuyerEid(Long buyerEid) {
        Assert.notNull(buyerEid, "参数buyerEid不能为空");
        List<EnterprisePurchaseRelationDO> list = enterprisePurchaseRelationService.listByBuyerEid(buyerEid);
        return list.stream().map(EnterprisePurchaseRelationDO::getSellerEid).distinct().collect(Collectors.toList());
    }

    @Override
    public Page<EnterpriseDTO> getSellerEnterprisePageList(QueryPurchaseRelationPageListRequest request) {
        return PojoUtils.map(enterprisePurchaseRelationService.querySellerEnterprisePageList(request), EnterpriseDTO.class);
    }

    @Override
    public Boolean checkPurchaseRelation(Long buyerEid, List<Long> sellerEids) {
        return enterprisePurchaseRelationService.checkPurchaseRelation(buyerEid, sellerEids);
    }

    @Override
    public List<EnterprisePurchaseRelationDTO> listSellesByBuyerEid(Long buyerEid) {

        Assert.notNull(buyerEid, "参数buyerEid不能为空");
        List<EnterprisePurchaseRelationDO> list = enterprisePurchaseRelationService.listByBuyerEid(buyerEid);

        return PojoUtils.map(list,EnterprisePurchaseRelationDTO.class);
    }
}
