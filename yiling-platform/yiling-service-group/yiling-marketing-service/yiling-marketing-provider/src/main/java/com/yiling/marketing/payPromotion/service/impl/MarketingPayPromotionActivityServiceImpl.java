package com.yiling.marketing.payPromotion.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.dao.MarketingPayPromotionActivityMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayCalculateRuleDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayEnterpriseGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionActivityDO;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayBuyerLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayCalculateRuleService;
import com.yiling.marketing.payPromotion.service.MarketingPayEnterpriseGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPlatformGoodsLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromoterMemberLimitService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionActivityService;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;
import com.yiling.marketing.paypromotion.dto.PayPromotionActivityDTO;
import com.yiling.marketing.paypromotion.dto.request.AddPayPromotionActivityRequest;
import com.yiling.marketing.paypromotion.dto.request.QueryPayPromotionActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 支付促销主表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
@Slf4j
public class MarketingPayPromotionActivityServiceImpl extends BaseServiceImpl<MarketingPayPromotionActivityMapper, MarketingPayPromotionActivityDO> implements MarketingPayPromotionActivityService {
    @Autowired
    MarketingPayBuyerLimitService marketingPayBuyerLimitService;

    @Autowired
    MarketingPayCalculateRuleService marketingPayCalculateRuleService;

    @Autowired
    MarketingPayEnterpriseGoodsLimitService marketingPayEnterpriseGoodsLimitService;

    @Autowired
    MarketingPayMemberLimitService marketingPayMemberLimitService;

    @Autowired
    MarketingPayPlatformGoodsLimitService marketingPayPlatformGoodsLimitService;

    @Autowired
    MarketingPayPromoterMemberLimitService marketingPayPromoterMemberLimitService;

    @Autowired
    MarketingPayPromotionSellerLimitService marketingPayPromotionSellerLimitService;

    @Override
    public Page<PayPromotionActivityDTO> pageList(QueryPayPromotionActivityPageRequest request) {
        return this.baseMapper.pageList(request.getPage(), request);
    }

    @Override
    public Long saveBasic(AddPayPromotionActivityRequest request) {
        MarketingPayPromotionActivityDO promotionActivityDO = PojoUtils.map(request, MarketingPayPromotionActivityDO.class);
        saveOrUpdate(promotionActivityDO);
        return promotionActivityDO.getId();
    }

    @Override
    public List<MarketingPayPromotionActivityDO> listEffectiveActivity(Integer sponsorType, Date time, List<Long> eidList) {
        return this.baseMapper.listEffectiveActivity(sponsorType, time, eidList);
    }

    @Override
    public int addRecordTimes(Long activityId, int times, Long opUserId, Date opTime) {
        return this.baseMapper.addRecordTimes(activityId, times, opUserId, opTime);
    }

