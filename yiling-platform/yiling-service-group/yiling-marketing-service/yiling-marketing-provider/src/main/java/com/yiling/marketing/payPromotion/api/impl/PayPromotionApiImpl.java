package com.yiling.marketing.payPromotion.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayCalculateRuleDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionParticipateDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.handler.PayPromotionHandler;
import com.yiling.marketing.payPromotion.service.MarketingPayCalculateRuleService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionActivityService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionParticipateService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;
import com.yiling.marketing.paypromotion.api.PayPromotionActivityApi;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityEidOrGoodsIdDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionCalculateRuleDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionParticipateDTO;
import com.yiling.marketing.paypromotion.dto.PayPromotionSellerLimitActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.AddPayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPaypromotionSellerLimitPageRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRecordRequest;
import com.yiling.marketing.paypromotion.dto.request.SavePayPromotionRulesActivityRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseAuthStatusEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * @author: shixing.sun
 * @date: 2022/9/21
 */
@DubboService
public class PayPromotionApiImpl implements PayPromotionActivityApi {

    @Autowired
    private MarketingPayPromotionActivityService promotionActivityService;

    @Autowired
    private MarketingPayCalculateRuleService marketingPayCalculateRuleService;

    @Autowired
    private EnterpriseApi enterpriseApi;

    @Autowired
    private MarketingPayPromotionSellerLimitService payPromotionSellerLimitService;

    @Autowired
    MarketingPayPromotionParticipateService marketingPayPromotionParticipateService;

    @Autowired
    private PayPromotionHandler payPromotionHandler;



    @Override
    public PayPromotionActivityDTO getById(Long id) {
        return PojoUtils.map(promotionActivityService.getById(id),PayPromotionActivityDTO.class);
    }

    @Override
    public List<PayPromotionActivityDTO> getByIds(List<Long> ids) {
        LambdaQueryWrapper<MarketingPayPromotionActivityDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(MarketingPayPromotionActivityDO::getId,ids);
        queryWrapper.select(MarketingPayPromotionActivityDO::getId,MarketingPayPromotionActivityDO::getName,MarketingPayPromotionActivityDO::getSponsorType);
        return PojoUtils.map(promotionActivityService.list(queryWrapper),PayPromotionActivityDTO.class);
    }

    @Override
    public Page<PayPromotionActivityDTO> pageList(QueryPayPromotionActivityPageRequest request) {
        Page<PayPromotionActivityDTO> doPage = promotionActivityService.pageList(request);
        return PojoUtils.map(doPage, PayPromotionActivityDTO.class);
    }

    @Override
    public Long saveBasic(AddPayPromotionActivityRequest request) {
        return promotionActivityService.saveBasic(request);
    }

