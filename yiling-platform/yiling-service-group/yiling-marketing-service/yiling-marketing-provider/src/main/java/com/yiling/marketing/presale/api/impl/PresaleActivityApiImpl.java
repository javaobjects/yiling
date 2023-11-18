package com.yiling.marketing.presale.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.presale.api.PresaleActivityApi;
import com.yiling.marketing.presale.dao.MarketingPresaleActivityMapper;
import com.yiling.marketing.presale.dao.MarketingPresaleBuyRecordMapper;
import com.yiling.marketing.presale.dao.MarketingPresaleBuyerLimitMapper;
import com.yiling.marketing.presale.dao.MarketingPresaleMemberLimitMapper;
import com.yiling.marketing.presale.dao.MarketingPresalePromoterMemberLimitMapper;
import com.yiling.marketing.presale.dto.PresaleActivityBuyRecorderDTO;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.PresaleActivityGoodsDTO;
import com.yiling.marketing.presale.dto.PresaleActivityOrderDTO;
import com.yiling.marketing.presale.dto.PresaleRelativeDTO;
import com.yiling.marketing.presale.dto.request.AddPresaleActivityRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleInfoRequest;
import com.yiling.marketing.presale.dto.request.QueryPresaleOrderRequest;
import com.yiling.marketing.presale.dto.request.SavePresaleActivityRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyRecordDO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyerLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresaleGoodsLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresaleMemberLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresalePromoterMemberLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleActivityService;
import com.yiling.marketing.presale.service.MarketingPresaleBuyRecordService;
import com.yiling.marketing.presale.service.MarketingPresaleBuyerLimitService;
import com.yiling.marketing.presale.service.MarketingPresaleGoodsLimitService;
import com.yiling.marketing.presale.service.MarketingPresaleMemberLimitService;
import com.yiling.marketing.presale.service.MarketingPresalePromoterMemberLimitService;
import com.yiling.marketing.promotion.dto.PromotionActivityDTO;
import com.yiling.marketing.promotion.dto.request.PromotionBuyRecord;
import com.yiling.marketing.promotion.dto.request.PromotionSaveBuyRecordRequest;
import com.yiling.marketing.strategy.api.StrategyActivityApi;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyCycleLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyAmountLadderDO;
import com.yiling.marketing.strategy.entity.StrategyCycleLadderDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyActivityService;
import com.yiling.marketing.strategy.service.StrategyAmountLadderService;
import com.yiling.marketing.strategy.service.StrategyCycleLadderService;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.CurrentMemberDTO;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode.BEGINTIME_AFTER_BEGINTIME;
import static com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode.ENDTIME_AFTER_ENDTIME;
import static com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode.RATIO_NOT_MEET_100;

