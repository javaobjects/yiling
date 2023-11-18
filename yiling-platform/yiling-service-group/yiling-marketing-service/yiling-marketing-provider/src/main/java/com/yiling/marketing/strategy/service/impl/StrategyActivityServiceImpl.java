package com.yiling.marketing.strategy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveScopeService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.marketing.presale.dao.MarketingPresaleGoodsLimitMapper;
import com.yiling.marketing.presale.dto.PresaleActivityDTO;
import com.yiling.marketing.presale.dto.PresaleGoodsLimitDTO;
import com.yiling.marketing.presale.dto.request.PresaleGoodsLimitRequest;
import com.yiling.marketing.presale.dto.request.SavePresaleActivityRequest;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.entity.MarketingPresaleGoodsLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleActivityService;
import com.yiling.marketing.presale.service.MarketingPresaleGoodsLimitService;
import com.yiling.marketing.strategy.dao.StrategyActivityMapper;
import com.yiling.marketing.strategy.dto.StrategyActivityDTO;
import com.yiling.marketing.strategy.dto.StrategyActivityEidOrGoodsIdDTO;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.CopyStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryGoodsStrategyInfoRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryLotteryStrategyRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityAutoGiveGiftPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveActivityAutoGiveGiftRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyActivityRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyAmountLadderRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyCycleLadderRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyGiftRequest;
import com.yiling.marketing.strategy.dto.request.StopStrategyRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityAutoGiveGiftDO;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyActivityRecordDO;
import com.yiling.marketing.strategy.entity.StrategyAmountLadderDO;
import com.yiling.marketing.strategy.entity.StrategyBuyerLimitDO;
import com.yiling.marketing.strategy.entity.StrategyCycleLadderDO;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.entity.StrategyGiftFailDO;
import com.yiling.marketing.strategy.entity.StrategyMemberLimitDO;
import com.yiling.marketing.strategy.entity.StrategyPlatformGoodsLimitDO;
import com.yiling.marketing.strategy.entity.StrategyPromoterMemberLimitDO;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;
import com.yiling.marketing.strategy.enums.StrategyConditionBuyerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionEnterpriseTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionGoodsTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionSellerTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserMemberTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyConditionUserTypeEnum;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.marketing.strategy.enums.StrategyStatusEnum;
import com.yiling.marketing.strategy.enums.StrategyTypeEnum;
import com.yiling.marketing.strategy.service.StrategyActivityAutoGiveGiftService;
import com.yiling.marketing.strategy.service.StrategyActivityRecordService;
import com.yiling.marketing.strategy.service.StrategyActivityService;
import com.yiling.marketing.strategy.service.StrategyAmountLadderService;
import com.yiling.marketing.strategy.service.StrategyBuyerLimitService;
import com.yiling.marketing.strategy.service.StrategyCycleLadderService;
import com.yiling.marketing.strategy.service.StrategyEnterpriseGoodsLimitService;
import com.yiling.marketing.strategy.service.StrategyGiftFailService;
import com.yiling.marketing.strategy.service.StrategyGiftService;
import com.yiling.marketing.strategy.service.StrategyMemberLimitService;
import com.yiling.marketing.strategy.service.StrategyPlatformGoodsLimitService;
import com.yiling.marketing.strategy.service.StrategyPromoterMemberLimitService;
import com.yiling.marketing.strategy.service.StrategySellerLimitService;
import com.yiling.marketing.strategy.service.StrategyStageMemberEffectService;
import com.yiling.order.order.api.OrderFirstInfoApi;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.member.api.EnterpriseMemberApi;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyRecordApi;
import com.yiling.user.member.api.MemberOrderApi;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode.EXPANSION_MULTIPLIER_TOO_HIGH;
import static com.yiling.marketing.couponactivity.enums.CouponActivityErrorCode.FINAL_PAY_DISCOUNT_AMOUNT_TOO_HIGH;