    @Override
    public PayPromotionActivityDTO copy(CopyStrategyRequest request) {
        // 1. 新增主表信息
        MarketingPayPromotionActivityDO activityDO = this.getById(request.getId());
        Long oldId = request.getId();
        activityDO.setId(null);
        activityDO.setOpUserId(request.getOpUserId());
        activityDO.setOpTime(request.getOpTime());
        activityDO.setStatus(4);
        activityDO.setTotalNum(BigDecimal.ZERO);
        this.save(activityDO);

        // 2.商家范围类型（1-全部商家；2-指定商家；）
        if (StrategyConditionSellerTypeEnum.getByType(activityDO.getConditionSellerType()) == StrategyConditionSellerTypeEnum.ASSIGN) {
            List<MarketingPayPromotionSellerLimitDO> sellerLimitDOList = marketingPayPromotionSellerLimitService.listByActivityId(oldId);
            for (MarketingPayPromotionSellerLimitDO sellerLimitDO : sellerLimitDOList) {
                sellerLimitDO.setId(null);
                sellerLimitDO.setMarketingPayId(activityDO.getId());
                sellerLimitDO.setOpUserId(request.getOpUserId());
                sellerLimitDO.setOpTime(request.getOpTime());
            }
            marketingPayPromotionSellerLimitService.saveBatch(sellerLimitDOList);
        }

        // 3.商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(activityDO.getConditionGoodsType());
        if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
            List<MarketingPayEnterpriseGoodsLimitDO> marketingPayEnterpriseGoodsLimitDOS = marketingPayEnterpriseGoodsLimitService.listByActivityIdIdList(oldId);
            for (MarketingPayEnterpriseGoodsLimitDO sellerLimitDO : marketingPayEnterpriseGoodsLimitDOS) {
                sellerLimitDO.setId(null);
                sellerLimitDO.setMarketingPayId(activityDO.getId());
                sellerLimitDO.setOpUserId(request.getOpUserId());
                sellerLimitDO.setOpTime(request.getOpTime());
            }
            marketingPayEnterpriseGoodsLimitService.saveBatch(marketingPayEnterpriseGoodsLimitDOS);
        } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
            List<MarketingPayPlatformGoodsLimitDO> marketingPayEnterpriseGoodsLimitDOS = marketingPayPlatformGoodsLimitService.listByActivityId(oldId);
            for (MarketingPayPlatformGoodsLimitDO sellerLimitDO : marketingPayEnterpriseGoodsLimitDOS) {
                sellerLimitDO.setId(null);
                sellerLimitDO.setMarketingPayId(activityDO.getId());
                sellerLimitDO.setOpUserId(request.getOpUserId());
                sellerLimitDO.setOpTime(request.getOpTime());
            }
            marketingPayPlatformGoodsLimitService.saveBatch(marketingPayEnterpriseGoodsLimitDOS);
        }

        // 4.商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
        StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(activityDO.getConditionBuyerType());
        if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
            List<MarketingPayBuyerLimitDO> marketingPayBuyerLimitDOS = marketingPayBuyerLimitService.listByActivityIdList(Arrays.asList(oldId));
            for (MarketingPayBuyerLimitDO sellerLimitDO : marketingPayBuyerLimitDOS) {
                sellerLimitDO.setId(null);
                sellerLimitDO.setMarketingPayId(activityDO.getId());
                sellerLimitDO.setOpUserId(request.getOpUserId());
                sellerLimitDO.setOpTime(request.getOpTime());
            }
            marketingPayBuyerLimitService.saveBatch(marketingPayBuyerLimitDOS);
        } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
            // 5.指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）
            StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(activityDO.getConditionUserType());
            if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                List<Integer> conditionUserMemberTypeList = JSON.parseArray(activityDO.getConditionUserMemberType(), Integer.class);
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                    List<MarketingPayMemberLimitDO> marketingPayMemberLimitDOS = marketingPayMemberLimitService.listMemberByActivityId(oldId);
                    for (MarketingPayMemberLimitDO sellerLimitDO : marketingPayMemberLimitDOS) {
                        sellerLimitDO.setId(null);
                        sellerLimitDO.setMarketingPayId(activityDO.getId());
                        sellerLimitDO.setOpUserId(request.getOpUserId());
                        sellerLimitDO.setOpTime(request.getOpTime());
                    }
                    marketingPayMemberLimitService.saveBatch(marketingPayMemberLimitDOS);
                }
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                    List<MarketingPayPromoterMemberLimitDO> marketingPayPromoterMemberLimitDOS = marketingPayPromoterMemberLimitService.listByActivityIdAndEidList(oldId);
                    for (MarketingPayPromoterMemberLimitDO sellerLimitDO : marketingPayPromoterMemberLimitDOS) {
                        sellerLimitDO.setId(null);
                        sellerLimitDO.setMarketingPayId(activityDO.getId());
                        sellerLimitDO.setOpUserId(request.getOpUserId());
                        sellerLimitDO.setOpTime(request.getOpTime());
                    }
                    marketingPayPromoterMemberLimitService.saveBatch(marketingPayPromoterMemberLimitDOS);
                }
            }
        }

        List<MarketingPayCalculateRuleDO> marketingPayCalculateRuleDOS = marketingPayCalculateRuleService.listRuleByActivityId(oldId);
        for (MarketingPayCalculateRuleDO sellerLimitDO : marketingPayCalculateRuleDOS) {
            sellerLimitDO.setId(null);
            sellerLimitDO.setMarketingPayId(activityDO.getId());
            sellerLimitDO.setOpUserId(request.getOpUserId());
            sellerLimitDO.setOpTime(request.getOpTime());
        }
        marketingPayCalculateRuleService.saveBatch(marketingPayCalculateRuleDOS);
        return PojoUtils.map(activityDO,PayPromotionActivityDTO.class);
    }


}