/**
 * 营销活动Api(策略满赠)
 *
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PresaleActivityApiImpl implements PresaleActivityApi {

    private final StrategyActivityService activityService;

    private final StrategyAmountLadderService amountLadderService;

    private final StrategyCycleLadderService cycleLadderService;

    private final MarketingPresaleActivityService presaleActivityService;

    private final MarketingPresaleGoodsLimitService presaleGoodsLimitService;

    private final MarketingPresaleBuyerLimitService presaleBuyerLimitService;

    private final MarketingPresaleMemberLimitService presaleMemberLimitService;

    private final MarketingPresalePromoterMemberLimitService promoterMemberLimitService;

    private final MarketingPresaleActivityMapper presaleActivityMapper;

    private final MarketingPresaleBuyRecordService marketingPresaleBuyRecordService;

    private final MarketingPresaleBuyRecordMapper buyRecordMapper;


    private final MarketingPresaleBuyerLimitMapper buyerLimitMapper;

    private final MarketingPresalePromoterMemberLimitMapper promoterMapper;

    private final MarketingPresaleMemberLimitMapper memberLimitMapper;

    @DubboReference
    private EnterpriseApi enterpriseApi;

    @DubboReference
    private MemberApi memberApi;

    @DubboReference
    private OrderFirstInfoApi firstInfoApi;


    @Override
    public PresaleActivityDTO save(AddPresaleActivityRequest request) {
        MarketingPresaleActivityDO presaleActivityDO = PojoUtils.map(request, MarketingPresaleActivityDO.class);
        checkPresaleInfo(presaleActivityDO);
        presaleActivityService.saveOrUpdate(presaleActivityDO);
        return PojoUtils.map(presaleActivityDO, PresaleActivityDTO.class);
    }

    private void checkPresaleInfo(MarketingPresaleActivityDO presaleActivityDO) {
        if (presaleActivityDO.getBear() == 1) {
            presaleActivityDO.setPlatformRatio(new BigDecimal(100));
            presaleActivityDO.setBusinessRatio(new BigDecimal(0));
        }
        if (presaleActivityDO.getBear() == 2) {
            presaleActivityDO.setPlatformRatio(new BigDecimal(0));
            presaleActivityDO.setBusinessRatio(new BigDecimal(100));
        }
        if (presaleActivityDO.getBear() == 3) {
            boolean meet100 = (presaleActivityDO.getPlatformRatio().add(presaleActivityDO.getBusinessRatio())).compareTo(new BigDecimal(100)) == 0;
            if (!meet100) {
                log.info("平台承担和商家承担比例分别是=====" + presaleActivityDO.getPlatformRatio() + "" + presaleActivityDO.getBusinessRatio());
                throw new BusinessException(RATIO_NOT_MEET_100);
            }
        }
        if (ObjectUtil.isNotNull(presaleActivityDO.getFinalPayBeginTime()) && ObjectUtil.isNotNull(presaleActivityDO.getBeginTime())) {
            if (presaleActivityDO.getFinalPayBeginTime().before(presaleActivityDO.getBeginTime())) {
                throw new BusinessException(BEGINTIME_AFTER_BEGINTIME);
            }
        }
        if (ObjectUtil.isNotNull(presaleActivityDO.getFinalPayEndTime()) && ObjectUtil.isNotNull(presaleActivityDO.getEndTime())) {
            if (presaleActivityDO.getFinalPayEndTime().before(presaleActivityDO.getEndTime())) {
                throw new BusinessException(ENDTIME_AFTER_ENDTIME);
            }
        }
    }

    @Override
    public boolean saveAll(SavePresaleActivityRequest request) {
        return activityService.saveAllForPresale(request);
    }

    @Override
    public StrategyActivityDTO copy(CopyStrategyRequest request) {
        StrategyActivityDO activityDO = activityService.copy(request);
        return PojoUtils.map(activityDO, StrategyActivityDTO.class);
    }

    @Override
    public boolean stop(StopStrategyRequest request) {
        return activityService.stop(request);
    }

    @Override
    public Page<StrategyActivityDTO> pageList(QueryStrategyActivityPageRequest request) {
        Page<StrategyActivityDO> doPage = activityService.pageList(request);
        return PojoUtils.map(doPage, StrategyActivityDTO.class);
    }

    @Override
    public StrategyActivityDTO queryById(Long id) {
        StrategyActivityDO strategyActivityDO = activityService.getById(id);
        return PojoUtils.map(strategyActivityDO, StrategyActivityDTO.class);
    }

    @Override
    public List<StrategyAmountLadderDTO> listAmountLadderByActivityId(Long strategyActivityId) {
        List<StrategyAmountLadderDO> doList = amountLadderService.listAmountLadderByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyAmountLadderDTO.class);
    }

    @Override
    public Long presaleActivityApi(Long currentUserId, Long id) {
        MarketingPresaleActivityDO presaleActivityDO = presaleActivityService.getById(id);
        presaleActivityDO.setOpTime(new Date());
        presaleActivityDO.setOpUserId(currentUserId);
        presaleActivityDO.setId(null);
        presaleActivityDO.setStatus(4);
        presaleActivityService.save(presaleActivityDO);
        QueryWrapper<MarketingPresaleGoodsLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getMarketingStrategyId, id);
        queryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getDelFlag, 0);
        List<MarketingPresaleGoodsLimitDO> presaleGoodsLimitDOS = presaleGoodsLimitService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(presaleGoodsLimitDOS)) {
            presaleGoodsLimitDOS.forEach(item -> {
                item.setOpTime(new Date());
                item.setOpUserId(currentUserId);
                item.setMarketingStrategyId(presaleActivityDO.getId());
            });
            presaleGoodsLimitService.saveBatch(presaleGoodsLimitDOS);
        }

        QueryWrapper<MarketingPresaleBuyerLimitDO> BuyerLimitDOQueryWrapper = new QueryWrapper<>();
        BuyerLimitDOQueryWrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getMarketingPresaleId, id);
        BuyerLimitDOQueryWrapper.lambda().eq(MarketingPresaleBuyerLimitDO::getDelFlag, 0);
        List<MarketingPresaleBuyerLimitDO> buyerLimitDOS = presaleBuyerLimitService.list(BuyerLimitDOQueryWrapper);
        if (CollectionUtils.isNotEmpty(buyerLimitDOS)) {
            buyerLimitDOS.forEach(item -> {
                item.setOpTime(new Date());
                item.setOpUserId(currentUserId);
                item.setMarketingPresaleId(presaleActivityDO.getId());
            });
            presaleBuyerLimitService.saveBatch(buyerLimitDOS);
        }

        QueryWrapper<MarketingPresaleMemberLimitDO> memberLimitDOQueryWrapper = new QueryWrapper<>();
        memberLimitDOQueryWrapper.lambda().eq(MarketingPresaleMemberLimitDO::getMarketingPresaleId, id);
        memberLimitDOQueryWrapper.lambda().eq(MarketingPresaleMemberLimitDO::getDelFlag, 0);
        List<MarketingPresaleMemberLimitDO> memberLimitDOS = presaleMemberLimitService.list(memberLimitDOQueryWrapper);
        if (CollectionUtils.isNotEmpty(memberLimitDOS)) {
            memberLimitDOS.forEach(item -> {
                item.setOpTime(new Date());
                item.setOpUserId(currentUserId);
                item.setMarketingPresaleId(presaleActivityDO.getId());
            });
            presaleMemberLimitService.saveBatch(memberLimitDOS);
        }

        QueryWrapper<MarketingPresalePromoterMemberLimitDO> promoterWapper = new QueryWrapper<>();
        promoterWapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getMarketingPresaleId, id);
        promoterWapper.lambda().eq(MarketingPresalePromoterMemberLimitDO::getDelFlag, 0);
        List<MarketingPresalePromoterMemberLimitDO> promoterMemberLimitDOS = promoterMemberLimitService.list(promoterWapper);
        if (CollectionUtils.isNotEmpty(promoterMemberLimitDOS)) {
            promoterMemberLimitDOS.forEach(item -> {
                item.setOpTime(new Date());
                item.setOpUserId(currentUserId);
                item.setMarketingPresaleId(presaleActivityDO.getId());
            });
            promoterMemberLimitService.saveBatch(promoterMemberLimitDOS);
        }
        return presaleActivityDO.getId();
    }

    @Override
    public Boolean presaleOrderCallback(PromotionSaveBuyRecordRequest request) {
        log.info("presaleOrderCallback=====>" + JSONUtil.toJsonStr(request));
        List<PromotionBuyRecord> buyRecordList = request.getBuyRecordList();
        List<MarketingPresaleBuyRecordDO> presaleBuyRecordDOS = new ArrayList<>();
        buyRecordList.forEach(item -> {
            MarketingPresaleBuyRecordDO presaleBuyRecordDO = PojoUtils.map(item, MarketingPresaleBuyRecordDO.class);
            presaleBuyRecordDO.setMarketingPresaleId(item.getPromotionActivityId());
            presaleBuyRecordDOS.add(presaleBuyRecordDO);
        });
        return marketingPresaleBuyRecordService.saveBatch(presaleBuyRecordDOS);
    }

    @Override
    public List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEid(QueryPresaleInfoRequest request) {
        return presaleActivityService.getPresaleInfoByGoodsIdAndBuyEid(request);
    }


    @Override
    public List<PresaleActivityGoodsDTO> getPresaleInfoByGoodsIdAndBuyEidNoNum(QueryPresaleInfoRequest request) {
        return presaleActivityService.getPresaleInfoByGoodsIdAndBuyEidNoNum(request);
    }

    @Override
    public Page<PresaleActivityOrderDTO> queryOrderInfoByPresaleId(QueryPresaleOrderRequest request) {
        return presaleActivityService.queryOrderInfoByPresaleId(request);
    }

    @Override
    public Map<Long, PresaleActivityDTO> batchQueryByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return new HashMap<>();
        }
        Map<Long, PresaleActivityDTO> result = new HashMap<>();
        List<MarketingPresaleActivityDO> marketingPresaleActivityDOS = presaleActivityService.listByIds(idList);
        if (CollectionUtils.isNotEmpty(marketingPresaleActivityDOS)) {
            List<PresaleActivityDTO> map = PojoUtils.map(marketingPresaleActivityDOS, PresaleActivityDTO.class);
            result = map.stream().collect(Collectors.toMap(PresaleActivityDTO::getId, e -> e));
        }
        return result;
    }
}