/**
 * <p>
 * 营销活动主表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyActivityServiceImpl extends BaseServiceImpl<StrategyActivityMapper, StrategyActivityDO> implements StrategyActivityService {

    private final StrategyAmountLadderService amountLadderService;

    private final StrategyCycleLadderService cycleLadderService;

    private final StrategyGiftService giftService;

    private final StrategyActivityRecordService activityRecordService;

    private final StrategySellerLimitService sellerLimitService;

    private final StrategyBuyerLimitService buyerLimitService;

    private final StrategyEnterpriseGoodsLimitService enterpriseGoodsLimitService;

    private final StrategyPlatformGoodsLimitService platformGoodsLimitService;

    private final StrategyMemberLimitService memberLimitService;

    private final StrategyPromoterMemberLimitService promoterMemberLimitService;

    private final StrategyStageMemberEffectService stageMemberEffectService;

    private final CouponActivityService couponActivityService;

    private final StrategyActivityAutoGiveGiftService strategyActivityAutoGiveGiftService;

    private final StrategyGiftFailService strategyGiftFailService;

    private final LotteryActivityTimesService lotteryActivityTimesService;

    private final LotteryActivityService lotteryActivityService;

    private final LotteryActivityGiveScopeService lotteryActivityGiveScopeService;

    private final MarketingPresaleActivityService presaleActivityService;

    private final MarketingPresaleGoodsLimitService presaleGoodsLimitService;

    private final MarketingPresaleGoodsLimitMapper presaleGoodsLimitMapper;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    MemberApi memberApi;

    @DubboReference
    MemberBuyRecordApi memberBuyRecordApi;

    @DubboReference
    EnterpriseMemberApi enterpriseMemberApi;

    @DubboReference
    MemberOrderApi memberOrderApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    OrderFirstInfoApi firstInfoApi;

    @Override
    public StrategyActivityDO save(AddStrategyActivityRequest request) {
        StrategyActivityDO activityDO = PojoUtils.map(request, StrategyActivityDO.class);
        QueryWrapper<StrategyActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityDO::getName, request.getName());
        if (Objects.nonNull(request.getId())) {
            wrapper.lambda().ne(StrategyActivityDO::getId, request.getId());
        }
        List<StrategyActivityDO> activityDOList = this.list(wrapper);
        if (CollUtil.isNotEmpty(activityDOList)) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_NAME_EXISTS);
        }
        if (ObjectUtil.isEmpty(request.getBeginTime()) || ObjectUtil.isEmpty(request.getEndTime())) {
            throw new BusinessException(StrategyErrorCode.BEGINNING_EMPTY);
        }
        if (request.getBeginTime().before(new Date())) {
            throw new BusinessException(StrategyErrorCode.BEGINNING_AFTER_NOE_ERROR);
        }
        if (request.getBeginTime().after(request.getEndTime())) {
            throw new BusinessException(StrategyErrorCode.BEGINNING_AFTER_ENDTIME_ERROR);
        }
        if (Objects.nonNull(request.getId())) {
            this.updateById(activityDO);
        } else {
            this.save(activityDO);
        }
        return activityDO;
    }

    @Override
    public boolean saveAll(SaveStrategyActivityRequest request) {
        QueryWrapper<StrategyActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityDO::getName, request.getName());
        wrapper.lambda().ne(StrategyActivityDO::getId, request.getId());
        List<StrategyActivityDO> activityDOList = this.list(wrapper);
        if (CollUtil.isNotEmpty(activityDOList)) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_NAME_EXISTS);
        }

        StrategyActivityDO activityDO = PojoUtils.map(request, StrategyActivityDO.class);
        this.updateById(activityDO);

        // 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
        if (StrategyTypeEnum.getByType(activityDO.getStrategyType()) == StrategyTypeEnum.ORDER_AMOUNT) {
            List<SaveStrategyAmountLadderRequest> strategyAmountLadderList = request.getStrategyAmountLadderList();
            List<SaveStrategyGiftRequest> giftList = new ArrayList<>();
            for (SaveStrategyAmountLadderRequest amountLadderRequest : strategyAmountLadderList) {
                List<SaveStrategyGiftRequest> strategyGiftList = amountLadderRequest.getStrategyGiftList();
                giftList.addAll(strategyGiftList);
            }
            checkGift(giftList);
            amountLadderService.save(request.getId(), strategyAmountLadderList, request.getOpUserId(), request.getOpTime());
        }
        if (StrategyTypeEnum.getByType(activityDO.getStrategyType()) == StrategyTypeEnum.CYCLE_TIME) {
            List<SaveStrategyCycleLadderRequest> strategyCycleLadderList = request.getStrategyCycleLadderList();
            List<SaveStrategyGiftRequest> giftList = new ArrayList<>();
            for (SaveStrategyCycleLadderRequest cycleLadderRequest : strategyCycleLadderList) {
                List<SaveStrategyGiftRequest> strategyGiftList = cycleLadderRequest.getStrategyGiftList();
                giftList.addAll(strategyGiftList);
            }
            checkGift(giftList);
            cycleLadderService.save(request.getId(), strategyCycleLadderList, request.getOpUserId(), request.getOpTime());
        }
        if (StrategyTypeEnum.getByType(activityDO.getStrategyType()) == StrategyTypeEnum.PURCHASE_MEMBER) {
            List<SaveStrategyGiftRequest> strategyGiftList = request.getStrategyGiftList();
            checkGift(strategyGiftList);
            giftService.save(request.getId(), null, strategyGiftList, request.getOpUserId(), request.getOpTime());
        }
        return true;
    }

    private void checkGift(List<SaveStrategyGiftRequest> giftList) {
        List<SaveStrategyGiftRequest> couponList = giftList.stream().filter(e -> 1 == e.getType() || 2 == e.getType()).collect(Collectors.toList());
        List<Long> couponIdList = couponList.stream().map(SaveStrategyGiftRequest::getGiftId).collect(Collectors.toList());
        Map<Long, CouponActivityDetailDTO> couponActivityDetailMap = couponActivityService.getRemainDtoByActivityIds(couponIdList);
        List<Long> matchedCouponIdList = new ArrayList<>();
        couponActivityDetailMap.forEach((key, value) -> {
            matchedCouponIdList.add(key);
        });
        List<Long> notMatchCoupon = couponIdList.stream().filter(e -> !matchedCouponIdList.contains(e)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(notMatchCoupon)) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_GIFT_UNABLE);
        }

        List<SaveStrategyGiftRequest> lotteryList = giftList.stream().filter(e -> 3 == e.getType()).collect(Collectors.toList());
        List<Long> lotteryIdList = lotteryList.stream().map(SaveStrategyGiftRequest::getGiftId).collect(Collectors.toList());
        for (Long lotteryId : lotteryIdList) {
            LotteryActivityGiveScopeDTO lotteryActivityGiveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(lotteryId);
            if (Objects.isNull(lotteryActivityGiveScopeDTO)) {
                throw new BusinessException(StrategyErrorCode.LOTTERY_INFO_INCOMPLETE);
            }
        }
    }

    @Override
    public StrategyActivityDO copy(CopyStrategyRequest request) {
        // 1. 新增主表信息
        StrategyActivityDO activityDO = this.getById(request.getId());
        Long oldId = activityDO.getId();
        activityDO.setId(null);
        activityDO.setOpUserId(request.getOpUserId());
        activityDO.setOpTime(request.getOpTime());
        activityDO.setStatus(4);
        this.save(activityDO);

        // 2.商家范围类型（1-全部商家；2-指定商家；）
        if (StrategyConditionSellerTypeEnum.getByType(activityDO.getConditionSellerType()) == StrategyConditionSellerTypeEnum.ASSIGN) {
            sellerLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        }

        // 3.商品范围类型（1-全部商品；2-指定平台SKU；3-指定店铺SKU；）
        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(activityDO.getConditionGoodsType());
        if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
            enterpriseGoodsLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
            platformGoodsLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        }

        // 4.商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
        StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(activityDO.getConditionBuyerType());
        if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
            buyerLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
            // 5.指定用户类型（1-全部用户；2-仅普通用户；3-全部会员用户；4-指定方案会员；5-指定推广方会员）
            StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(activityDO.getConditionUserType());
            if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                List<Integer> conditionUserMemberTypeList = JSON.parseArray(activityDO.getConditionUserMemberType(), Integer.class);
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                    memberLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
                }
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                    promoterMemberLimitService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
                }
            }
        }

        // 6. 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
        StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getByType(activityDO.getStrategyType());
        if (strategyTypeEnum == StrategyTypeEnum.ORDER_AMOUNT) {
            amountLadderService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        } else if (strategyTypeEnum == StrategyTypeEnum.CYCLE_TIME) {
            cycleLadderService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        } else if (strategyTypeEnum == StrategyTypeEnum.PURCHASE_MEMBER) {
            stageMemberEffectService.copy(activityDO, oldId, request.getOpUserId(), request.getOpTime());
        }

        return activityDO;
    }

    @Override
    public boolean stop(StopStrategyRequest request) {
        StrategyActivityDO activityDO = PojoUtils.map(request, StrategyActivityDO.class);
        return this.updateById(activityDO);
    }

    @Override
    public Page<StrategyActivityDO> pageList(QueryStrategyActivityPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        Page<StrategyActivityDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageList(objectPage, request);
    }

    @Override
    public Page<MarketingPresaleActivityDO> pageListForPresale(QueryStrategyActivityPageRequest request) {
        if (null != request.getStartTime()) {
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if (null != request.getStopTime()) {
            request.setStopTime(DateUtil.endOfDay(request.getStopTime()));
        }
        Page<StrategyActivityDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return presaleGoodsLimitMapper.pageList(objectPage, request);
    }

    /**
     * 查询出所有的有效活动
     *
     * @param strategyType 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
     * @param platformSelected 选择平台（1-B2B；2-销售助手）逗号隔开
     * @return 有效活动
     */
    @Override
    public List<StrategyActivityDO> listEffectiveStrategy(Integer strategyType, String platformSelected, Integer orderAmountLadderType, Integer orderAmountStatusType) {
        Date now = DateTime.now();
        QueryWrapper<StrategyActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityDO::getType, 1);
        wrapper.lambda().eq(StrategyActivityDO::getStrategyType, strategyType);
        wrapper.lambda().eq(StrategyActivityDO::getStatus, StrategyStatusEnum.ENABLE.getCode());
        wrapper.lambda().le(StrategyActivityDO::getBeginTime, now);
        wrapper.lambda().ge(StrategyActivityDO::getEndTime, now);
        if (StringUtils.isNotBlank(platformSelected)) {
            wrapper.lambda().like(StrategyActivityDO::getPlatformSelected, platformSelected);
        }
        if (Objects.nonNull(orderAmountLadderType)) {
            wrapper.lambda().eq(StrategyActivityDO::getOrderAmountLadderType, orderAmountLadderType);
        }
        if (Objects.nonNull(orderAmountStatusType)) {
            wrapper.lambda().eq(StrategyActivityDO::getOrderAmountStatusType, orderAmountStatusType);
        }
        return this.list(wrapper);
    }

    @Override
    public List<StrategyActivityDO> listEffectiveStrategyByTime(Integer strategyType, String platformSelected, List<Integer> orderAmountLadderTypeList, Integer orderAmountStatusType, Date time) {
        QueryWrapper<StrategyActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityDO::getType, 1);
        wrapper.lambda().eq(StrategyActivityDO::getStrategyType, strategyType);
        wrapper.lambda().eq(StrategyActivityDO::getStatus, StrategyStatusEnum.ENABLE.getCode());
        wrapper.lambda().le(StrategyActivityDO::getBeginTime, time);
        wrapper.lambda().ge(StrategyActivityDO::getEndTime, time);
        if (StringUtils.isNotBlank(platformSelected)) {
            wrapper.lambda().like(StrategyActivityDO::getPlatformSelected, platformSelected);
        }
        if (CollUtil.isNotEmpty(orderAmountLadderTypeList)) {
            // 订单累计金额-阶梯匹配方式(1-按单累计匹配;2-活动结束整体匹配;3-按单匹配)
            wrapper.lambda().in(StrategyActivityDO::getOrderAmountLadderType, orderAmountLadderTypeList);
        }
        if (Objects.nonNull(orderAmountStatusType)) {
            wrapper.lambda().eq(StrategyActivityDO::getOrderAmountStatusType, orderAmountStatusType);
        }
        return this.list(wrapper);
    }

    @Override
    public void strategyActivityAutoJobHandler() {
        List<StrategyActivityDO> strategyActivityDOList = listEffectiveStrategy(StrategyTypeEnum.CYCLE_TIME.getType(), null, null, null);
        List<StrategyActivityDO> cycleLadderWeekStrategyActivityDOList = strategyActivityDOList.stream().filter(e -> 1 == e.getCycleRate()).collect(Collectors.toList());
        List<StrategyActivityDO> cycleLadderMonthStrategyActivityDOList = strategyActivityDOList.stream().filter(e -> 2 == e.getCycleRate()).collect(Collectors.toList());
        // 1.时间周期-按周
        cycleLadderWeekAutoJobHandler(cycleLadderWeekStrategyActivityDOList);
        // 2.时间周期-按月
        cycleLadderMonthAutoJobHandler(cycleLadderMonthStrategyActivityDOList);
    }

    @Override
    public void strategyMemberAutoJobHandler() {
        DateTime now = DateTime.now();
        String giveTime = now.year() + "-" + now.monthStartFromOne() + "-" + now.dayOfMonth();
        // 赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)
        List<Integer> typeList = new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
        }};
        QueryStrategyActivityAutoGiveGiftPageRequest listRequest = new QueryStrategyActivityAutoGiveGiftPageRequest();
        listRequest.setGiveTime(giveTime);
        listRequest.setTypeList(typeList);
        log.info("strategyMemberAutoJobHandler 今天续费需要赠送的赠品信息 查询条件为:[{}]", listRequest);
        List<StrategyActivityAutoGiveGiftDO> activityAutoGiveGiftDOList = strategyActivityAutoGiveGiftService.listByCondition(listRequest);
        if (CollUtil.isEmpty(activityAutoGiveGiftDOList)) {
            activityAutoGiveGiftDOList = new ArrayList<>();
        }

        listRequest.setGiveTime(now.dayOfMonth() + "-all");
        listRequest.setStartTime(new Date());
        List<StrategyActivityAutoGiveGiftDO> allActivityAutoGiveGiftDOList = strategyActivityAutoGiveGiftService.listByCondition(listRequest);
        if (CollUtil.isNotEmpty(allActivityAutoGiveGiftDOList)) {
            activityAutoGiveGiftDOList.addAll(allActivityAutoGiveGiftDOList);
        }
        if (CollUtil.isEmpty(activityAutoGiveGiftDOList)) {
            log.info("strategyMemberAutoJobHandler stop 没有需要自动发放赠品的记录");
            return;
        }

        log.info("strategyMemberAutoJobHandler 今天续费需要赠送的赠品信息为:[{}]", activityAutoGiveGiftDOList);
        List<Long> idList = activityAutoGiveGiftDOList.stream().map(StrategyActivityAutoGiveGiftDO::getMarketingStrategyId).collect(Collectors.toList());
        List<StrategyActivityDO> activityDOList = enableStrategy(idList);
        if (CollUtil.isEmpty(activityDOList)) {
            log.info("strategyMemberAutoJobHandler stop 没有需要自动发放赠品的记录 的有效活动");
            return;
        }

        log.info("strategyMemberAutoJobHandler 有效的活动为:[{}]", activityDOList);
        List<Long> activityIdList = activityDOList.stream().map(StrategyActivityDO::getId).collect(Collectors.toList());
        List<StrategyActivityAutoGiveGiftDO> autoGiveGiftDOList = activityAutoGiveGiftDOList.stream().filter(e -> activityIdList.contains(e.getMarketingStrategyId())).collect(Collectors.toList());
        List<StrategyActivityAutoGiveGiftDO> deleteGiveGiftDOList = activityAutoGiveGiftDOList.stream().filter(e -> !activityIdList.contains(e.getMarketingStrategyId())).collect(Collectors.toList());


        if (CollUtil.isNotEmpty(deleteGiveGiftDOList)) {
            log.info("strategyMemberAutoJobHandler 活动结束,删除定时任务记录:[{}]", deleteGiveGiftDOList);
            List<Long> deleteIdList = deleteGiveGiftDOList.stream().map(StrategyActivityAutoGiveGiftDO::getId).collect(Collectors.toList());
            strategyActivityAutoGiveGiftService.deleteByIdList(deleteIdList, 0L);
        }

        Map<String, List<StrategyActivityAutoGiveGiftDO>> listMap = autoGiveGiftDOList.stream().collect(Collectors.groupingBy(e -> e.getMarketingStrategyId() + "-" + e.getEid() + "-" + e.getMemberId() + "-" + e.getOrderId()));
        listMap.forEach((key, value) -> {
            log.info("strategyMemberAutoJobHandler 开始准备赠送优惠券：giveGiftDOList:[{}]", value);
            StrategyActivityAutoGiveGiftDO activityAutoGiveGiftDO = value.get(0);
            StrategyActivityDO activityDO = this.getById(activityAutoGiveGiftDO.getMarketingStrategyId());

            QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
            activityRecordListRequest.setMarketingStrategyId(activityDO.getId());
            activityRecordListRequest.setStrategyType(activityDO.getStrategyType());
            activityRecordListRequest.setEid(activityAutoGiveGiftDO.getEid());
            activityRecordListRequest.setOrderId(activityAutoGiveGiftDO.getOrderId());
            List<StrategyActivityRecordDO> activityRecordDOList = activityRecordService.listByCondition(activityRecordListRequest);
            if (CollUtil.isEmpty(activityRecordDOList)) {
                Map<String, List<StrategyActivityAutoGiveGiftDO>> stringListMap = value.stream().collect(Collectors.groupingBy(StrategyActivityAutoGiveGiftDO::getGiveTime));
                stringListMap.forEach((k, v) -> {
                    List<StrategyGiftDO> giftDOList = PojoUtils.map(v, StrategyGiftDO.class);
                    sendGift(activityDO, activityAutoGiveGiftDO.getEid(), giftDOList, null, activityAutoGiveGiftDO.getOrderId(), activityAutoGiveGiftDO.getMemberId());
                });
            } else {
                log.info("strategyMemberAutoJobHandler 重复执行 已经赠送过优惠券:[{}]", key);
            }
        });
    }

    private List<StrategyActivityDO> enableStrategy(List<Long> idList) {
        List<StrategyActivityDO> activityDOList = this.listByIds(idList);
        return activityDOList.stream().filter(activityDO -> {
            // 状态：1-启用 2-停用 3-废弃
            Integer status = activityDO.getStatus();
            // 生效开始时间
            Date beginTime = activityDO.getBeginTime();
            Date endTime = activityDO.getEndTime();
            if (1 != status) {
                return false;
            }
            if (endTime.compareTo(new Date()) < 0) {
                return false;
            }
            return beginTime.compareTo(new Date()) <= 0;
        }).collect(Collectors.toList());
    }

    @Override
    public List<StrategyActivityDO> listStopAmountStrategyActivity(Date startTime, Date stopTime) {
        QueryWrapper<StrategyActivityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityDO::getStrategyType, StrategyTypeEnum.ORDER_AMOUNT.getType());
        wrapper.lambda().eq(StrategyActivityDO::getType, 1);
        wrapper.lambda().eq(StrategyActivityDO::getStatus, StrategyStatusEnum.ENABLE.getCode());
        wrapper.lambda().ge(StrategyActivityDO::getEndTime, startTime);
        wrapper.lambda().le(StrategyActivityDO::getEndTime, stopTime);
        wrapper.lambda().eq(StrategyActivityDO::getOrderAmountLadderType, 2);
        return this.list(wrapper);
    }

    @Override
    public List<Long> queryGoodsStrategyInfo(QueryGoodsStrategyInfoRequest request) {
        log.info("queryGoodsStrategyInfo 查询精选商品所有企业是否满赠策略满赠活动 request:[{}]", request);
        Boolean newVisitor = firstInfoApi.checkNewVisitor(request.getBuyerEid(), OrderTypeEnum.B2B);
        List<StrategyActivityDO> effectiveStrategy = listEffectiveStrategy(StrategyTypeEnum.ORDER_AMOUNT.getType(), request.getPlatformSelected().toString(), null, null);

        List<Long> resultGoodsIdList = new ArrayList<>();
        for (StrategyActivityDO strategyActivityDO : effectiveStrategy) {
            if (strategyActivityDO.getConditionOther().contains("1") && !newVisitor) {
                log.info("queryGoodsStrategyInfo 非新客用户，不允许参与新客活动, eid:[{}], 活动id:[{}]", request.getBuyerEid(), strategyActivityDO.getId());
                continue;
            }
            log.info("queryGoodsStrategyInfo 活动id:[{}]", strategyActivityDO.getId());
            List<Long> goodsIdList = new ArrayList<>();
            // 1. 商家范围筛选
            {
                List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(request.getGoodsIdList());
                List<Long> sellerEidList = goodsDTOList.stream().map(GoodsDTO::getEid).collect(Collectors.toList());
                StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDO.getConditionSellerType());
                if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                    List<StrategySellerLimitDO> strategySellerLimitDOList = sellerLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), sellerEidList);
                    if (CollUtil.isEmpty(strategySellerLimitDOList)) {
                        log.info("queryGoodsStrategyInfo match seller fail 活动id:[{}]", strategyActivityDO.getId());
                        continue;
                    } else {
                        List<Long> eidList = strategySellerLimitDOList.stream().map(StrategySellerLimitDO::getEid).collect(Collectors.toList());
                        List<GoodsDTO> goodsList = goodsDTOList.stream().filter(e -> eidList.contains(e.getEid())).collect(Collectors.toList());
                        goodsIdList = goodsList.stream().map(GoodsDTO::getId).collect(Collectors.toList());
                    }
                }
            }
            //            log.info("queryGoodsStrategyInfo match seller");
            // 2. 客户范围筛选
            {
                Long buyerEid = request.getBuyerEid();
                StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(strategyActivityDO.getConditionBuyerType());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
                // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
                if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                    log.info("queryGoodsStrategyInfo match buyer fail 1-工业 2-商业 buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                    continue;
                }

                if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
                    List<StrategyBuyerLimitDO> strategyBuyerLimitDOList = buyerLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), new ArrayList<Long>() {{
                        add(buyerEid);
                    }});
                    if (CollUtil.isEmpty(strategyBuyerLimitDOList)) {
                        log.info("queryGoodsStrategyInfo match buyer fail assign buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
                    StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                    if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                        String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                        if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                            log.info("queryGoodsStrategyInfo match buyer fail range buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }
                    StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                    if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                        boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                        if (enterpriseMemberStatus) {
                            log.info("queryGoodsStrategyInfo match buyer fail common buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                        boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                        if (!enterpriseMemberStatus) {
                            log.info("queryGoodsStrategyInfo match buyer fail all member buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                        List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                        if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                            log.info("queryGoodsStrategyInfo match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }

                        List<Long> promoterIdList = new ArrayList<>();
                        if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                            // 指定推广方会员
                            List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = promoterMemberLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                            promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDO::getEid).collect(Collectors.toList());
                            if (CollUtil.isEmpty(promoterIdList)) {
                                log.info("queryGoodsStrategyInfo match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                                continue;
                            }
                        }

                        List<Long> memberIdList = new ArrayList<>();
                        if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                            // 指定方案会员
                            List<StrategyMemberLimitDO> strategyMemberLimitDOList = memberLimitService.listMemberByActivityId(strategyActivityDO.getId());
                            memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDO::getMemberId).collect(Collectors.toList());
                            if (CollUtil.isEmpty(memberIdList)) {
                                log.info("queryGoodsStrategyInfo match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                                continue;
                            }
                        }
                        QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                        memberListRecordRequest.setMemberIdList(memberIdList);
                        memberListRecordRequest.setPromoterIdList(promoterIdList);
                        memberListRecordRequest.setEid(buyerEid);
                        memberListRecordRequest.setCurrentValid(true);
                        List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                        if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                            log.info("queryGoodsStrategyInfo match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }
                }
            }
            //            log.info("queryGoodsStrategyInfo match buyer");
            // 3. 商品范围筛选
            {
                StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDO.getConditionGoodsType());
                if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ALL) {
                    log.info("queryGoodsStrategyInfo  match goods all goodsIdList:[{}],request:[{}]", goodsIdList, request);
                    if (CollUtil.isNotEmpty(goodsIdList)) {
                        return goodsIdList;
                    } else {
                        return request.getGoodsIdList();
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                    StrategyPlatformGoodsLimitDO platformGoodsLimitDO = platformGoodsLimitService.queryByActivityIdAndSellSpecificationsId(strategyActivityDO.getId(), request.getSellSpecificationsId());
                    if (Objects.isNull(platformGoodsLimitDO)) {
                        log.info("queryGoodsStrategyInfo match goods platform fail 活动id:[{}]", strategyActivityDO.getId());
                        continue;
                    }
                    log.info("queryGoodsStrategyInfo  match goods platform goodsIdList:[{}],request:[{}]", goodsIdList, request);
                    if (CollUtil.isNotEmpty(goodsIdList)) {
                        return goodsIdList;
                    } else {
                        return request.getGoodsIdList();
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                    if (CollUtil.isEmpty(goodsIdList)) {
                        goodsIdList = request.getGoodsIdList();
                    }
                    List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = enterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(strategyActivityDO.getId(), goodsIdList);
                    if (CollUtil.isNotEmpty(enterpriseGoodsLimitDOList)) {
                        List<Long> idList = enterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                        resultGoodsIdList.addAll(idList);
                    }
                }
            }
            log.info("queryGoodsStrategyInfo match goods stop 活动id:[{}]", strategyActivityDO.getId());
        }

        log.info("queryGoodsStrategyInfo 返回的数据为:[{}]", resultGoodsIdList);
        if (CollUtil.isNotEmpty(resultGoodsIdList)) {
            return resultGoodsIdList.stream().distinct().collect(Collectors.toList());
        }
        return resultGoodsIdList;
    }

    @Override
    public List<StrategyActivityDTO> queryGoodsStrategyGift(QueryGoodsStrategyInfoRequest request) {
        log.info("queryGoodsStrategyGift 精选商品查看商品满赠的策略满赠活动 request:[{}]", request);
        Boolean newVisitor = firstInfoApi.checkNewVisitor(request.getBuyerEid(), OrderTypeEnum.B2B);
        List<StrategyActivityDO> effectiveStrategy = listEffectiveStrategy(StrategyTypeEnum.ORDER_AMOUNT.getType(), request.getPlatformSelected().toString(), null, null);

        List<StrategyActivityDTO> strategyActivityDTOList = new ArrayList<>();
        for (StrategyActivityDO strategyActivityDO : effectiveStrategy) {
            if (strategyActivityDO.getConditionOther().contains("1") && !newVisitor) {
                log.info("queryGoodsStrategyGift 非新客用户，不允许参与新客活动, eid:[{}], 活动id:[{}]", request.getBuyerEid(), strategyActivityDO.getId());
                continue;
            }
            log.info("queryGoodsStrategyGift 活动id:[{}]", strategyActivityDO.getId());
            // 1. 商家范围筛选
            {
                Long sellerEid = request.getSellerEid();
                StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDO.getConditionSellerType());
                if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
                    List<StrategySellerLimitDO> strategySellerLimitDOList = sellerLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), new ArrayList<Long>() {{
                        add(sellerEid);
                    }});
                    if (CollUtil.isEmpty(strategySellerLimitDOList)) {
                        log.info("queryGoodsStrategyGift match seller fail 活动id:[{}]", strategyActivityDO.getId());
                        continue;
                    }
                }
            }
            //            log.info("queryGoodsStrategyGift match seller 活动id:[{}]", strategyActivityDO.getId());
            // 2. 客户范围筛选
            {
                Long buyerEid = request.getBuyerEid();
                StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(strategyActivityDO.getConditionBuyerType());
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
                // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
                if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                    log.info("queryGoodsStrategyGift match buyer fail 1-工业 2-商业 buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                    continue;
                }

                if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
                    List<StrategyBuyerLimitDO> strategyBuyerLimitDOList = buyerLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), new ArrayList<Long>() {{
                        add(buyerEid);
                    }});
                    if (CollUtil.isEmpty(strategyBuyerLimitDOList)) {
                        log.info("queryGoodsStrategyGift match buyer fail assign buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
                    StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                    if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                        String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                        if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                            log.info("queryGoodsStrategyGift match buyer fail range buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }
                    StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                    if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                        boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                        if (enterpriseMemberStatus) {
                            log.info("queryGoodsStrategyGift match buyer fail common buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                        boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(buyerEid);
                        if (!enterpriseMemberStatus) {
                            log.info("queryGoodsStrategyGift match buyer fail all member buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                        List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                        if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                            log.info("queryGoodsStrategyGift match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }

                        List<Long> promoterIdList = new ArrayList<>();
                        if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                            // 指定推广方会员
                            List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = promoterMemberLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                            promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDO::getEid).collect(Collectors.toList());
                            if (CollUtil.isEmpty(promoterIdList)) {
                                log.info("queryGoodsStrategyGift match buyer promoter member 4 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                                continue;
                            }
                        }

                        List<Long> memberIdList = new ArrayList<>();
                        if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                            // 指定方案会员
                            List<StrategyMemberLimitDO> strategyMemberLimitDOList = memberLimitService.listMemberByActivityId(strategyActivityDO.getId());
                            memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDO::getMemberId).collect(Collectors.toList());
                            if (CollUtil.isEmpty(memberIdList)) {
                                log.info("queryGoodsStrategyGift match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                                continue;
                            }
                        }
                        QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                        memberListRecordRequest.setMemberIdList(memberIdList);
                        memberListRecordRequest.setPromoterIdList(promoterIdList);
                        memberListRecordRequest.setEid(buyerEid);
                        memberListRecordRequest.setCurrentValid(true);
                        List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                        if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                            log.info("queryGoodsStrategyGift match buyer member 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }
                }
            }
            //            log.info("queryGoodsStrategyGift match buyer 活动id:[{}]", strategyActivityDO.getId());
            // 3. 商品范围筛选
            {
                StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDO.getConditionGoodsType());
                if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                    StrategyPlatformGoodsLimitDO platformGoodsLimitDO = platformGoodsLimitService.queryByActivityIdAndSellSpecificationsId(strategyActivityDO.getId(), request.getSellSpecificationsId());
                    if (Objects.isNull(platformGoodsLimitDO)) {
                        log.info("queryGoodsStrategyGift match goods platform fail 活动id:[{}]", strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                    List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = enterpriseGoodsLimitService.listByActivityIdAndGoodsIdList(strategyActivityDO.getId(), request.getGoodsIdList());
                    if (CollUtil.isEmpty(enterpriseGoodsLimitDOList)) {
                        log.info("queryGoodsStrategyGift match goods enterprise fail 活动id:[{}]", strategyActivityDO.getId());
                        continue;
                    }
                }
            }
            //            log.info("queryGoodsStrategyGift match goods 活动id:[{}]", strategyActivityDO.getId());
            {
                StrategyActivityDTO strategyActivityDTO = PojoUtils.map(strategyActivityDO, StrategyActivityDTO.class);
                List<StrategyAmountLadderDO> amountLadderDOList = amountLadderService.listAmountLadderByActivityId(strategyActivityDO.getId());
                List<StrategyAmountLadderDTO> amountLadderDTOList = PojoUtils.map(amountLadderDOList, StrategyAmountLadderDTO.class);
                for (StrategyAmountLadderDTO strategyAmountLadderDTO : amountLadderDTOList) {
                    List<StrategyGiftDO> strategyGiftDOList = giftService.listGiftByActivityIdAndLadderId(strategyActivityDO.getId(), strategyAmountLadderDTO.getId());
                    List<StrategyGiftDTO> strategyGiftDTOList = PojoUtils.map(strategyGiftDOList, StrategyGiftDTO.class);
                    for (StrategyGiftDTO strategyGiftDTO : strategyGiftDTOList) {
                        if (1 == strategyGiftDTO.getType() || 2 == strategyGiftDTO.getType()) {
                            CouponActivityDetailDTO couponActivityDetailDTO = couponActivityService.getDetailById(strategyGiftDTO.getGiftId());
                            strategyGiftDTO.setGiftName(couponActivityDetailDTO.getName());
                        }
                        if (3 == strategyGiftDTO.getType()) {
                            LotteryActivityDO lotteryActivityDO = lotteryActivityService.getById(strategyGiftDTO.getGiftId());
                            strategyGiftDTO.setGiftName(lotteryActivityDO.getActivityName());
                        }
                    }
                    strategyAmountLadderDTO.setStrategyGiftList(strategyGiftDTOList);
                }
                strategyActivityDTO.setStrategyAmountLadderList(amountLadderDTOList);
                strategyActivityDTOList.add(strategyActivityDTO);
            }
            log.info("queryGoodsStrategyGift get gift 活动id:[{}]", strategyActivityDO.getId());
        }

        log.info("queryGoodsStrategyGift返回的数据为:[{}]", JSONUtil.toJsonStr(strategyActivityDTOList));
        return strategyActivityDTOList;
    }

    /**
     * 根据策略满赠活动和用户以及会员筛选出满足用户条件的活动
     *
     * @param strategyActivityDOList 策略满赠活动集合
     * @param buyerEid 客户id
     * @param isRenew 是否是续费
     * @param memberId 会员id
     * @return
     */
    private List<StrategyActivityDO> matchStrategyBuyer(List<StrategyActivityDO> strategyActivityDOList, Long buyerEid, Boolean isRenew, Long memberId, String orderNo) {
        log.info("matchStrategyBuyer start buyerEid:[{}], strategyActivityDOList:[{}], isRenew:[{}]", buyerEid, JSONUtil.toJsonStr(strategyActivityDOList), isRenew);
        List<StrategyActivityDO> resultDO = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 1).collect(Collectors.toList());

        List<StrategyActivityDO> assignBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 2).collect(Collectors.toList());
        List<StrategyActivityDO> rangeBuyerStrategy = strategyActivityDOList.stream().filter(e -> e.getConditionBuyerType() == 3).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(assignBuyerStrategy)) {
            List<Long> assignIdList = assignBuyerStrategy.stream().map(StrategyActivityDO::getId).collect(Collectors.toList());
            List<StrategyBuyerLimitDO> assignStrategyBuyerLimitDOList = buyerLimitService.listByActivityIdListAndEid(assignIdList, buyerEid);
            if (CollUtil.isNotEmpty(assignStrategyBuyerLimitDOList)) {
                List<Long> assignStrategyIdList = assignStrategyBuyerLimitDOList.stream().map(StrategyBuyerLimitDO::getMarketingStrategyId).collect(Collectors.toList());
                List<StrategyActivityDO> assignBuyerStrategyList = assignBuyerStrategy.stream().filter(e -> assignStrategyIdList.contains(e.getId())).collect(Collectors.toList());
                resultDO.addAll(assignBuyerStrategyList);
            }
        }

        if (CollUtil.isNotEmpty(rangeBuyerStrategy)) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(buyerEid);
            // 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
            if (1 == enterpriseDTO.getType() || 2 == enterpriseDTO.getType()) {
                log.info("matchStrategyBuyer match buyer fail 1-工业 2-商业 buyerEid:[{}]", buyerEid);
                return resultDO;
            }
            for (StrategyActivityDO strategyActivityDO : rangeBuyerStrategy) {
                StrategyConditionEnterpriseTypeEnum conditionEnterpriseTypeEnum = StrategyConditionEnterpriseTypeEnum.getByType(strategyActivityDO.getConditionEnterpriseType());
                if (conditionEnterpriseTypeEnum == StrategyConditionEnterpriseTypeEnum.ASSIGN) {
                    String conditionEnterpriseTypeValue = strategyActivityDO.getConditionEnterpriseTypeValue();
                    if (!conditionEnterpriseTypeValue.contains(enterpriseDTO.getType() + "")) {
                        log.info("matchStrategyBuyer match buyer assign type fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(strategyActivityDO.getConditionUserType());
                if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                    if (isRenew) {
                        log.info("matchStrategyBuyer match buyer common 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                    List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getCurrentValidMemberRecord(buyerEid);
                    if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                        log.info("matchStrategyBuyer match buyer common 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                    List<Long> memberIdList = memberBuyRecordDTOList.stream().map(MemberBuyRecordDTO::getMemberId).collect(Collectors.toList());
                    List<Long> otherMemberIdList = memberIdList.stream().filter(e -> !e.equals(memberId)).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(otherMemberIdList)) {
                        log.info("matchStrategyBuyer match buyer common 3 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }

                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                    List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getCurrentValidMemberRecord(buyerEid);
                    if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                        log.info("matchStrategyBuyer match buyer all member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                    List<Long> memberIdList = memberBuyRecordDTOList.stream().map(MemberBuyRecordDTO::getMemberId).collect(Collectors.toList());
                    List<Long> otherMemberIdList = memberIdList.stream().filter(e -> !e.equals(memberId)).collect(Collectors.toList());
                    if (!isRenew && CollUtil.isEmpty(otherMemberIdList)) {
                        log.info("matchStrategyBuyer match buyer all member fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                    // 购买会员，那么购买的这个会员不可能触发(这里触发的条件是部分会员)
                    // 用户购买用户购买会员，如果用户当前的会员不满足这里部分会员的条件，但是购买的那个会员满足，是否满足这里的策略满赠，是否需要触发活动--不触发
                    List<Integer> conditionUserMemberTypeList = JSON.parseArray(strategyActivityDO.getConditionUserMemberType(), Integer.class);
                    if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                        log.info("matchStrategyBuyer match buyer member 0 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }

                    List<Long> promoterIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                        // 指定推广方会员
                        List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = promoterMemberLimitService.listByActivityIdAndEidList(strategyActivityDO.getId(), null);
                        promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDO::getEid).collect(Collectors.toList());
                        if (CollUtil.isEmpty(promoterIdList)) {
                            log.info("matchStrategyBuyer match buyer promoter member 1 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }

                    List<Long> memberIdList = new ArrayList<>();
                    if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                        // 指定方案会员
                        List<StrategyMemberLimitDO> strategyMemberLimitDOList = memberLimitService.listMemberByActivityId(strategyActivityDO.getId());
                        memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDO::getMemberId).collect(Collectors.toList());
                        if (CollUtil.isEmpty(memberIdList)) {
                            log.info("matchStrategyBuyer match buyer program member 2 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                            continue;
                        }
                    }

                    // 查询用户再此次购买和续费会员之前是否符合购买条件
                    QueryMemberListRecordRequest memberListRequest = new QueryMemberListRecordRequest();
                    memberListRequest.setMemberIdList(memberIdList);
                    memberListRequest.setPromoterIdList(promoterIdList);
                    memberListRequest.setEid(buyerEid);
                    memberListRequest.setCurrentValid(true);
                    List<MemberBuyRecordDTO> buyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRequest);
                    if (CollUtil.isEmpty(buyRecordDTOList)) {
                        log.info("matchStrategyBuyer match buyer member 5 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                    List<MemberBuyRecordDTO> buyRecordDTOLists = buyRecordDTOList.stream().filter(e -> !e.getOrderNo().equals(orderNo)).collect(Collectors.toList());
                    if (CollUtil.isEmpty(buyRecordDTOLists)) {
                        log.info("matchStrategyBuyer match buyer member 6 fail ,buyerEid:[{}], 活动id:[{}]", buyerEid, strategyActivityDO.getId());
                        continue;
                    }
                }
                resultDO.add(strategyActivityDO);
            }
        }

        log.info("matchStrategyBuyer end buyerEid:[{}] --> [{}]", buyerEid, JSON.toJSONString(resultDO));
        return resultDO;
    }

    @Override
    public Boolean sendGiftAfterBuyMember(String orderNo) {
        MemberOrderDTO memberOrderDTO = memberOrderApi.getMemberOrderByOrderNo(orderNo);
        Long stageMemberId = memberOrderDTO.getBuyStageId();
        Long eid = memberOrderDTO.getEid();
        List<StrategyActivityDO> strategyActivityDOList = this.getBaseMapper().listByStageMemberId(stageMemberId, null);

        List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getCurrentValidMemberRecord(eid);
        List<String> orderNoList = memberBuyRecordDTOList.stream().map(MemberBuyRecordDTO::getOrderNo).collect(Collectors.toList());
        MemberBuyRecordDTO memberBuyRecordDTO = memberBuyRecordApi.getBuyRecodeByOrderNo(orderNo);
        boolean isRenew = false;
        if (!orderNoList.contains(memberBuyRecordDTO.getOrderNo())) {
            isRenew = true;
        }

        // 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
        List<StrategyActivityDO> activityDOList = matchStrategyBuyer(strategyActivityDOList, eid, isRenew, memberOrderDTO.getMemberId(), orderNo);

        if (CollUtil.isEmpty(activityDOList)) {
            log.info("sendGiftAfterBuyMember 订单{} 用户不满足策略满赠活动", orderNo);
            return true;
        }
        if (!isRenew) {
            // 相等说明这个续费的活动就是当前的活动，需要即时赠送
            Boolean newVisitor = firstInfoApi.checkNewVisitor(eid, OrderTypeEnum.B2B);
            for (StrategyActivityDO activityDO : activityDOList) {
                if (activityDO.getConditionOther().contains("1") && !newVisitor) {
                    log.info("sendGiftAfterBuyMember 购买 非新客用户，不允许参与新客活动, eid:[{}],活动id:[{}]", eid, activityDO.getId());
                    return true;
                }
                QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
                activityRecordListRequest.setMarketingStrategyId(activityDO.getId());
                activityRecordListRequest.setStrategyType(activityDO.getStrategyType());
                activityRecordListRequest.setEid(eid);
                activityRecordListRequest.setOrderId(memberOrderDTO.getId());
                List<StrategyActivityRecordDO> activityRecordDOList = activityRecordService.listByCondition(activityRecordListRequest);
                if (CollUtil.isNotEmpty(activityRecordDOList)) {
                    log.info("sendGiftAfterBuyMember 购买会员,重复执行, eid:[{}],活动id:[{}]", eid, activityDO.getId());
                    continue;
                }

                List<StrategyGiftDO> giftDOList = giftService.listGiftByActivityIdAndLadderId(activityDO.getId(), null);
                sendGift(activityDO, eid, giftDOList, null, memberOrderDTO.getId(), stageMemberId);

                saveMemberAutoSend(activityDO, giftDOList, memberOrderDTO.getId(), eid, stageMemberId, memberBuyRecordDTO.getStartTime());
            }
        } else {
            // 续费，需要等到起效才开始赠送赠品
            log.info("sendGiftAfterBuyMember delay send  memberBuyRecordDTOList:[{}]", memberBuyRecordDTOList);
            DateTime dateTime = new DateTime(memberBuyRecordDTO.getStartTime().getTime());
            dateTime = DateUtil.offsetDay(dateTime, 1);
            String giveTime = dateTime.year() + "-" + dateTime.monthStartFromOne() + "-" + dateTime.dayOfMonth();
            Boolean newVisitor = firstInfoApi.checkNewVisitor(eid, OrderTypeEnum.B2B);
            for (StrategyActivityDO activityDO : activityDOList) {
                if (activityDO.getConditionOther().contains("1") && !newVisitor) {
                    log.info("sendGiftAfterBuyMember 续费 非新客用户，不允许参与新客活动, eid:[{}],活动id:[{}]", eid, activityDO.getId());
                    return true;
                }
                List<StrategyGiftDO> giftDOList = giftService.listGiftByActivityIdAndLadderId(activityDO.getId(), null);
                if (CollUtil.isEmpty(giftDOList)) {
                    continue;
                }
                QueryStrategyActivityAutoGiveGiftPageRequest activityAutoGiveGiftPageRequest = new QueryStrategyActivityAutoGiveGiftPageRequest();
                activityAutoGiveGiftPageRequest.setMarketingStrategyId(activityDO.getId());
                activityAutoGiveGiftPageRequest.setOrderId(memberOrderDTO.getId());
                activityAutoGiveGiftPageRequest.setEid(eid);
                activityAutoGiveGiftPageRequest.setMemberId(stageMemberId);
                activityAutoGiveGiftPageRequest.setGiveTime(giveTime);
                List<StrategyActivityAutoGiveGiftDO> activityAutoGiveGiftDOList = strategyActivityAutoGiveGiftService.listByCondition(activityAutoGiveGiftPageRequest);
                if (CollUtil.isNotEmpty(activityAutoGiveGiftDOList)) {
                    log.info("sendGiftAfterBuyMember 续费会员,重复执行, eid:[{}],活动id:[{}]", eid, activityDO.getId());
                    continue;
                }

                List<StrategyGiftDTO> giftDTOList = PojoUtils.map(giftDOList, StrategyGiftDTO.class);
                SaveActivityAutoGiveGiftRequest request = new SaveActivityAutoGiveGiftRequest();
                request.setMarketingStrategyId(activityDO.getId());
                request.setOrderId(memberOrderDTO.getId());
                request.setEid(eid);
                request.setMemberId(stageMemberId);
                request.setGiveTime(giveTime);
                request.setStrategyGiftList(giftDTOList);
                log.info("sendGiftAfterBuyMember strategyActivityAutoGiveGiftService saveList request:[{}]", JSONUtil.toJsonStr(request));
                strategyActivityAutoGiveGiftService.saveList(request);

                saveMemberAutoSend(activityDO, giftDOList, memberOrderDTO.getId(), eid, stageMemberId, memberBuyRecordDTO.getStartTime());
            }
        }
        return true;
    }

    private void saveMemberAutoSend(StrategyActivityDO activityDO, List<StrategyGiftDO> giftDOList, Long orderId, Long eid, Long stageMemberId, Date startTime) {
        // 购买会员-是否每月固定日期重复执行(1-是；2-否；)
        Integer memberRepeat = activityDO.getMemberRepeat();
        if (1 != memberRepeat) {
            return;
        }
        Integer memberTimes = activityDO.getMemberTimes();
        List<Integer> memberRepeatDay = JSON.parseArray(activityDO.getMemberRepeatDay(), Integer.class);
        Collections.sort(memberRepeatDay);
        List<StrategyGiftDTO> giftDTOList = PojoUtils.map(giftDOList, StrategyGiftDTO.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date strategyStartTime = calendar.getTime();
        DateTime dateTime = DateUtil.offsetMonth(startTime, 1);
        if (memberTimes > 0) {
            int times = 0;
            do {
                for (Integer repeatDay : memberRepeatDay) {
                    SaveActivityAutoGiveGiftRequest request = new SaveActivityAutoGiveGiftRequest();
                    request.setMarketingStrategyId(activityDO.getId());
                    request.setOrderId(orderId);
                    request.setEid(eid);
                    request.setMemberId(stageMemberId);
                    request.setGiveTime(dateTime.year() + "-" + dateTime.monthStartFromOne() + "-" + repeatDay);
                    request.setStartTime(strategyStartTime);
                    request.setStrategyGiftList(giftDTOList);
                    log.info("sendGiftAfterBuyMember strategyActivityAutoGiveGiftService saveList request:[{}]", JSON.toJSONString(request));
                    strategyActivityAutoGiveGiftService.saveList(request);
                    times++;
                    if (times == memberTimes) {
                        break;
                    }
                }
                dateTime = DateUtil.offsetMonth(dateTime, 1);
            } while (times < memberTimes);
        } else if (memberTimes == 0) {
            for (Integer repeatDay : memberRepeatDay) {
                SaveActivityAutoGiveGiftRequest request = new SaveActivityAutoGiveGiftRequest();
                request.setMarketingStrategyId(activityDO.getId());
                request.setOrderId(orderId);
                request.setEid(eid);
                request.setMemberId(stageMemberId);
                request.setGiveTime(repeatDay + "-all");
                request.setStartTime(strategyStartTime);
                request.setStrategyGiftList(giftDTOList);
                log.info("sendGiftAfterBuyMember strategyActivityAutoGiveGiftService saveList request:[{}]", JSON.toJSONString(request));
                strategyActivityAutoGiveGiftService.saveList(request);
            }
        }
    }

    @Override
    public StrategyActivityEidOrGoodsIdDTO getGoodsListPageByActivityId(Long strategyActivityId, Long buyerEid) {
        StrategyActivityEidOrGoodsIdDTO activityEidOrGoodsIdDTO = new StrategyActivityEidOrGoodsIdDTO();
        StrategyActivityDO strategyActivityDO = this.getById(strategyActivityId);
        if (Objects.isNull(strategyActivityDO)) {
            return activityEidOrGoodsIdDTO;
        }
        activityEidOrGoodsIdDTO.setTitle(strategyActivityDO.getName());
        StrategyConditionSellerTypeEnum conditionSellerTypeEnum = StrategyConditionSellerTypeEnum.getByType(strategyActivityDO.getConditionSellerType());
        StrategyConditionGoodsTypeEnum conditionGoodsTypeEnum = StrategyConditionGoodsTypeEnum.getByType(strategyActivityDO.getConditionGoodsType());

        List<Long> sellerEidList = new ArrayList<>();
        if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ALL) {
        } else if (conditionSellerTypeEnum == StrategyConditionSellerTypeEnum.ASSIGN) {
            List<StrategySellerLimitDO> sellerLimitDOList = sellerLimitService.listSellerByActivityId(strategyActivityId);
            List<Long> eidList = sellerLimitDOList.stream().map(StrategySellerLimitDO::getEid).collect(Collectors.toList());
            sellerEidList.addAll(eidList);
        } else {
            throw new BusinessException(StrategyErrorCode.STRATEGY_ERROR);
        }

        if (conditionGoodsTypeEnum != StrategyConditionGoodsTypeEnum.ALL) {
            if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.PLATFORM) {
                List<StrategyPlatformGoodsLimitDO> platformGoodsLimitDOList = platformGoodsLimitService.listPlatformGoodsByActivityId(strategyActivityId);
                List<Long> sellSpecificationsIdList = platformGoodsLimitDOList.stream().map(StrategyPlatformGoodsLimitDO::getSellSpecificationsId).collect(Collectors.toList());
                if (CollUtil.isEmpty(sellerEidList)) {
                    activityEidOrGoodsIdDTO.setSellSpecificationsIdList(sellSpecificationsIdList);
                } else {
                    List<GoodsDTO> goodsDTOList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsIdList, sellerEidList);
                    List<Long> goodsIdList = goodsDTOList.stream().map(GoodsDTO::getId).collect(Collectors.toList());
                    activityEidOrGoodsIdDTO.setGoodsIdList(goodsIdList);
                }
            } else if (conditionGoodsTypeEnum == StrategyConditionGoodsTypeEnum.ENTERPRISE) {
                List<StrategyEnterpriseGoodsLimitDO> enterpriseGoodsLimitDOList = enterpriseGoodsLimitService.listEnterpriseGoodsByActivityId(strategyActivityId);
                List<Long> goodsIdList = enterpriseGoodsLimitDOList.stream().map(StrategyEnterpriseGoodsLimitDO::getGoodsId).collect(Collectors.toList());
                activityEidOrGoodsIdDTO.setGoodsIdList(goodsIdList);
            } else {
                throw new BusinessException(StrategyErrorCode.STRATEGY_ERROR);
            }
        } else {
            if (CollUtil.isEmpty(sellerEidList)) {
                activityEidOrGoodsIdDTO.setAllEidFlag(true);
            } else {
                activityEidOrGoodsIdDTO.setEidList(sellerEidList);
            }
        }
        return activityEidOrGoodsIdDTO;
    }

    /**
     * 时间周期-按周
     *
     * @param strategyActivityDOList 策略满赠活动集合
     */
    private void cycleLadderWeekAutoJobHandler(List<StrategyActivityDO> strategyActivityDOList) {
        log.info("strategyActivityAutoJobHandler cycleLadderWeekAutoJobHandler start strategyActivityDOList:[{}]", JSONUtil.toJsonStr(strategyActivityDOList));
        Date now = DateTime.now();
        for (StrategyActivityDO activityDO : strategyActivityDOList) {
            List<StrategyCycleLadderDO> cycleLadderDOList = cycleLadderService.listCycleLadderByActivityId(activityDO.getId());
            int dayOfWeek = DateUtil.dayOfWeek(now);
            List<StrategyCycleLadderDO> ladderDOList = cycleLadderDOList.stream().filter(e -> JSON.parseArray(e.getConditionValue(), Integer.class).contains(dayOfWeek)).collect(Collectors.toList());
            if (CollUtil.isEmpty(ladderDOList)) {
                log.info("strategyActivityAutoJobHandler data not match 活动id：[{}]", activityDO.getId());
                continue;
            }
            QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
            activityRecordListRequest.setMarketingStrategyId(activityDO.getId());
            activityRecordListRequest.setStrategyType(activityDO.getStrategyType());
            activityRecordListRequest.setStartTime(DateUtil.beginOfDay(new Date()));
            activityRecordListRequest.setStopTime(DateUtil.endOfDay(new Date()));
            StrategyActivityRecordDO strategyActivityRecordDO = activityRecordService.getFirstByCondition(activityRecordListRequest);
            if (Objects.nonNull(strategyActivityRecordDO)) {
                log.info("strategyActivityAutoJobHandler 时间周期-按周,重复执行, 查询条件:[{}]", activityRecordListRequest);
                continue;
            }
            // 活动满足今天发放赠品条件，开始发放赠品
            sendGiftBefore(activityDO, ladderDOList);
        }
        log.info("strategyActivityAutoJobHandler cycleLadderWeekAutoJobHandler end");
    }

    /**
     * 时间周期-按月
     *
     * @param strategyActivityDOList 策略满赠活动集合
     */
    private void cycleLadderMonthAutoJobHandler(List<StrategyActivityDO> strategyActivityDOList) {
        log.info("strategyActivityAutoJobHandler cycleLadderMonthAutoJobHandler start strategyActivityDOList:[{}]", JSONUtil.toJsonStr(strategyActivityDOList));
        Date now = DateTime.now();
        for (StrategyActivityDO activityDO : strategyActivityDOList) {
            List<StrategyCycleLadderDO> cycleLadderDOList = cycleLadderService.listCycleLadderByActivityId(activityDO.getId());
            int dayOfMonth = DateUtil.dayOfMonth(now);
            List<StrategyCycleLadderDO> ladderDOList = cycleLadderDOList.stream().filter(e -> JSON.parseArray(e.getConditionValue(), Integer.class).contains(dayOfMonth)).collect(Collectors.toList());
            if (CollUtil.isEmpty(ladderDOList)) {
                log.info("strategyActivityAutoJobHandler data not match 活动id：[{}]", activityDO.getId());
                continue;
            }
            QueryStrategyActivityRecordListRequest activityRecordListRequest = new QueryStrategyActivityRecordListRequest();
            activityRecordListRequest.setMarketingStrategyId(activityDO.getId());
            activityRecordListRequest.setStrategyType(activityDO.getStrategyType());
            activityRecordListRequest.setStartTime(DateUtil.beginOfDay(new Date()));
            activityRecordListRequest.setStopTime(DateUtil.endOfDay(new Date()));
            StrategyActivityRecordDO strategyActivityRecordDO = activityRecordService.getFirstByCondition(activityRecordListRequest);
            if (Objects.nonNull(strategyActivityRecordDO)) {
                log.info("strategyActivityAutoJobHandler 时间周期-按月,重复执行, 查询条件:[{}]", activityRecordListRequest);
                continue;
            }

            // 活动满足今天发放赠品条件，开始发放赠品
            sendGiftBefore(activityDO, ladderDOList);
        }
        log.info("strategyActivityAutoJobHandler cycleLadderMonthAutoJobHandler end");
    }

    /**
     * 策略满赠时间周期模式根据客户类型对客户分别赠送赠品(优惠券和抽奖次数)
     *
     * @param activityDO 活动信息
     * @param ladderDOList 时间周期的阶梯信息
     */
    private void sendGiftBefore(StrategyActivityDO activityDO, List<StrategyCycleLadderDO> ladderDOList) {
        log.info("strategyActivityAutoJobHandler sendGiftBefore start strategyActivityDO:[{}], ladderDOList:[{}]", activityDO, JSONUtil.toJsonStr(ladderDOList));
        // 获得所有符合这个发放条件的客户
        // 商户范围类型（1-全部客户；2-指定客户；3-指定范围客户）
        Integer conditionBuyerType = activityDO.getConditionBuyerType();
        StrategyConditionBuyerTypeEnum conditionBuyerTypeEnum = StrategyConditionBuyerTypeEnum.getByType(conditionBuyerType);
        if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ALL) {
            QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
            request.setMallFlag(1);
            request.setStatus(1);
            request.setAuthStatus(2);
            List<Integer> notInTypeList = new ArrayList<>();
            notInTypeList.add(1);
            notInTypeList.add(2);
            request.setNotInTypeList(notInTypeList);
            Page<EnterpriseDTO> enterprisePage;
            int current = 1;
            do {
                request.setCurrent(current);
                request.setSize(100);
                enterprisePage = enterpriseApi.pageList(request);
                for (EnterpriseDTO enterpriseDTO : enterprisePage.getRecords()) {
                    sendCycleLadderGift(activityDO, enterpriseDTO.getId(), ladderDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(enterprisePage.getRecords()));
        } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.ASSIGN) {
            QueryStrategyBuyerLimitPageRequest request = new QueryStrategyBuyerLimitPageRequest();
            request.setMarketingStrategyId(activityDO.getId());
            Page<StrategyBuyerLimitDO> buyerLimitPage;
            int current = 1;
            do {
                request.setCurrent(current);
                request.setSize(100);
                buyerLimitPage = buyerLimitService.pageList(request);
                for (StrategyBuyerLimitDO buyerLimitDO : buyerLimitPage.getRecords()) {

                    sendCycleLadderGift(activityDO, buyerLimitDO.getEid(), ladderDOList);
                }
                current = current + 1;
            } while (CollUtil.isNotEmpty(buyerLimitPage.getRecords()));
        } else if (conditionBuyerTypeEnum == StrategyConditionBuyerTypeEnum.RANGE) {
            // 指定企业类型(1:全部类型 2:指定类型)
            Integer conditionEnterpriseType = activityDO.getConditionEnterpriseType();
            // 指定企业类型值，多个值用逗号隔开，字典enterprise_type（3-连锁总店；4-连锁直营；5-连锁加盟；6-单体药店；7-医疗机构；8-诊所）
            String conditionEnterpriseTypeValue = activityDO.getConditionEnterpriseTypeValue();
            List<Integer> typeList = JSON.parseArray(conditionEnterpriseTypeValue, Integer.class);
            List<Integer> inTypeList = new ArrayList<>();
            if (1 == conditionEnterpriseType) {
                inTypeList.add(3);
                inTypeList.add(4);
                inTypeList.add(5);
                inTypeList.add(6);
                inTypeList.add(7);
                inTypeList.add(8);
            } else if (2 == conditionEnterpriseType) {
                inTypeList.addAll(typeList);
            } else {
                return;
            }
            StrategyConditionUserTypeEnum conditionUserTypeEnum = StrategyConditionUserTypeEnum.getByType(activityDO.getConditionUserType());
            if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL) {
                // 全部用户
                QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
                request.setMallFlag(1);
                request.setStatus(1);
                request.setAuthStatus(2);
                if (1 == conditionEnterpriseType) {
                    List<Integer> notInTypeList = new ArrayList<>();
                    notInTypeList.add(1);
                    notInTypeList.add(2);
                    request.setNotInTypeList(notInTypeList);
                } else {
                    request.setInTypeList(inTypeList);
                }
                // 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
                Page<EnterpriseDTO> enterprisePage;
                int current = 1;
                do {
                    request.setCurrent(current);
                    request.setSize(100);
                    enterprisePage = enterpriseApi.pageList(request);
                    for (EnterpriseDTO enterpriseDTO : enterprisePage.getRecords()) {
                        sendCycleLadderGift(activityDO, enterpriseDTO.getId(), ladderDOList);
                    }
                    current = current + 1;
                } while (CollUtil.isNotEmpty(enterprisePage.getRecords()));
            } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.COMMON) {
                // 普通用户-非会员用户
                QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
                request.setMallFlag(1);
                request.setStatus(1);
                request.setAuthStatus(2);
                if (1 == conditionEnterpriseType) {
                    List<Integer> notInTypeList = new ArrayList<>();
                    notInTypeList.add(1);
                    notInTypeList.add(2);
                    request.setNotInTypeList(notInTypeList);
                } else {
                    request.setInTypeList(inTypeList);
                }
                // 类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
                Page<EnterpriseDTO> enterprisePage;
                int current = 1;
                do {
                    request.setCurrent(current);
                    request.setSize(100);
                    enterprisePage = enterpriseApi.pageList(request);
                    for (EnterpriseDTO enterpriseDTO : enterprisePage.getRecords()) {
                        boolean enterpriseMemberStatus = enterpriseMemberApi.getEnterpriseMemberStatus(enterpriseDTO.getId());
                        if (!enterpriseMemberStatus) {
                            sendCycleLadderGift(activityDO, enterpriseDTO.getId(), ladderDOList);
                        }
                    }
                    current = current + 1;
                } while (CollUtil.isNotEmpty(enterprisePage.getRecords()));
            } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.ALL_MEMBER) {
                // 全部会员
                List<EnterpriseDTO> memberEnterprise = enterpriseMemberApi.getMemberEnterprise();
                for (EnterpriseDTO enterpriseDTO : memberEnterprise) {
                    if (Objects.isNull(enterpriseDTO)) {
                        continue;
                    }
                    if (inTypeList.contains(enterpriseDTO.getType())) {
                        sendCycleLadderGift(activityDO, enterpriseDTO.getId(), ladderDOList);
                    }
                }
            } else if (conditionUserTypeEnum == StrategyConditionUserTypeEnum.PART_MEMBER) {
                List<Integer> conditionUserMemberTypeList = JSON.parseArray(activityDO.getConditionUserMemberType(), Integer.class);
                if (CollUtil.isEmpty(conditionUserMemberTypeList)) {
                    log.info("strategyActivityAutoJobHandler sendGiftBefore match member 0 fail , 活动id:[{}]", activityDO.getId());
                    return;
                }
                List<Long> promoterIdList = new ArrayList<>();
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROMOTER_MEMBER.getType())) {
                    // 指定推广方会员
                    List<StrategyPromoterMemberLimitDO> promoterMemberLimitDOList = promoterMemberLimitService.listByActivityIdAndEidList(activityDO.getId(), null);
                    promoterIdList = promoterMemberLimitDOList.stream().map(StrategyPromoterMemberLimitDO::getEid).collect(Collectors.toList());
                    if (CollUtil.isEmpty(promoterIdList)) {
                        log.info("strategyActivityAutoJobHandler sendGiftBefore match promoter member 1 fail, 活动id:[{}]", activityDO.getId());
                        return;
                    }
                }

                List<Long> memberIdList = new ArrayList<>();
                if (conditionUserMemberTypeList.contains(StrategyConditionUserMemberTypeEnum.PROGRAM_MEMBER.getType())) {
                    // 指定方案会员
                    List<StrategyMemberLimitDO> strategyMemberLimitDOList = memberLimitService.listMemberByActivityId(activityDO.getId());
                    memberIdList = strategyMemberLimitDOList.stream().map(StrategyMemberLimitDO::getMemberId).collect(Collectors.toList());
                    if (CollUtil.isEmpty(memberIdList)) {
                        log.info("strategyActivityAutoJobHandler sendGiftBefore match program member 2 fail , 活动id:[{}]", activityDO.getId());
                        return;
                    }
                }
                QueryMemberListRecordRequest memberListRecordRequest = new QueryMemberListRecordRequest();
                memberListRecordRequest.setMemberIdList(memberIdList);
                memberListRecordRequest.setPromoterIdList(promoterIdList);
                memberListRecordRequest.setCurrentValid(true);
                List<MemberBuyRecordDTO> memberBuyRecordDTOList = memberBuyRecordApi.getBuyRecordListByCond(memberListRecordRequest);
                if (CollUtil.isEmpty(memberBuyRecordDTOList)) {
                    log.info("strategyActivityAutoJobHandler sendGiftBefore match member 3 fail , 活动id:[{}]", activityDO.getId());
                    return;
                }
                List<Long> eidList = memberBuyRecordDTOList.stream().map(MemberBuyRecordDTO::getEid).distinct().collect(Collectors.toList());

                for (Long eid : eidList) {
                    EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
                    if (Objects.isNull(enterpriseDTO)) {
                        continue;
                    }
                    if (inTypeList.contains(enterpriseDTO.getType())) {
                        sendCycleLadderGift(activityDO, enterpriseDTO.getId(), ladderDOList);
                    }
                }

            }
        }
        log.info("strategyActivityAutoJobHandler sendGiftBefore end ladderDOList:[{}]", JSONUtil.toJsonStr(ladderDOList));
    }

    /**
     * 策略满赠时间周期模式下针对客户-赠送对应阶梯的赠品(同时如果有新客要求则判断新客用户)
     *
     * @param activityDO 策略满赠活动信息
     * @param eid 客户ID
     * @param ladderDOList 活动阶梯信息
     */
    private void sendCycleLadderGift(StrategyActivityDO activityDO, Long eid, List<StrategyCycleLadderDO> ladderDOList) {
        Boolean newVisitor = firstInfoApi.checkNewVisitor(eid, OrderTypeEnum.B2B);
        if (activityDO.getConditionOther().contains("1") && !newVisitor) {
            log.info("sendCycleLadderGift 非新客用户，不允许参与新客活动, eid:[{}],活动id:[{}]", eid, activityDO.getId());
            return;
        }
        for (StrategyCycleLadderDO cycleLadderDO : ladderDOList) {
            // 1.筛选已经执行的次数，看是否允许再次执行
            Integer times = cycleLadderDO.getTimes();
            Integer records = activityRecordService.countRecordByActivityIdAndEid(activityDO.getId(), cycleLadderDO.getId(), eid, null);
            log.info("sendCycleLadderGift cycleLadderDO:[{}], eid:[{}], records times:[{}], times:[{}]", cycleLadderDO, eid, records, times);
            if (times == 0 || records < times) {
                // 2.开始给对应的客户发放赠品
                List<StrategyGiftDO> strategyGiftDOList = giftService.listGiftByActivityIdAndLadderId(activityDO.getId(), cycleLadderDO.getId());
                sendGift(activityDO, eid, strategyGiftDOList, cycleLadderDO.getId(), null, null);
            }
        }
    }

    /**
     * 客户执行策略满赠活动记录数据的新增
     *
     * @param activityDO 活动信息
     * @param eid 企业ID
     * @param ladderId 阶梯id
     * @param orderId 订单id
     * @param memberId 会员id
     * @param sendStatus 执行成功/失败
     * @return 记录信息
     */
    private StrategyActivityRecordDO saveRecord(StrategyActivityDO activityDO, Long eid, Long ladderId, Long orderId, Long memberId, Integer sendStatus) {
        StrategyActivityRecordDO strategyActivityRecordDO = new StrategyActivityRecordDO();
        strategyActivityRecordDO.setMarketingStrategyId(activityDO.getId());
        strategyActivityRecordDO.setMarketingStrategyName(activityDO.getName());
        strategyActivityRecordDO.setStrategyType(activityDO.getStrategyType());
        strategyActivityRecordDO.setEid(eid);
        strategyActivityRecordDO.setLadderId(ladderId);
        strategyActivityRecordDO.setOrderId(orderId);
        strategyActivityRecordDO.setMemberId(memberId);
        strategyActivityRecordDO.setSendStatus(sendStatus);
        activityRecordService.save(strategyActivityRecordDO);
        return strategyActivityRecordDO;
    }

    private Integer getLotteryType(Integer strategyType) {
        // strategyType 策略类型：1-订单累计金额/2-签到天数/3-时间周期/4-购买会员
        // 获取方式：1-签到、2-抽奖赠送、3-活动每天赠送、4-分享、5-活动开始赠送、6-购买会员、7-订单累计金额赠送、8-时间周期
        if (1 == strategyType) {
            return 7;
        } else if (2 == strategyType) {
            return 1;
        } else if (3 == strategyType) {
            return 8;
        } else if (4 == strategyType) {
            return 6;
        } else {
            return 0;
        }
    }

    @Override
    public void sendGift(StrategyActivityDO activityDO, Long eid, List<StrategyGiftDO> strategyGiftDOList, Long ladderId, Long orderId, Long memberId) {
        log.info("sendGift start 活动id:[{}], eid:[{}], ladderId:[{}], orderId:[{}], memberId:[{}], strategyGiftDOList:[{}]", activityDO.getId(), eid, ladderId, orderId, memberId, JSONUtil.toJsonStr(strategyGiftDOList));
        List<StrategyGiftFailDO> lotteryFailDOList = new ArrayList<>();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(eid);
        {
            List<StrategyGiftDO> lotteryGiftList = strategyGiftDOList.stream().filter(e -> e.getType() == 3).collect(Collectors.toList());

            for (StrategyGiftDO strategyGiftDO : lotteryGiftList) {
                AddLotteryTimesRequest lotteryTimesRequest = new AddLotteryTimesRequest();
                lotteryTimesRequest.setLotteryActivityId(strategyGiftDO.getGiftId());
                lotteryTimesRequest.setActivityName(activityDO.getName());
                lotteryTimesRequest.setPlatformType(1);
                lotteryTimesRequest.setUid(eid);
                lotteryTimesRequest.setUname(enterpriseDTO.getName());
                lotteryTimesRequest.setGetType(getLotteryType(activityDO.getStrategyType()));
                lotteryTimesRequest.setTimes(strategyGiftDO.getCount());
                LotteryActivityDO lotteryActivityDO = lotteryActivityService.getById(strategyGiftDO.getGiftId());
                if (lotteryActivityDO != null && lotteryActivityDO.getStatus() == 1 && lotteryActivityDO.getEndTime().compareTo(new Date()) >= 0) {
                    log.info("sendGift addLotteryTimes lotteryTimesRequest:[{}]", lotteryTimesRequest);
                    Integer lotteryTimes = lotteryActivityTimesService.addLotteryTimes(lotteryTimesRequest);
                    log.info("sendGift addLotteryTimes lotteryTimesRequest:[{}],response:[{}]", lotteryTimesRequest, lotteryTimes);
                } else {
                    StrategyGiftFailDO strategyGiftFailDO = new StrategyGiftFailDO();
                    strategyGiftFailDO.setMarketingStrategyId(activityDO.getId());
                    strategyGiftFailDO.setEid(eid);
                    strategyGiftFailDO.setType(strategyGiftDO.getType());
                    strategyGiftFailDO.setGiftId(strategyGiftDO.getGiftId());
                    strategyGiftFailDO.setCount(strategyGiftDO.getCount());
                    strategyGiftFailDO.setSendTime(new Date());
                    strategyGiftFailDO.setRemark("已经作废或失效");
                    lotteryFailDOList.add(strategyGiftFailDO);
                }
            }
        }
        List<StrategyGiftFailDO> couponFailDOList = new ArrayList<>();
        {
            List<StrategyGiftDO> couponGiftList = strategyGiftDOList.stream().filter(e -> e.getType() != 3).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(couponGiftList)) {
                List<StrategyGiftFailDO> failDOList = new ArrayList<>();
                List<Long> couponIdList = couponGiftList.stream().map(StrategyGiftDO::getGiftId).collect(Collectors.toList());
                Map<Long, CouponActivityDetailDTO> couponActivityDetailDTOMap = couponActivityService.getRemainDtoByActivityIds(couponIdList);
                log.info("sendGift getRemainDtoByActivityIds couponIdList:[{}],couponActivityDetailDTOMap:[{}]", couponIdList, JSONUtil.toJsonStr(couponActivityDetailDTOMap));
                List<SaveCouponRequest> requests = new ArrayList<>();

                for (StrategyGiftDO strategyGiftDO : couponGiftList) {
                    CouponActivityDetailDTO couponDTO = couponActivityDetailDTOMap.get(strategyGiftDO.getGiftId());
                    if (Objects.isNull(couponDTO)) {
                        StrategyGiftFailDO strategyGiftFailDO = new StrategyGiftFailDO();
                        strategyGiftFailDO.setMarketingStrategyId(activityDO.getId());
                        strategyGiftFailDO.setEid(eid);
                        strategyGiftFailDO.setType(strategyGiftDO.getType());
                        strategyGiftFailDO.setGiftId(strategyGiftDO.getGiftId());
                        strategyGiftFailDO.setCount(strategyGiftDO.getCount());
                        strategyGiftFailDO.setSendTime(new Date());
                        strategyGiftFailDO.setRemark("已经作废或失效");
                        failDOList.add(strategyGiftFailDO);
                        log.info("sendGift [couponDTO is NULL] couponDTOList:[{}],strategyGiftDO:[{}]", JSONUtil.toJsonStr(couponActivityDetailDTOMap), JSONUtil.toJsonStr(strategyGiftDO));
                        continue;
                    }
                    Integer allCount = couponDTO.getSurplusCount();
                    // 赠品类型(1-商品优惠券；2-会员优惠券；3-抽奖次数)
                    if (3 == strategyGiftDO.getType() || allCount <= 0) {
                        StrategyGiftFailDO strategyGiftFailDO = new StrategyGiftFailDO();
                        strategyGiftFailDO.setMarketingStrategyId(activityDO.getId());
                        strategyGiftFailDO.setEid(eid);
                        strategyGiftFailDO.setType(strategyGiftDO.getType());
                        strategyGiftFailDO.setGiftId(strategyGiftDO.getGiftId());
                        strategyGiftFailDO.setCount(strategyGiftDO.getCount());
                        strategyGiftFailDO.setSendTime(new Date());
                        strategyGiftFailDO.setRemark("数量不足");
                        failDOList.add(strategyGiftFailDO);
                        continue;
                    }

                    Integer sendCount = strategyGiftDO.getCount();
                    int count = 0;
                    if (sendCount <= allCount) {
                        count = sendCount;
                    } else {
                        count = allCount;
                        StrategyGiftFailDO strategyGiftFailDO = new StrategyGiftFailDO();
                        strategyGiftFailDO.setMarketingStrategyId(activityDO.getId());
                        strategyGiftFailDO.setEid(eid);
                        strategyGiftFailDO.setType(strategyGiftDO.getType());
                        strategyGiftFailDO.setGiftId(strategyGiftDO.getGiftId());
                        strategyGiftFailDO.setCount(sendCount - allCount);
                        strategyGiftFailDO.setSendTime(new Date());
                        strategyGiftFailDO.setRemark("数量不足");
                        failDOList.add(strategyGiftFailDO);
                    }
                    SaveCouponRequest request = new SaveCouponRequest();
                    request.setCouponActivityAutoId(activityDO.getId());
                    request.setCouponActivityAutoName(activityDO.getName());
                    request.setCouponActivityId(couponDTO.getId());
                    request.setCouponActivityName(couponDTO.getName());
                    request.setEid(eid);
                    request.setEname(enterpriseDTO.getName());
                    request.setGetType(5);
                    request.setUsedStatus(1);
                    request.setStatus(1);
                    for (int i = 0; i < count; i++) {
                        requests.add(request);
                    }
                }
                log.info("sendGift SaveCouponRequest :[{}]", requests);
                if (CollUtil.isNotEmpty(requests)) {
                    Boolean isSuccess = couponActivityService.giveCoupon(requests);
                    if (isSuccess) {
                        couponFailDOList.addAll(failDOList);
                    } else {
                        for (StrategyGiftDO strategyGiftDO : couponGiftList) {
                            StrategyGiftFailDO strategyGiftFailDO = new StrategyGiftFailDO();
                            strategyGiftFailDO.setMarketingStrategyId(activityDO.getId());
                            strategyGiftFailDO.setEid(eid);
                            strategyGiftFailDO.setType(strategyGiftDO.getType());
                            strategyGiftFailDO.setGiftId(strategyGiftDO.getGiftId());
                            strategyGiftFailDO.setCount(strategyGiftDO.getCount());
                            strategyGiftFailDO.setSendTime(new Date());
                            strategyGiftFailDO.setRemark("发送失败");
                            couponFailDOList.add(strategyGiftFailDO);
                        }
                    }
                } else {
                    couponFailDOList.addAll(failDOList);
                }
            }
        }
        // 新增记录
        if (CollUtil.isEmpty(lotteryFailDOList) && CollUtil.isEmpty(couponFailDOList)) {
            saveRecord(activityDO, eid, ladderId, orderId, memberId, 1);
        } else {
            StrategyActivityRecordDO strategyActivityRecordDO = saveRecord(activityDO, eid, ladderId, orderId, memberId, 2);
            lotteryFailDOList.addAll(couponFailDOList);
            for (StrategyGiftFailDO strategyGiftFailDO : lotteryFailDOList) {
                strategyGiftFailDO.setActivityRecordId(strategyActivityRecordDO.getId());
            }
            log.info("sendGift failDOList:[{}]", lotteryFailDOList);
            strategyGiftFailService.saveBatch(lotteryFailDOList);
        }

    }

    @Override
    public List<StrategyActivityDO> pageStrategyByGiftId(QueryLotteryStrategyRequest request) {
        return this.getBaseMapper().listStrategyByGiftId(request);
    }

    @Override
    public Page<StrategyActivityDO> pageLotteryStrategy(QueryLotteryStrategyPageRequest request) {
        Page<StrategyActivityDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.getBaseMapper().pageLotteryStrategy(objectPage, request);
    }

    @Override
    public Map<Long, Integer> countStrategyByGiftId(List<Long> lotteryActivityIdList) {
        Map<Long, Integer> resultMap = new HashMap<>();
        for (Long lotteryActivityId : lotteryActivityIdList) {
            Integer count = this.getBaseMapper().countStrategyByGiftId(lotteryActivityId);
            resultMap.put(lotteryActivityId, count != null ? count : 0);
        }
        return resultMap;
    }

    @Override
    public boolean saveAllForPresale(SavePresaleActivityRequest request) {
        checkPresaleGoodsInfo(request);
        MarketingPresaleActivityDO presaleActivityDO = PojoUtils.map(request, MarketingPresaleActivityDO.class);
        String platformSelected = String.join(",", request.getPlatformSelected());
        presaleActivityDO.setPlatformSelected(platformSelected);
        String conditionEnter = String.join(",", request.getConditionEnterpriseTypeValue());
        presaleActivityDO.setConditionEnterpriseTypeValue(conditionEnter);
        String other = String.join(",", request.getConditionOther());
        presaleActivityDO.setConditionOther(other);
        presaleActivityService.saveOrUpdate(presaleActivityDO);
        List<MarketingPresaleGoodsLimitDO> presaleGoodsLimitDOS = PojoUtils.map(request.getPresaleGoodsLimitForms(), MarketingPresaleGoodsLimitDO.class);
        if (CollectionUtils.isNotEmpty(presaleGoodsLimitDOS)) {
            presaleGoodsLimitDOS.forEach(item -> {
                item.setMarketingStrategyId(presaleActivityDO.getId());
            });
            QueryWrapper<MarketingPresaleGoodsLimitDO> presaleGoodsLimitDOQueryWrapper = new QueryWrapper<>();
            presaleGoodsLimitDOQueryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getMarketingStrategyId, request.getId());
            presaleGoodsLimitDOQueryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getDelFlag, 0);
            List<MarketingPresaleGoodsLimitDO> list = presaleGoodsLimitService.list(presaleGoodsLimitDOQueryWrapper);
            if (CollectionUtils.isNotEmpty(list)) {
                // 删除条件
                LambdaQueryWrapper<MarketingPresaleGoodsLimitDO> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.eq(MarketingPresaleGoodsLimitDO::getMarketingStrategyId, request.getId());
                // 删除
                MarketingPresaleGoodsLimitDO couponActivityGoodsLimit = new MarketingPresaleGoodsLimitDO();
                couponActivityGoodsLimit.setUpdateUser(request.getOpUserId());
                presaleGoodsLimitService.batchDeleteWithFill(couponActivityGoodsLimit, deleteWrapper);
            }
            presaleGoodsLimitService.saveBatch(presaleGoodsLimitDOS);
            return true;
        }

        return false;
    }

    private void checkPresaleGoodsInfo(SavePresaleActivityRequest request) {
        List<PresaleGoodsLimitRequest> presaleGoodsLimitForms = request.getPresaleGoodsLimitForms();
        if (CollectionUtils.isNotEmpty(presaleGoodsLimitForms)) {
            presaleGoodsLimitForms.forEach(item -> {
                if (item.getPresaleType() == 1) {
                    boolean satisfy = item.getDepositRatio().multiply(item.getExpansionMultiplier()).compareTo(new BigDecimal("100")) == -1;
                    if (!satisfy) {
                        throw new BusinessException(EXPANSION_MULTIPLIER_TOO_HIGH);
                    }
                }
                if (item.getPresaleType() == 2) {
                    boolean satisfy = item.getFinalPayDiscountAmount().compareTo(item.getPresaleAmount().subtract(item.getPresaleAmount().multiply(item.getDepositRatio().divide(new BigDecimal("100"))))) == -1;
                    if (!satisfy) {
                        throw new BusinessException(FINAL_PAY_DISCOUNT_AMOUNT_TOO_HIGH);
                    }
                }
            });
        }
    }

    @Override
    public PresaleActivityDTO queryPresaleActivityById(Long id) {
        MarketingPresaleActivityDO presaleActivityDO = presaleActivityService.getById(id);
        PresaleActivityDTO presaleActivityDTO = PojoUtils.map(presaleActivityDO, PresaleActivityDTO.class);
        QueryWrapper<MarketingPresaleGoodsLimitDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getMarketingStrategyId, id);
        queryWrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getDelFlag, 0);
        List<MarketingPresaleGoodsLimitDO> presaleGoodsLimitDOS = presaleGoodsLimitService.list(queryWrapper);
        List<PresaleGoodsLimitDTO> presaleGoodsLimitDTOS = PojoUtils.map(presaleGoodsLimitDOS, PresaleGoodsLimitDTO.class);
        presaleActivityDTO.setPresaleGoodsLimitForms(presaleGoodsLimitDTOS);
        Integer buyerNum = presaleGoodsLimitMapper.getBuyerNumber(id);
        Integer promoterNumber = presaleGoodsLimitMapper.getPromoterNumber(id);
        Integer memberNumber = presaleGoodsLimitMapper.getMemberNumber(id);
        presaleActivityDTO.setBuyerNum(buyerNum);
        presaleActivityDTO.setPromoterNnm(promoterNumber);
        presaleActivityDTO.setMemberNum(memberNumber);
        return presaleActivityDTO;
    }
}
