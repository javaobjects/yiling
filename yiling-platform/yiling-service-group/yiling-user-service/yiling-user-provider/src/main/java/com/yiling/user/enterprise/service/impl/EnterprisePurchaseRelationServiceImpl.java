package com.yiling.user.enterprise.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.bo.CountSellerChannelBO;
import com.yiling.user.enterprise.bo.PurchaseRelationBO;
import com.yiling.user.enterprise.dao.EnterprisePurchaseRelationMapper;
import com.yiling.user.enterprise.dto.request.QueryPurchaseRelationPageListRequest;
import com.yiling.user.enterprise.dto.request.RemovePurchaseRelationFormRequest;
import com.yiling.user.enterprise.dto.request.SavePurchaseRelationFormRequest;
import com.yiling.user.enterprise.entity.EnterpriseChannelPurchaseRelationDO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.entity.EnterprisePurchaseRelationDO;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseStatusEnum;
import com.yiling.user.enterprise.service.EnterpriseChannelPurchaseRelationService;
import com.yiling.user.enterprise.service.EnterprisePurchaseRelationService;
import com.yiling.user.enterprise.service.EnterpriseService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 企业采购关系 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
public class EnterprisePurchaseRelationServiceImpl extends BaseServiceImpl<EnterprisePurchaseRelationMapper, EnterprisePurchaseRelationDO> implements EnterprisePurchaseRelationService {
    
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private EnterpriseChannelPurchaseRelationService channelPurchaseRelationService;
    
    @Override
    public Page<PurchaseRelationBO> pageList(QueryPurchaseRelationPageListRequest request) {
        Assert.notNull(request.getBuyerEid(), "查询企业采购关系：该渠道商企业ID参数缺失！");
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        return this.baseMapper.pageList(request.getPage(), request);
    }


    @Override
    public Page<PurchaseRelationBO> pageListByOrderTime(QueryPurchaseRelationPageListRequest request) {

        Assert.notNull(request.getBuyerEid(), "查询企业采购关系：该渠道商企业ID参数缺失！");
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());

