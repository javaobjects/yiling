package com.yiling.marketing.lotteryactivity.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityRewardSettingBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryResultBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityJoinDetailRequest;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailRewardCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetCountRequest;
import com.yiling.marketing.lotteryactivity.dto.request.ReduceLotteryTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SignAddTimesRequest;
import com.yiling.marketing.lotteryactivity.dto.request.UpdateCashRewardRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGetDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinDetailDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivitySignRecordDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityTimesChangeLogDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityTimesDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityTimesMapper;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGetTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryTimesChangeTypeEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGetService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinDetailService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivitySignRecordService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesChangeLogService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.integral.api.IntegralUseRuleApi;
import com.yiling.user.integral.api.UserIntegralApi;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeLotteryRequest;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖次数表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Slf4j
@Service
public class LotteryActivityTimesServiceImpl extends BaseServiceImpl<LotteryActivityTimesMapper, LotteryActivityTimesDO> implements LotteryActivityTimesService {

    @Autowired
    LotteryActivityTimesChangeLogService lotteryActivityTimesChangeLogService;
    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;
    @Autowired
    LotteryActivityJoinDetailService lotteryActivityJoinDetailService;
    @Autowired
    LotteryActivityJoinRuleService lotteryActivityJoinRuleService;
    @Autowired
    LotteryActivityGetService lotteryActivityGetService;
    @Autowired
    LotteryActivitySignRecordService lotteryActivitySignRecordService;
    @Autowired
    SpringAsyncConfig springAsyncConfig;
    @Autowired
    GoodsGiftService goodsGiftService;
    @Autowired
    CouponActivityService couponActivityService;

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    UserIntegralApi userIntegralApi;
    @DubboReference
    IntegralUseRuleApi integralUseRuleApi;

    @Autowired
    RedisDistributedLock redisDistributedLock;
    @Autowired
    RedisService redisService;

    @Lazy
    @Autowired
    LotteryActivityTimesServiceImpl _this;

    /**
     * 获取可用抽奖次数
     *
     * @param lotteryActivityId 抽奖活动ID
     * @param platform 平台类型：1-B端 2-C端
     * @param uid 用户ID
     * @return
     */
    @Override
    public Integer getAvailableTimes(Long lotteryActivityId, Integer platform, Long uid) {
        // 使用缓存抽奖次数
        Object activityTimes = redisService.hGet("lottery_activity_times", lotteryActivityId + "_" + platform + "_" + uid);
        if (Objects.nonNull(activityTimes)) {
            return (Integer) activityTimes;
        } else {
            LotteryActivityTimesDO activityTimesDO = this.getLotteryActivityTimesDO(lotteryActivityId, platform, uid);
            if (Objects.nonNull(activityTimesDO)) {
                redisService.hSet("lottery_activity_times", lotteryActivityId + "_" + platform + "_" + uid, activityTimesDO.getAvailableTimes(), 3600);
                return activityTimesDO.getAvailableTimes();
            }
        }

        return 0;
    }

    @Override
    public Map<Long, Integer> getUseTimes(List<Long> lotteryActivityIdList) {
        if (CollUtil.isEmpty(lotteryActivityIdList)) {
            return MapUtil.newHashMap();
        }

        LambdaQueryWrapper<LotteryActivityTimesDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(LotteryActivityTimesDO::getLotteryActivityId, lotteryActivityIdList);
        return this.list(wrapper).stream().collect(Collectors.groupingBy(LotteryActivityTimesDO::getLotteryActivityId, Collectors.summingInt(LotteryActivityTimesDO::getUseTimes)));
    }

    @Override
    public Integer addLotteryTimes(AddLotteryTimesRequest request) {
        int afterTimes;
        // 上锁
        String lockKey = this.getLockName(request.getLotteryActivityId(), request.getPlatformType(), request.getUid());
        String lockId = null;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);

            LotteryActivityTimesDO activityTimesDO = this.getLotteryActivityTimesDO(request.getLotteryActivityId(), request.getPlatformType(), request.getUid());
            // 增加抽奖次数和记录日志
            afterTimes = _this.addLotteryTimesTx(request, activityTimesDO);