    @Override
    public boolean saveAll(SavePayPromotionActivityRequest request) {
        MarketingPayPromotionActivityDO promotionActivityDO = PojoUtils.map(request, MarketingPayPromotionActivityDO.class);
        promotionActivityDO.setConditionEffective(1);
        promotionActivityService.saveOrUpdate(promotionActivityDO);
        // 先删除再新增
        List<SavePayPromotionRulesActivityRequest> calculateRules = request.getCalculateRules();
        MarketingPayCalculateRuleDO marketingPayCalculateRuleDO = new MarketingPayCalculateRuleDO();
        marketingPayCalculateRuleDO.setOpUserId(request.getOpUserId());
        QueryWrapper<MarketingPayCalculateRuleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketingPayCalculateRuleDO::getMarketingPayId, promotionActivityDO.getId());
        marketingPayCalculateRuleService.batchDeleteWithFill(marketingPayCalculateRuleDO,queryWrapper);
        List<MarketingPayCalculateRuleDO> marketingPayCalculateRuleDOS = PojoUtils.map(calculateRules, MarketingPayCalculateRuleDO.class);
        marketingPayCalculateRuleDOS.forEach(entity -> {
            entity.setMarketingPayId(request.getId());
        });
        marketingPayCalculateRuleService.saveBatch(marketingPayCalculateRuleDOS);
        return true;
    }

    @Override
    public boolean stop(StopStrategyRequest request) {
        MarketingPayPromotionActivityDO promotionActivityDO = PojoUtils.map(request, MarketingPayPromotionActivityDO.class);
        return promotionActivityService.updateById(promotionActivityDO);
    }

    @Override
    public boolean addSellerLimiti(AddStrategySellerLimitRequest request) {
        List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(request.getEidList());
        List<MarketingPayPromotionSellerLimitDO> sellerLimitDOList = new ArrayList<>();
        for (EnterpriseDTO enterpriseDTO : enterpriseDTOList) {
            MarketingPayPromotionSellerLimitDO sellerLimitDO = new MarketingPayPromotionSellerLimitDO();
            sellerLimitDO.setMarketingPayId(request.getMarketingStrategyId());
            sellerLimitDO.setEid(enterpriseDTO.getId());
            sellerLimitDO.setDelFlag(0);
            sellerLimitDO.setEname(enterpriseDTO.getName());
            sellerLimitDO.setOpUserId(request.getOpUserId());
            sellerLimitDO.setOpTime(request.getOpTime());
            sellerLimitDOList.add(sellerLimitDO);
        }
        return payPromotionSellerLimitService.saveBatch(sellerLimitDOList);
    }

    @Override
    public boolean deletePayPromotionSeller(DeleteStrategySellerLimitRequest request) {
        MarketingPayPromotionSellerLimitDO sellerLimitDO = new MarketingPayPromotionSellerLimitDO();
        sellerLimitDO.setDelFlag(1);
        sellerLimitDO.setOpUserId(request.getOpUserId());
        sellerLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        wrapper.lambda().in(MarketingPayPromotionSellerLimitDO::getEid, request.getEidList());
        return payPromotionSellerLimitService.batchDeleteWithFill(sellerLimitDO, wrapper) > 0;
    }

    @Override
    public Page<PayPromotionSellerLimitActivityDTO> PayPromotionSellerPageList(QueryPaypromotionSellerLimitPageRequest request) {
        boolean isAddressNull = StringUtils.isBlank(request.getEname()) && Objects.isNull(request.getProvinceCode()) && Objects.isNull(request.getCityCode()) && Objects.isNull(request.getRegionCode());
        QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, request.getMarketingPayId());
        if (Objects.isNull(request.getEid()) && isAddressNull) {
            return PojoUtils.map(payPromotionSellerLimitService.page(request.getPage(), wrapper), PayPromotionSellerLimitActivityDTO.class);
        } else if (Objects.nonNull(request.getEid()) && isAddressNull) {
            if (Objects.nonNull(request.getEid())) {
                wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getEid, request.getEid());
            }
            return PojoUtils.map(payPromotionSellerLimitService.page(request.getPage(), wrapper), PayPromotionSellerLimitActivityDTO.class);
        } else {
            List<MarketingPayPromotionSellerLimitDO> sellerLimitDOList = payPromotionSellerLimitService.list(wrapper);
            List<Long> eidList = sellerLimitDOList.stream().map(MarketingPayPromotionSellerLimitDO::getEid).collect(Collectors.toList());
            QueryEnterprisePageListRequest enterprisePageListRequest = PojoUtils.map(request, QueryEnterprisePageListRequest.class);
            enterprisePageListRequest.setAuthStatus(EnterpriseAuthStatusEnum.AUTH_SUCCESS.getCode());
            enterprisePageListRequest.setStatus(EnableStatusEnum.ENABLED.getCode());
            // 仅能添加开通B2B、商业类型的
            enterprisePageListRequest.setMallFlag(1);
            enterprisePageListRequest.setIds(eidList);
            if (ObjectUtil.isNotNull(request.getEid()) && request.getEid() != 0) {
                enterprisePageListRequest.setId(request.getEid());
            }
            if (StringUtils.isNotBlank(request.getEname())) {
                enterprisePageListRequest.setName(request.getEname());
            }
            if (Objects.nonNull(request.getProvinceCode())) {
                enterprisePageListRequest.setProvinceCode(request.getProvinceCode());
            }
            if (Objects.nonNull(request.getCityCode())) {
                enterprisePageListRequest.setCityCode(request.getCityCode());
            }
            if (Objects.nonNull(request.getRegionCode())) {
                enterprisePageListRequest.setRegionCode(request.getRegionCode());
            }
            List<Integer> inTypeList = Lists.newArrayList();
            inTypeList.add(EnterpriseTypeEnum.BUSINESS.getCode());
            List<Long> idList = enterpriseApi.listSubEids(Constants.YILING_EID);
            idList.add(Constants.YILING_EID);
            enterprisePageListRequest.setNotInIds(idList);
            enterprisePageListRequest.setInTypeList(inTypeList);
            Page<EnterpriseDTO> enterprisePage = enterpriseApi.pageList(enterprisePageListRequest);
            Page<MarketingPayPromotionSellerLimitDO> doPage = PojoUtils.map(enterprisePage, MarketingPayPromotionSellerLimitDO.class);
            List<Long> eidEnterpriseList = enterprisePage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(eidEnterpriseList)) {
                QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper1 = new QueryWrapper<>();
                wrapper1.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, request.getMarketingPayId());
                if (CollUtil.isNotEmpty(eidList)) {
                    wrapper1.lambda().in(MarketingPayPromotionSellerLimitDO::getEid, eidList);
                }
                List<MarketingPayPromotionSellerLimitDO> strategySellerLimitDOList = payPromotionSellerLimitService.list(wrapper1);
                doPage.setRecords(strategySellerLimitDOList);
            }
            return PojoUtils.map(doPage, PayPromotionSellerLimitActivityDTO.class);
        }
    }

    @Override
    public List<PayPromotionSellerLimitActivityDTO> listSellerByActivityId(Long marketingStrategyId) {
        QueryWrapper<MarketingPayPromotionSellerLimitDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId,marketingStrategyId);
        return PojoUtils.map(payPromotionSellerLimitService.list(objectQueryWrapper),PayPromotionSellerLimitActivityDTO.class);
    }

    @Override
    public List<PayPromotionCalculateRuleDTO> getPayCalculateRuleList(Long marketingStrategyId) {
        QueryWrapper<MarketingPayCalculateRuleDO> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.lambda().eq(MarketingPayCalculateRuleDO::getMarketingPayId,marketingStrategyId);
        return PojoUtils.map(marketingPayCalculateRuleService.list(objectQueryWrapper),PayPromotionCalculateRuleDTO.class);
    }

    @Override
    public Page<PayPromotionParticipateDTO> getPayPromotionParticipateById(QueryPayPromotionActivityPageRequest request) {
        QueryWrapper<MarketingPayPromotionParticipateDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketingPayPromotionParticipateDO::getMarketingPayId,request.getId());
        return PojoUtils.map(marketingPayPromotionParticipateService.page(request.getPage(),queryWrapper), PayPromotionParticipateDTO.class);
    }

    @Override
    public boolean savePayPromotionRecord(List<SavePayPromotionRecordRequest> requestList) {
        return payPromotionHandler.savePayPromotionRecord(requestList);
    }

    @Override
    public PayPromotionActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long activityId, Long buyerEid, Long sellerEid) {
        return payPromotionHandler.getGoodsListPageByActivityId(activityId, buyerEid, sellerEid);
    }

    @Override
    public PayPromotionActivityDTO copy(CopyStrategyRequest request) {
        return promotionActivityService.copy(request);
    }

}