        return this.baseMapper.pageListOrderByOrderTime(request.getPage(),request);
    }

    @Override
    public boolean addPurchaseRelation(SavePurchaseRelationFormRequest request) {
        Assert.notNull(request.getBuyerId(), "添加采购关系：采购商ID参数为空！");
        Assert.notEmpty(request.getSellerIds(), "添加采购关系：供应商ID参数为空！");
        Assert.notNull(request.getSource(), "添加采购关系：数据类型参数为空！");

        List<EnterprisePurchaseRelationDO> batchEntities = new ArrayList<>();
        for (Long sellerEid : request.getSellerIds()) {
            EnterprisePurchaseRelationDO entity = new EnterprisePurchaseRelationDO();
            entity.setSource(request.getSource());
            entity.setBuyerEid(request.getBuyerId());
            entity.setSellerEid(sellerEid);
            // 校验重复
            int count = this.count(new LambdaQueryWrapper<EnterprisePurchaseRelationDO>().eq(EnterprisePurchaseRelationDO::getBuyerEid, request.getBuyerId())
                    .eq(EnterprisePurchaseRelationDO::getSellerEid, sellerEid));
            if (count > 0) {
                continue;
            }
            entity.setOpUserId(request.getOpUserId());
            batchEntities.add(entity);
        }
        return this.saveOrUpdateBatch(batchEntities, 1000);
    }

    @Override
    public Boolean removePurchaseRelation(RemovePurchaseRelationFormRequest request) {
        Assert.notNull(request.getBuyerId(), "移除采购关系：采购商ID参数为空！");
        Assert.notEmpty(request.getSellerIds(), "移除采购关系：供应商ID参数为空！");

        EnterprisePurchaseRelationDO entity = new EnterprisePurchaseRelationDO();
        entity.setBuyerEid(request.getBuyerId());

        return this.baseMapper.batchDeleteWithFill(entity, new LambdaQueryWrapper<EnterprisePurchaseRelationDO>()
                .eq(EnterprisePurchaseRelationDO::getBuyerEid, request.getBuyerId())
                .in(EnterprisePurchaseRelationDO::getSellerEid, request.getSellerIds())) > 0;
    }

    @Override
    public Page<PurchaseRelationBO> canPurchaseEnterprisePageList(QueryPurchaseRelationPageListRequest request) {
        Assert.notNull(request.getBuyerChannelId(), "查询企业采购关系：采购渠道商ID参数缺失！");
        Assert.notNull(request.getBuyerEid(), "查询企业采购关系：采购企业ID参数缺失！");

        // 如果销售渠道商ID为空，则查询其所有队员渠道ID列表
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        Page<PurchaseRelationBO> page = this.baseMapper.canPurchaseEnterprisePageList(request.getPage(), request);

        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> sellerEidList = page.getRecords().stream().map(PurchaseRelationBO::getSellerEid).collect(Collectors.toList());
            // 查询已存在的采购关系
            List<EnterprisePurchaseRelationDO> purchaseRelationDOList = this.baseMapper.selectList(new LambdaQueryWrapper<EnterprisePurchaseRelationDO>()
                            .eq(EnterprisePurchaseRelationDO::getBuyerEid, request.getBuyerEid())
                            .in(EnterprisePurchaseRelationDO::getSellerEid, sellerEidList));

            Map<Long,EnterprisePurchaseRelationDO> purchaseRelationMap = purchaseRelationDOList.stream().collect(Collectors.toMap(EnterprisePurchaseRelationDO::getSellerEid, Function.identity(),(key1,key2)->key2));
            page.getRecords().forEach(e -> {
                e.setChooseFlag(false);
                if (purchaseRelationMap.containsKey(e.getSellerEid())) {
                    e.setChooseFlag(true);
                }
            });
        }
        return page;
    }

    @Override
    public List<CountSellerChannelBO> countSellerChannel(QueryPurchaseRelationPageListRequest request) {
        Assert.notNull(request.getBuyerEid(), "查询企业采购关系：该渠道商企业ID参数缺失！");
        request.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
        request.setStatus(EnterpriseStatusEnum.ENABLED.getCode());
        return this.baseMapper.countSellerChannel(request);
    }

    @Override
    public List<EnterprisePurchaseRelationDO> listByBuyerEid(Long buyerEid) {
        LambdaQueryWrapper<EnterprisePurchaseRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterprisePurchaseRelationDO::getBuyerEid, buyerEid);
        return this.list(queryWrapper);
    }

    @Override
    public Boolean checkPurchaseRelation(Long buyerEid, List<Long> sellerEids) {
        // 查询buyerEid采购关系列表
        List<EnterprisePurchaseRelationDO> list = this.baseMapper.selectList(new LambdaQueryWrapper<EnterprisePurchaseRelationDO>()
                .eq(EnterprisePurchaseRelationDO::getBuyerEid, buyerEid));

        List<Long> sellerEidList = list.stream().map(EnterprisePurchaseRelationDO::getSellerEid).collect(Collectors.toList());
        if (CollUtil.isEmpty(sellerEidList) || sellerEidList.size() < sellerEids.size()) {
            return false;
        }
        if (sellerEidList.containsAll(sellerEids)) {
            return true;
        }
        return false;
    }

    @Override
    public Page<EnterpriseDO> querySellerEnterprisePageList(QueryPurchaseRelationPageListRequest request) {
        Assert.notNull(request.getBuyerEid(), "可采购企业ID列表：采购商ID为空！");
        EnterpriseDO enterpriseDO = enterpriseService.getById(request.getBuyerEid());
        Assert.notNull(enterpriseDO, "可采购企业ID列表：查询采购商不存在！");
        Assert.notNull(enterpriseDO.getChannelId(), "可采购企业ID列表：查询采购商渠道ID为空！");

        List<EnterpriseChannelPurchaseRelationDO> channelPurchaseRelationDOList = channelPurchaseRelationService.list(new LambdaQueryWrapper<EnterpriseChannelPurchaseRelationDO>()
                .eq(EnterpriseChannelPurchaseRelationDO::getBuyerChannelId, enterpriseDO.getChannelId()));
        List<Long> sellerChannelIds = channelPurchaseRelationDOList.stream().map(EnterpriseChannelPurchaseRelationDO::getSellerChannelId)
                .filter(channelId -> EnterpriseChannelEnum.INDUSTRY != EnterpriseChannelEnum.getByCode(channelId)).collect(Collectors.toList());
        if (CollUtil.isEmpty(sellerChannelIds)) {
            return request.getPage();
        }

        LambdaQueryWrapper<EnterpriseDO> queryWrapper = new LambdaQueryWrapper<EnterpriseDO>()
                .in(EnterpriseDO::getChannelId, sellerChannelIds)
                .eq(EnterpriseDO::getAuthStatus, EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode())
                .eq(EnterpriseDO::getStatus, EnterpriseStatusEnum.ENABLED.getCode());

        if(Objects.nonNull(request.getSellerEid()) && request.getSellerEid() != 0){
            queryWrapper.eq(EnterpriseDO::getId,request.getSellerEid());
        }
        if(StrUtil.isNotEmpty(request.getSellerName())){
            queryWrapper.like(EnterpriseDO::getName,request.getSellerName());
        }

        return enterpriseService.page(request.getPage(), queryWrapper);
    }
}