            // 更新缓存内的可用次数
            redisService.hSet("lottery_activity_times", request.getLotteryActivityId() + "_" + request.getPlatformType() + "_" + request.getUid(), afterTimes, 3600);

        }  finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }
        log.info("抽奖活动ID={} 平台类型={} 用户ID={} 增加抽奖次数{}次完成", request.getLotteryActivityId(), request.getPlatformType(), request.getUid(), request.getTimes());

        return afterTimes;
    }

    /**
     * 积分兑换抽奖时的操作(1.查看积分和兑换次数是否重组；2.扣减积分；3.生成记录新增抽奖次数)
     *
     * @param request   请求条件
     * @param lotteryActivityDO 抽奖主信息
     */
    private void executeIntegralLottery(ReduceLotteryTimesRequest request,LotteryActivityDO lotteryActivityDO) {
        //  积分抽奖：1.校验积分是否充足，当天是否还有兑换次数；
        //  2.抽奖完成后减去抽奖次数(已经有了)；
        //  3.减去抽奖使用的积分；
        //  4.生成抽奖记录(已经存在，积分类型的修改)
        log.info("开始积分兑换抽奖次数的抽奖");
        // 1. 查询登录用户的积分总额
        Integer userIntegral = userIntegralApi.getUserIntegralByUid(request.getUid(), IntegralRulePlatformEnum.B2B.getCode());
        // 2. 查询积分的兑换规则
        IntegralLotteryConfigDTO integralLotteryConfigDTO = integralUseRuleApi.getRuleByLotteryActivityId(request.getLotteryActivityId());
        if (Objects.isNull(integralLotteryConfigDTO)) {
            // 用户积分不够，抛出异常
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_INTEGRAL_CONFIG_NOT_NULL);
        }
        Integer useIntegralValue = integralLotteryConfigDTO.getUseIntegralValue();
        if (useIntegralValue > userIntegral) {
            // 用户积分不够，抛出异常
            log.info("用户兑换抽奖的积分不够,用户积分为:[{}],此活动兑换需要的积分为:[{}]", userIntegral, useIntegralValue);
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_INTEGRAL_NOT_ENOUGH);
        }
//        Integer everyDayTimes = integralLotteryConfigDTO.getEveryDayTimes();
//        Integer useSumTimes = integralLotteryConfigDTO.getUseSumTimes();
        // 3.如果用户积分兑换抽奖次数达到限制则返回不允许兑换
//        QueryLotteryActivityGetCountRequest countRequest = new QueryLotteryActivityGetCountRequest();
//        countRequest.setLotteryActivityId(request.getLotteryActivityId());
//        countRequest.setUid(request.getUid());
//        countRequest.setGetType(LotteryActivityGetTypeEnum.INTEGRAL.getCode());
//        countRequest.setPlatformType(1);
//
//        if (null != useSumTimes && useSumTimes > 0) {
//            countRequest.setIsToday(false);
//            Integer allUserGetTimes = lotteryActivityGetService.countByUidAndGetType(countRequest);
//            int allUserCanGetTimes = useSumTimes.compareTo(allUserGetTimes);
//            if (allUserCanGetTimes < 1) {
//                // 用户此活动总兑换次数不够
//                log.info("用户此活动的兑换次数不够,允许兑换的抽奖次数为:[{}]", allUserCanGetTimes);
//                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_INTEGRAL_TIMES_NOT_ENOUGH);
//            }
//        }
//
//        if (null != everyDayTimes && everyDayTimes > 0) {
//            countRequest.setIsToday(true);
//            Integer todayUserGetTimes = lotteryActivityGetService.countByUidAndGetType(countRequest);
//            int todayUserCanGetTimes = everyDayTimes.compareTo(todayUserGetTimes);
//            if (todayUserCanGetTimes < 1) {
//                // 用户当天积分兑换次数不够
//                log.info("用户今天积分兑换次数不够,今天允许兑换的抽奖次数为:[{}]", todayUserCanGetTimes);
//                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_INTEGRAL_TODAY_TIMES_NOT_ENOUGH);
//            }
//        }

        // 4.开始扣减积分，新增兑换次数
        UpdateIntegralExchangeLotteryRequest exchangeLotteryRequest = new UpdateIntegralExchangeLotteryRequest();
        exchangeLotteryRequest.setOpUserId(request.getOpUserId());
        exchangeLotteryRequest.setOpTime(request.getOpTime());
        exchangeLotteryRequest.setLotteryActivityId(request.getLotteryActivityId());
        exchangeLotteryRequest.setExchangeTimes(1);
        exchangeLotteryRequest.setPlatform(LotteryActivityPlatformEnum.B2B.getCode());
        exchangeLotteryRequest.setUid(request.getUid());
        integralUseRuleApi.exchangeLotteryTimes(exchangeLotteryRequest);

        String uname;
        if (request.getPlatformType() == 1) {
            uname = Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO()).getName();
        } else {
            uname = Optional.ofNullable(userApi.getById(request.getUid())).orElse(new UserDTO()).getNickName();
        }

        AddLotteryTimesRequest addLotteryTimesRequest = PojoUtils.map(request, AddLotteryTimesRequest.class);
        addLotteryTimesRequest.setActivityName(lotteryActivityDO.getActivityName());
        addLotteryTimesRequest.setGetType(LotteryActivityGetTypeEnum.INTEGRAL.getCode());
        addLotteryTimesRequest.setUname(uname);
        addLotteryTimesRequest.setTimes(1);
        Integer addLotteryTimes = _this.addLotteryTimes(addLotteryTimesRequest);
        log.info("积分兑换抽奖，增加后的抽奖次数为:[{}]", addLotteryTimes);
    }

    @Override
    @GlobalTransactional
    public LotteryResultBO executeLottery(ReduceLotteryTimesRequest request) {
        LotteryResultBO lotteryResultBO = new LotteryResultBO();
        lotteryResultBO.setLotteryTimesFlag(true);

        Long lotteryActivityId = request.getLotteryActivityId();
        LotteryActivityDO lotteryActivityDO = getLotteryActivityDO(lotteryActivityId);

        Integer type = request.getType();
        if (Objects.nonNull(type) && 1 == type) {
            // 积分兑换抽奖处理
            executeIntegralLottery(request, lotteryActivityDO);
        }

        // 校验是否还有抽奖次数
        Integer lotteryTimes = this.getAvailableTimes(lotteryActivityId, request.getPlatformType(), request.getUid());
        if (Objects.isNull(lotteryTimes) || lotteryTimes <= 0) {
            lotteryResultBO.setAvailableTimes(0);
            lotteryResultBO.setLotteryTimesFlag(false);
            return lotteryResultBO;
        }

        // 1.计算每个奖品的概率：获取奖品信息，判断是否有库存，库存数量为0的过滤掉，重新计算概率
        List<LotteryActivityRewardSettingDTO> rewardSettingDTOList = lotteryActivityRewardSettingService.getByLotteryActivityId(lotteryActivityId);

        List<LotteryActivityRewardSettingBO> rewardSettingBOList = rewardSettingDTOList.stream().map(rewardSettingDTO -> {
            // 获取奖品剩余数量
            LotteryActivityRewardSettingBO rewardSettingBO = lotteryActivityService.getRewardRemainNumber(rewardSettingDTO);
            if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.EMPTY
                    || LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.DRAW) {
                return rewardSettingBO;
            }
            if (rewardSettingBO.getRemainNumber() <= 0) {
                log.info("抽奖活动ID={} 奖品设置ID={} 关联奖品ID={} 奖品库存数量为={}", lotteryActivityId, rewardSettingDTO.getId(), rewardSettingDTO.getRewardId(), rewardSettingBO.getRemainNumber());
                return null;
            }
            // 如果当前奖品当天已达到最大抽中数量，则不再抽中此奖品。如果是0，则不限制
            if (rewardSettingBO.getEveryMaxNumber() > 0) {
                QueryJoinDetailRewardCountRequest rewardCountRequest = PojoUtils.map(rewardSettingDTO, QueryJoinDetailRewardCountRequest.class);
                Integer rewardCount = lotteryActivityJoinDetailService.getCurrentRewardCount(rewardCountRequest);
                if (rewardSettingBO.getEveryMaxNumber() <= rewardCount) {
                    log.info("抽奖活动ID={} 奖品ID={} 奖品数量={}已经达到最大抽中数量={}故过滤", lotteryActivityId, rewardSettingDTO.getRewardId(), rewardCount, rewardSettingBO.getEveryMaxNumber());
                    return null;
                }
            }

            return rewardSettingBO;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // 2.进行抽奖
        int giftIndex = this.drawGift(rewardSettingBOList);
        LotteryActivityRewardSettingBO rewardSettingBO = rewardSettingBOList.get(giftIndex);
        if (Objects.isNull(rewardSettingBO)) {
            log.error("抽奖失败 抽奖活动ID={} 命中区间={} 设置的奖品信息={}", lotteryActivityId, giftIndex, JSONObject.toJSONString(rewardSettingBOList));
            throw new BusinessException(LotteryActivityErrorCode.DRAW_FAIL);
        }
        log.info("抽奖活动ID={} 成功中奖且中奖的奖品信息={}", lotteryActivityId, JSONObject.toJSONString(rewardSettingBO));

        // 3.设置返回信息
        PojoUtils.map(rewardSettingBO, lotteryResultBO);
        lotteryResultBO.setRewardFlag(LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) != LotteryActivityRewardTypeEnum.EMPTY);
        // 中奖图片地址处理
        if (LotteryActivityRewardTypeEnum.REAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())
                || LotteryActivityRewardTypeEnum.VIRTUAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())) {
            GoodsGiftDO goodsGiftDO = Optional.ofNullable(goodsGiftService.getById(rewardSettingBO.getRewardId())).orElse(new GoodsGiftDO());
            lotteryResultBO.setRewardImg(goodsGiftDO.getPictureUrl());
        } else {
            lotteryResultBO.setRewardImg(lotteryActivityService.getRewardImg(rewardSettingBO.getRewardType()));
        }
        // 抽奖活动奖品的索引位置返回
        for (LotteryActivityRewardSettingDTO rewardSettingDTO : rewardSettingDTOList) {
            if (rewardSettingDTO.getId().compareTo(rewardSettingBO.getId()) == 0) {
                lotteryResultBO.setRewardIndex(rewardSettingDTOList.indexOf(rewardSettingDTO));
                break;
            }
        }

        // 4.校验抽中的奖品库存是否足够，如果不足够需要进行重新抽奖，如果足够则需要发放奖品扣减库存
        if (lotteryResultBO.getRewardFlag()) {
            if (Objects.nonNull(rewardSettingBO.getRewardId()) && rewardSettingBO.getRewardId() != 0) {
                String stockLockKey = this.getStockLockName(rewardSettingBO.getRewardType(), rewardSettingBO.getRewardId());
                String stockLockId = null;
                try {
                    stockLockId = redisDistributedLock.lock2(stockLockKey, 3, 3, TimeUnit.SECONDS);
                    // 赠品库：真实物品、虚拟物品
                    if (LotteryActivityRewardTypeEnum.REAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())
                            || LotteryActivityRewardTypeEnum.VIRTUAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())) {
                        GoodsGiftDO goodsGiftDO = goodsGiftService.getById(rewardSettingBO.getRewardId());
                        if (Objects.isNull(goodsGiftDO) || goodsGiftDO.getAvailableQuantity() <= 0 || goodsGiftDO.getAvailableQuantity() < rewardSettingBO.getRewardNumber()) {
                            log.info("抽奖活动ID={} 抽中奖品ID={} 库存数量为={}库存不足进行重新抽奖", lotteryActivityId, rewardSettingBO.getRewardId(), goodsGiftDO.getAvailableQuantity());
                            // 释放锁
                            redisDistributedLock.releaseLock(stockLockKey, stockLockId);
                            return this.executeLottery(request);
                        }

                    } else if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.GOODS_COUPON
                            || LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.MEMBER_COUPON) {
                        // 商品优惠券、会员优惠券
                        Map<Long, Integer> remainMap = couponActivityService.getRemainByActivityIds(ListUtil.toList(rewardSettingBO.getRewardId()));
                        Integer count = remainMap.get(rewardSettingBO.getRewardId());
                        if (Objects.isNull(count) || count <= 0 || count < rewardSettingBO.getRewardNumber()) {
                            log.info("抽奖活动ID={} 抽中奖品ID={} 库存数量为={}库存不足进行重新抽奖", lotteryActivityId, rewardSettingBO.getRewardId(), count);
                            // 释放锁
                            redisDistributedLock.releaseLock(stockLockKey, stockLockId);
                            return this.executeLottery(request);
                        }
                    }
                    // 中奖为：真实物品、虚拟物品、优惠券，发放奖品、扣减库存
                    boolean result = this.giveReward(rewardSettingBO, lotteryActivityDO, request.getPlatformType(), request.getUid());
                    if (!result) {
                        // 释放锁
                        redisDistributedLock.releaseLock(stockLockKey, stockLockId);
                        log.error("抽奖活动ID={} 抽中奖品ID={} 发放奖品扣减库存失败", lotteryActivityId, rewardSettingBO.getRewardId());
                        throw new BusinessException(LotteryActivityErrorCode.DRAW_FAIL);
                    }

                } finally {
                    // 释放锁
                    redisDistributedLock.releaseLock(stockLockKey, stockLockId);
                }
            }
        }

        // 5.抽奖次数减1，抽奖活动参与明细记录、抽奖次数变更记录
        String lockKey = this.getLockName(request.getLotteryActivityId(), request.getPlatformType(), request.getUid());
        String lockId = null;
        Map<String, Long> map;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);

            LotteryActivityTimesDO activityTimesDO = this.getLotteryActivityTimesDO(request.getLotteryActivityId(), request.getPlatformType(), request.getUid());
            if (Objects.isNull(activityTimesDO) || activityTimesDO.getAvailableTimes() <= 0) {
                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_TIMES_USE_ZERO);
            }
            map = _this.reduceLotteryTimes(request, activityTimesDO, rewardSettingBO, lotteryActivityDO.getActivityName(), lotteryActivityDO.getPlatform());

            // 更新缓存内的可用次数
            redisService.hSet("lottery_activity_times", request.getLotteryActivityId() + "_" + request.getPlatformType() + "_" + request.getUid(), map.get("lotteryTimes"), 3600);

        }  finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }
        lotteryResultBO.setAvailableTimes(Math.toIntExact(map.get("lotteryTimes")));

        // 6.异步调用更新兑付状态：虚拟物品、会员优惠券、商品优惠券、抽奖机会这些类型需要更新兑付状态为已兑付
        if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) != LotteryActivityRewardTypeEnum.EMPTY
                && LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) != LotteryActivityRewardTypeEnum.REAL_GOODS) {
            // 如果中奖为抽奖机会：此时就进行发放抽奖次数
            if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.DRAW) {
                CompletableFuture.runAsync(() -> this.giveLotteryTimes(request, lotteryActivityDO, rewardSettingBO), springAsyncConfig.getAsyncExecutor());
            }
            CompletableFuture.runAsync(() -> this.cashReward(lotteryActivityDO.getId(), map.get("joinDetailId")), springAsyncConfig.getAsyncExecutor());
        }

        return lotteryResultBO;
    }

    public void giveLotteryTimes(ReduceLotteryTimesRequest request, LotteryActivityDO lotteryActivityDO, LotteryActivityRewardSettingBO rewardSettingBO) {
        AddLotteryTimesRequest addLotteryTimesRequest = new AddLotteryTimesRequest();
        addLotteryTimesRequest.setLotteryActivityId(lotteryActivityDO.getId());
        addLotteryTimesRequest.setActivityName(lotteryActivityDO.getActivityName());
        addLotteryTimesRequest.setPlatformType(request.getPlatformType());
        addLotteryTimesRequest.setUid(request.getUid());
        if (request.getPlatformType() == 1) {
            addLotteryTimesRequest.setUname(Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO()).getName());
        } else {
            addLotteryTimesRequest.setUname(Optional.ofNullable(userApi.getById(request.getUid())).orElse(new UserDTO()).getNickName());
        }
        addLotteryTimesRequest.setTimes(rewardSettingBO.getRewardNumber());
        addLotteryTimesRequest.setGetType(LotteryActivityGetTypeEnum.DRAW_GIVE.getCode());
        this.addLotteryTimes(addLotteryTimesRequest);
        log.info("抽奖活动ID={}发放奖品 抽奖用户UID={} 中奖类型={} 赠送抽奖次数数量={}", lotteryActivityDO.getId(), request.getUid(), rewardSettingBO.getRewardType(), rewardSettingBO.getRewardNumber());
    }

    /**
     * 中奖后发放奖品、扣减库存
     *
     * @param rewardSettingBO 奖品信息
     * @param lotteryActivityDO 抽奖活动信息
     * @param platformType 平台：1-B端 2-C端
     * @param uid 用户ID
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean giveReward(LotteryActivityRewardSettingBO rewardSettingBO, LotteryActivityDO lotteryActivityDO, Integer platformType, Long uid) {
        boolean result = true;
        // 发放奖品
        if (platformType == 1 && (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.GOODS_COUPON
                || LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.MEMBER_COUPON)) {

            // 中奖为商品优惠券/会员优惠券
            Map<Long, Integer> remainMap = couponActivityService.getRemainByActivityIds(ListUtil.toList(rewardSettingBO.getRewardId()));
            if (Objects.nonNull(remainMap.get(rewardSettingBO.getRewardId())) && remainMap.get(rewardSettingBO.getRewardId()) > 0) {
                EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(uid)).orElse(new EnterpriseDTO());
                List<SaveCouponRequest>  requests = ListUtil.toList();
                for (int i = 0; i < rewardSettingBO.getRewardNumber(); i++) {
                    SaveCouponRequest saveCouponRequest = new SaveCouponRequest();
                    saveCouponRequest.setCouponActivityId(rewardSettingBO.getRewardId());
                    saveCouponRequest.setEid(uid);
                    saveCouponRequest.setEname(enterpriseDTO.getName());
                    saveCouponRequest.setCouponActivityAutoId(lotteryActivityDO.getId());
                    saveCouponRequest.setCouponActivityAutoName(lotteryActivityDO.getActivityName());
                    saveCouponRequest.setGetType(CouponGetTypeEnum.GIFT.getCode());
                    requests.add(saveCouponRequest);
                }
                result = couponActivityService.giveCoupon(requests);
                log.info("抽奖活动ID={} 平台类型={} 用户ID={} 中奖后调用发放优惠券接口返回结果={} 参数={}", lotteryActivityDO.getId(), platformType, uid, result, JSONObject.toJSONString(requests));
            }

        } else if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            // 扣减赠品库库存
            result = this.reduceStock(lotteryActivityDO.getId(), rewardSettingBO.getRewardId(), rewardSettingBO.getRewardNumber());

        } else if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS) {
            // 真实物品：中奖后就要扣减库存，但不更新兑付状态，在运营后台填写快递单号时修改为已兑付
            result = this.reduceStock(lotteryActivityDO.getId(), rewardSettingBO.getRewardId(), rewardSettingBO.getRewardNumber());

        }
        return result;
    }

    /**
     * 更新兑付状态
     *
     * @param lotteryActivityId
     * @param joinDetailId
     */
    private void cashReward(Long lotteryActivityId, Long joinDetailId) {
        UpdateCashRewardRequest cashRewardRequest = new UpdateCashRewardRequest();
        cashRewardRequest.setLotteryActivityId(lotteryActivityId);
        cashRewardRequest.setCashType(LotteryActivityCashTypeEnum.ONE.getCode());
        cashRewardRequest.setId(joinDetailId);
        lotteryActivityJoinDetailService.cashReward(cashRewardRequest);
        log.info("中奖后更新兑付状态成功 抽奖活动ID={} 活动参与ID={}", lotteryActivityId, joinDetailId);
    }

    /**
     * 扣减赠品库库存
     *
     * @param lotteryActivityId 抽奖活动ID
     * @param rewardId 赠品库Id
     * @param rewardNumber 奖品数量
     */
    private boolean reduceStock(Long lotteryActivityId, Long rewardId, Integer rewardNumber) {
        boolean deduct = goodsGiftService.deduct(rewardNumber, rewardId);
        if (deduct) {
            log.info("抽奖活动中奖扣减库存成功 抽奖活动ID={} 奖品ID={} 奖品数量={}", lotteryActivityId, rewardId, rewardNumber);
        } else {
            log.error("抽奖活动中奖扣减库存失败 抽奖活动ID={} 奖品ID={} 奖品数量={}", lotteryActivityId, rewardId, rewardNumber);
        }
        return deduct;
    }

    /**
     * 获取抽奖活动信息
     *
     * @param lotteryActivityId
     * @return
     */
    private LotteryActivityDO getLotteryActivityDO(Long lotteryActivityId) {
        // 抽奖前校验
        LotteryActivityDO lotteryActivityDO = Optional.ofNullable(lotteryActivityService.getById(lotteryActivityId)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
        // 校验活动状态
        if (EnableStatusEnum.getByCode(lotteryActivityDO.getStatus()) != EnableStatusEnum.ENABLED) {
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_HAD_END);
        }
        Date now = new Date();
        if (lotteryActivityDO.getStartTime().after(now)) {
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_START);
        }
        if (lotteryActivityDO.getEndTime().before(now)) {
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_HAD_END);
        }
        return lotteryActivityDO;
    }

    @Override
    public Integer sign(SignAddTimesRequest request) {
        // 校验活动
        Long lotteryActivityId = request.getLotteryActivityId();
        LotteryActivityDO lotteryActivityDO = this.getLotteryActivityDO(lotteryActivityId);
        // 校验当日是否已经签到
        boolean todaySignFlag = lotteryActivitySignRecordService.checkTodaySign(lotteryActivityId, request.getPlatformType(), request.getUid());
        if (todaySignFlag) {
            throw new BusinessException(LotteryActivityErrorCode.TODAY_HAD_SIGN);
        }

        String uname;
        if (LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
            // B端为企业名称
            EnterpriseDTO enterpriseDTO = Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO());
            uname = enterpriseDTO.getName();
        } else {
            // C端为用户昵称
            UserDTO userDTO = Optional.ofNullable(userApi.getById(request.getUid())).orElse(new UserDTO());
            uname = userDTO.getNickName();
        }

        // 获取签到送多少抽奖次数
        LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(lotteryActivityId);
        if (Objects.isNull(joinRuleDTO) || joinRuleDTO.getSignGive() == 0) {
            throw new BusinessException(LotteryActivityErrorCode.UN_SETTING_SIGN_GIVE);
        }

        // 增加抽奖次数
        AddLotteryTimesRequest timesRequest = new AddLotteryTimesRequest();
        timesRequest.setLotteryActivityId(lotteryActivityId);
        timesRequest.setActivityName(lotteryActivityDO.getActivityName());
        timesRequest.setPlatformType(request.getPlatformType());
        timesRequest.setUid(request.getUid());
        timesRequest.setUname(uname);
        timesRequest.setTimes(joinRuleDTO.getSignGive());
        timesRequest.setGetType(LotteryActivityGetTypeEnum.SIGN.getCode());
        timesRequest.setOpUserId(request.getOpUserId());
        Integer lotteryTimes = this.addLotteryTimes(timesRequest);

        // 增加签到记录
        LotteryActivitySignRecordDO signRecordDO = PojoUtils.map(request, LotteryActivitySignRecordDO.class);
        signRecordDO.setSignTime(new Date());
        lotteryActivitySignRecordService.save(signRecordDO);
        log.info("抽奖活动ID={} 用户ID={} 签到完成后的抽奖次数为={}", lotteryActivityId, request.getUid(), lotteryTimes);

        return lotteryTimes;
    }

    /**
     * 每日0点清零C端用户的抽奖次数
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean clearUserLotteryTimes() {
        // 查询出所有C端用户抽奖次数
        LambdaQueryWrapper<LotteryActivityTimesDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityTimesDO::getPlatformType, 2);
        wrapper.gt(LotteryActivityTimesDO::getAvailableTimes, 0);
        List<LotteryActivityTimesDO> activityTimesDOList = this.list(wrapper);

        List<LotteryActivityTimesChangeLogDO> list = ListUtil.toList();
        activityTimesDOList.forEach(lotteryActivityTimesDO -> {
            Integer beforeTimes = lotteryActivityTimesDO.getAvailableTimes();

            LotteryActivityTimesChangeLogDO changeLogDO = new LotteryActivityTimesChangeLogDO();
            changeLogDO.setLotteryActivityId(lotteryActivityTimesDO.getLotteryActivityId());
            changeLogDO.setPlatformType(lotteryActivityTimesDO.getPlatformType());
            changeLogDO.setUid(lotteryActivityTimesDO.getUid());
            changeLogDO.setChangeType(LotteryTimesChangeTypeEnum.CLEAR_ZERO.getCode());
            changeLogDO.setBeforeTimes(beforeTimes);
            changeLogDO.setChangeTimes(-beforeTimes);
            changeLogDO.setAfterTimes(0);
            list.add(changeLogDO);
        });

        if (CollUtil.isNotEmpty(list)) {
            // 抽奖次数清零
            this.baseMapper.clearLotteryTimes();
            // 抽奖次数变更日志
            lotteryActivityTimesChangeLogService.saveBatch(list);
        }
        log.info("每日0点清零C端用户的抽奖次数完成 当前清零抽奖次数={}", list.size());

        return true;
    }

    /**
     * 执行抽奖
     *
     * @param rewardSettingBOList
     */
    private int drawGift(List<LotteryActivityRewardSettingBO> rewardSettingBOList){
        if (CollUtil.isEmpty(rewardSettingBOList)) {
            return -1;
        }

        List<BigDecimal> orgProbabilityList = ListUtil.toList();
        rewardSettingBOList.forEach(rewardSettingBO -> orgProbabilityList.add(rewardSettingBO.getHitProbability()));

        List<BigDecimal> sortRateList = ListUtil.toList();
        // 计算概率总和
        BigDecimal sumProbability = rewardSettingBOList.stream().map(LotteryActivityRewardSettingBO::getHitProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sumProbability.compareTo(BigDecimal.ZERO) > 0) {
            // 概率所占比例
            BigDecimal rate = BigDecimal.ZERO;

            for (BigDecimal prob : orgProbabilityList) {
                rate = rate.add(prob);
                // 构建一个比例区段组成的集合（避免概率和不为1），可能存在除不尽的可能，因此先转换为double进行除
                sortRateList.add(BigDecimal.valueOf(rate.doubleValue()/sumProbability.doubleValue()));
            }

            // 随机生成一个随机数并排序
            ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
            double random = threadLocalRandom.nextDouble(0, 1);
            sortRateList.add(BigDecimal.valueOf(random));
            Collections.sort(sortRateList);

            // 返回该随机数在比例集合中的索引
            return sortRateList.indexOf(BigDecimal.valueOf(random));
        }

        return -1;

    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Long> reduceLotteryTimes(ReduceLotteryTimesRequest request, LotteryActivityTimesDO activityTimesDO, LotteryActivityRewardSettingBO rewardSettingBO,
                                      String activityName, Integer platform) {
        // 减少剩余的抽奖次数
        Integer hadUseTimes = activityTimesDO.getUseTimes();
        Integer beforeLotteryTimes = activityTimesDO.getAvailableTimes();
        Integer afterLotteryTimes = beforeLotteryTimes - 1;

        activityTimesDO.setAvailableTimes(afterLotteryTimes);
        activityTimesDO.setUseTimes(hadUseTimes + 1);
        this.updateById(activityTimesDO);

        // 添加抽奖明细
        AddLotteryActivityJoinDetailRequest joinDetailRequest = PojoUtils.map(rewardSettingBO, AddLotteryActivityJoinDetailRequest.class);
        joinDetailRequest.setActivityName(activityName);
        joinDetailRequest.setPlatformType(platform);
        joinDetailRequest.setUid(request.getUid());
        if (platform == 1) {
            joinDetailRequest.setUname(Optional.ofNullable(enterpriseApi.getById(request.getUid())).orElse(new EnterpriseDTO()).getName());
        } else {
            joinDetailRequest.setUname(Optional.ofNullable(userApi.getById(request.getUid())).orElse(new UserDTO()).getNickName());
        }
        if (Objects.nonNull(request.getShopEid()) && request.getShopEid() != 0) {
            joinDetailRequest.setShopEid(request.getShopEid());
            joinDetailRequest.setShopEname(Optional.ofNullable(enterpriseApi.getById(request.getShopEid())).orElse(new EnterpriseDTO()).getName());
        }
        joinDetailRequest.setLotteryTime(new Date());
        Long joinDetailId = lotteryActivityJoinDetailService.addJoinDetail(joinDetailRequest);

        // 抽奖次数变更日志
        LotteryActivityTimesChangeLogDO timesChangeLogDO = PojoUtils.map(request, LotteryActivityTimesChangeLogDO.class);
        timesChangeLogDO.setBeforeTimes(beforeLotteryTimes);
        timesChangeLogDO.setAfterTimes(afterLotteryTimes);
        timesChangeLogDO.setChangeTimes(-1);
        timesChangeLogDO.setChangeType(LotteryTimesChangeTypeEnum.LOTTERY_USE.getCode());
        lotteryActivityTimesChangeLogService.save(timesChangeLogDO);
        log.info("抽奖活动进行抽奖减少剩余次数 当前用户ID={} 抽奖活动ID={} 变更前次数={} 变更次数={} 变更后次数={}",
                request.getUid(), request.getLotteryActivityId(), beforeLotteryTimes, -1, afterLotteryTimes);

        Map<String, Long> map = MapUtil.newHashMap();
        map.put("joinDetailId", joinDetailId);
        map.put("lotteryTimes", Long.valueOf(afterLotteryTimes));

        return map;
    }

    @Transactional(rollbackFor = Exception.class)
    public int addLotteryTimesTx(AddLotteryTimesRequest request, LotteryActivityTimesDO activityTimesDO) {
        int afterTimes;
        int beforeTimes = 0;
        if (Objects.isNull(activityTimesDO)) {
            afterTimes = request.getTimes();

            LotteryActivityTimesDO timesDO = PojoUtils.map(request, LotteryActivityTimesDO.class);
            timesDO.setAvailableTimes(request.getTimes());
            this.save(timesDO);

        } else {
            beforeTimes = activityTimesDO.getAvailableTimes();
            afterTimes = activityTimesDO.getAvailableTimes() + request.getTimes();

            activityTimesDO.setAvailableTimes(afterTimes);
            this.updateById(activityTimesDO);
        }

        // 增加抽奖次数变更日志
        LotteryActivityTimesChangeLogDO timesChangeLogDO = PojoUtils.map(request, LotteryActivityTimesChangeLogDO.class);
        timesChangeLogDO.setBeforeTimes(beforeTimes);
        timesChangeLogDO.setAfterTimes(afterTimes);
        timesChangeLogDO.setChangeTimes(request.getTimes());
        timesChangeLogDO.setChangeType(request.getGetType());
        lotteryActivityTimesChangeLogService.save(timesChangeLogDO);

        // 增加获取抽奖机会明细表记录
        LotteryActivityGetDO activityGetDO = PojoUtils.map(request, LotteryActivityGetDO.class);
        activityGetDO.setGetTimes(request.getTimes());
        lotteryActivityGetService.save(activityGetDO);

        log.info("抽奖活动增加抽奖次数 当前企业ID={} 抽奖活动ID={} 变更类型={} 变更前次数={} 变更次数={} 变更后次数={}",
                request.getUid(), request.getLotteryActivityId(), request.getGetType(), beforeTimes, request.getTimes(), afterTimes);

        return afterTimes;
    }

    /**
     * 获取抽奖次数对象
     *
     * @param lotteryActivityId 抽奖活动ID
     * @param platformType 平台类型：1-B端 2-C端
     * @param uid 用户ID
     * @return
     */
    private LotteryActivityTimesDO getLotteryActivityTimesDO(Long lotteryActivityId, Integer platformType, Long uid) {
        LambdaQueryWrapper<LotteryActivityTimesDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityTimesDO::getLotteryActivityId, lotteryActivityId);
        wrapper.eq(LotteryActivityTimesDO::getPlatformType, platformType);
        wrapper.eq(LotteryActivityTimesDO::getUid, uid);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    /**
     * 获取抽奖次数分布式锁名称
     *
     * @param lotteryActivityId
     * @param platformType
     * @param uid
     * @return
     */
    private String getLockName(Long lotteryActivityId, Integer platformType, Long uid) {
        return "lottery_activity:lottery_times" + ":" + lotteryActivityId + ":" + platformType + ":" + uid;
    }

    /**
     * 获取库存分布式锁名称
     *
     * @param rewardType
     * @param rewardId
     * @return
     */
    private String getStockLockName(Integer rewardType, Long rewardId) {
        return "lottery_activity:reward_stock" + ":" + rewardType + ":" + rewardId;
    }

}
