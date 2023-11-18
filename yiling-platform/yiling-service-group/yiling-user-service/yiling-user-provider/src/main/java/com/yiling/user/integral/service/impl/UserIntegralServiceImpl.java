package com.yiling.user.integral.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.AddIntegralOrderGiveRequest;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveMatchRuleRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveUseRecordRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralGiveUseRecordDO;
import com.yiling.user.integral.entity.IntegralOrderRecordDetailDO;
import com.yiling.user.integral.entity.UserIntegralChangeLogDO;
import com.yiling.user.integral.entity.UserIntegralDO;
import com.yiling.user.integral.dao.UserIntegralMapper;
import com.yiling.user.integral.enums.IntegralBehaviorEnum;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.integral.service.IntegralBehaviorService;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;
import com.yiling.user.integral.service.IntegralOrderRecordDetailService;
import com.yiling.user.integral.service.UserIntegralChangeLogService;
import com.yiling.user.integral.service.UserIntegralService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.member.service.EnterpriseMemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户积分表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
@Slf4j
@Service
public class UserIntegralServiceImpl extends BaseServiceImpl<UserIntegralMapper, UserIntegralDO> implements UserIntegralService {

    @Autowired
    RedisDistributedLock redisDistributedLock;
    @Autowired
    UserIntegralChangeLogService userIntegralChangeLogService;
    @Autowired
    IntegralGiveUseRecordService integralGiveUseRecordService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    IntegralBehaviorService integralBehaviorService;
    @Autowired
    IntegralGiveRuleService integralGiveRuleService;
    @Autowired
    IntegralOrderGiveConfigService integralOrderGiveConfigService;
    @Autowired
    EnterpriseMemberService enterpriseMemberService;
    @Autowired
    IntegralOrderRecordDetailService integralOrderRecordDetailService;

    @Lazy
    @Autowired
    UserIntegralServiceImpl _this;

    @Override
    public Integer getUserIntegralByUid(Long uid, Integer platform) {
        LambdaQueryWrapper<UserIntegralDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIntegralDO::getUid, uid);
        wrapper.eq(UserIntegralDO::getPlatform, platform);
        wrapper.last("limit 1");
        UserIntegralDO userIntegralDO = Optional.ofNullable(this.getOne(wrapper)).orElse(new UserIntegralDO());
        return Objects.nonNull(userIntegralDO.getIntegralValue()) ? userIntegralDO.getIntegralValue() : 0;
    }

    @Override
    public UserIntegralDO getUserIntegral(Long uid, Integer platform) {
        LambdaQueryWrapper<UserIntegralDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIntegralDO::getUid, uid);
        wrapper.eq(UserIntegralDO::getPlatform, platform);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean giveIntegralByOrder(AddIntegralOrderGiveRequest request) {
        log.info("订单确认收货送积分 订单号={} 入参={}", request.getOrderNo(), JSONObject.toJSONString(request));
        if (CollUtil.isEmpty(request.getGoodsInfoList())) {
            throw new BusinessException(ResultCode.PARAM_MISS, "商品信息不能为空");
        }

        // 校验是否存在订单送积分的规则
        List<IntegralGiveRuleDTO> validGiveRuleList = integralGiveRuleService.getDoingGiveRuleList();
        IntegralBehaviorDTO behaviorDTO = Optional.ofNullable(integralBehaviorService.getByName(IntegralBehaviorEnum.ORDER_GIVE_INTEGRAL.getName())).orElse(new IntegralBehaviorDTO());
        List<IntegralGiveRuleDTO> giveRuleDTOList = validGiveRuleList.stream().filter(integralGiveRuleDTO -> integralGiveRuleDTO.getBehaviorId().equals(behaviorDTO.getId())).collect(Collectors.toList());
        if (CollUtil.isEmpty(giveRuleDTOList)) {
            log.info("订单确认收货送积分 订单号={} 没有有效的赠送规则", request.getOrderNo());
            return true;
        }

        BigDecimal sumIntegralValue = BigDecimal.ZERO;
        List<IntegralOrderRecordDetailDO> recordDetailDOList = ListUtil.toList();
        // 自动匹配订单送积分倍数配置
        List<AddIntegralOrderGiveRequest.GoodsInfo> goodsInfoList = request.getGoodsInfoList();
        for (AddIntegralOrderGiveRequest.GoodsInfo goodsInfo : goodsInfoList) {
            QueryIntegralGiveMatchRuleRequest matchRuleRequest = new QueryIntegralGiveMatchRuleRequest();
            matchRuleRequest.setEid(request.getEid());
            matchRuleRequest.setUid(request.getUid());
            matchRuleRequest.setGoodsId(goodsInfo.getGoodsId());
            matchRuleRequest.setStandardId(goodsInfo.getStandardId());
            matchRuleRequest.setSpecificationId(goodsInfo.getSellSpecificationsId());
            matchRuleRequest.setPaymentMethod(request.getPaymentMethod());
            matchRuleRequest.setOrderNO(request.getOrderNo());
            GenerateMultipleConfigDTO multipleConfigDTO = integralOrderGiveConfigService.autoMatchRule(matchRuleRequest);

            if (Objects.isNull(multipleConfigDTO)) {
                log.info("订单送积分自动匹配倍数规则为空 订单号={} 请求数据={}", request.getOrderNo(), JSONObject.toJSONString(matchRuleRequest));
                continue;
            }
            log.info("订单送积分自动匹配倍数规则 订单号={} 匹配到的规则为={}", request.getOrderNo(), JSONObject.toJSONString(multipleConfigDTO));
            // 计算该商品对应的积分值
            BigDecimal integralValue = goodsInfo.getGoodsAmount().multiply(multipleConfigDTO.getIntegralMultiple()).setScale(0, BigDecimal.ROUND_HALF_UP);
            sumIntegralValue = sumIntegralValue.add(integralValue);

            // 积分发放记录订单明细
            IntegralOrderRecordDetailDO recordDetailDO = new IntegralOrderRecordDetailDO();
            recordDetailDO.setOrderId(request.getOrderId());
            recordDetailDO.setOrderNo(request.getOrderNo());
            recordDetailDO.setRuleId(multipleConfigDTO.getGiveRuleId());
            recordDetailDO.setRuleName(Optional.ofNullable(integralGiveRuleService.getById(multipleConfigDTO.getGiveRuleId())).orElse(new IntegralGiveRuleDO()).getName());
            recordDetailDO.setGoodsId(goodsInfo.getGoodsId());
            recordDetailDO.setGoodsNum(goodsInfo.getGoodsNum());
            recordDetailDO.setOrderAmount(goodsInfo.getGoodsAmount());
            recordDetailDO.setIntegralMultiple(multipleConfigDTO.getIntegralMultiple());
            recordDetailDO.setIntegralValue(integralValue.intValue());
            recordDetailDO.setOpUserId(request.getOpUserId());
            recordDetailDOList.add(recordDetailDO);

        }

        if (sumIntegralValue.compareTo(BigDecimal.ZERO) == 0) {
            log.info("订单确认收货送积分时该订单号={} 未匹配到积分规则", request.getOrderNo());
            return true;
        }

        // 发放积分（积分变更和积分操作日志）
        UpdateIUserIntegralRequest integralRequest = new UpdateIUserIntegralRequest();
        integralRequest.setPlatform(request.getPlatform());
        integralRequest.setUid(request.getUid());
        integralRequest.setChangeType(UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL.getCode());
        integralRequest.setIntegralValue(sumIntegralValue.intValue());
        integralRequest.setOpUserId(request.getOpUserId());
        this.updateIntegral(integralRequest);

        // 增加积分发放日志
        AddIntegralRecordRequest integralRecordRequest = new AddIntegralRecordRequest();
        integralRecordRequest.setPlatform(request.getPlatform());
        integralRecordRequest.setUid(request.getUid());
        integralRecordRequest.setUname(Optional.ofNullable(enterpriseService.getById(request.getUid())).orElse(new EnterpriseDO()).getName());
        integralRecordRequest.setChangeType(UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL.getCode());
        integralRecordRequest.setIntegralValue(sumIntegralValue.intValue());
        integralRecordRequest.setBehaviorId(behaviorDTO.getId());
        integralRecordRequest.setBehaviorName(IntegralBehaviorEnum.ORDER_GIVE_INTEGRAL.getName());
        integralRecordRequest.setOpRemark(request.getOrderNo());
        integralRecordRequest.setOpUserId(request.getOpUserId());
        IntegralGiveUseRecordDO giveUseRecordDO = integralGiveUseRecordService.addRecord(integralRecordRequest);

        // 积分发放记录订单明细
        if (CollUtil.isNotEmpty(recordDetailDOList)) {
            recordDetailDOList.forEach(integralOrderRecordDetailDO -> integralOrderRecordDetailDO.setRecordId(giveUseRecordDO.getId()));
            integralOrderRecordDetailService.saveBatch(recordDetailDOList);
        }
        log.info("订单确认收货送积分完成 订单号={} 赠送积分={}", request.getOrderNo(), sumIntegralValue.intValue());

        return true;
    }

    @Override
    public boolean clearIntegral(Integer platform) {
        IntegralBehaviorDTO behaviorDTO = Optional.ofNullable(integralBehaviorService.getByName(IntegralBehaviorEnum.EXPIRED_INVALID.getName())).orElse(new IntegralBehaviorDTO());
        // B2B平台：每年1月1日零时，清零上上年度12月1日至上年度11月30日的积分
        if (platform.equals(IntegralRulePlatformEnum.B2B.getCode())) {
            int year = DateUtil.year(new Date());
            String decStartDateStr = (year -1 ) + "-12-01";
            String decEndDateStr = (year -1 ) + "-12-31";
            DateTime decStartDate = DateUtil.parse(decStartDateStr, "yyyy-MM-dd");
            DateTime decEndDate = DateUtil.parse(decEndDateStr, "yyyy-MM-dd");

            LambdaQueryWrapper<UserIntegralDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserIntegralDO::getPlatform, platform);
            wrapper.gt(UserIntegralDO::getIntegralValue, 0);
            List<UserIntegralDO> userIntegralDOList = this.list(wrapper);

            userIntegralDOList.forEach(userIntegralDO -> {

                // 查询去年的12月1号到12月31号有没有发放积分的记录，没有则直接全部清零
                QueryIntegralGiveUseRecordRequest recordRequest = new QueryIntegralGiveUseRecordRequest();
                recordRequest.setPlatform(platform);
                recordRequest.setUid(userIntegralDO.getUid());
                recordRequest.setChangeTypeList(ListUtil.toList(UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL.getCode(), UserIntegralChangeTypeEnum.SIGN_GIVE_INTEGRAL.getCode()));
                recordRequest.setStartOperTime(DateUtil.beginOfDay(decStartDate));
                recordRequest.setEndOperTime(DateUtil.endOfDay(decEndDate));
                List<IntegralGiveUseRecordDTO> useRecordDTOList = integralGiveUseRecordService.queryList(recordRequest);

                UpdateIUserIntegralRequest request = new UpdateIUserIntegralRequest();
                request.setPlatform(platform);
                request.setUid(userIntegralDO.getUid());
                request.setChangeType(UserIntegralChangeTypeEnum.EXPIRED_INVALID.getCode());
                // 如果12月发放的为空，则直接全部清零积分
                if (CollUtil.isEmpty(useRecordDTOList)) {
                    request.setIntegralValue(userIntegralDO.getIntegralValue());
                    this.updateIntegral(request);
                    this.addIntegralCleanRecord(platform, userIntegralDO.getUid(), userIntegralDO.getIntegralValue(), behaviorDTO.getId());
                } else {
                    // 12-01 ~ 12-31发放的总积分值：如果12月发放的总值小于当前剩余的，则剩余的-12月发放的=需要扣减的；反之表示剩余的积分均为12月发放的积分，不用清零
                    int decGiveSum = useRecordDTOList.stream().mapToInt(IntegralGiveUseRecordDTO::getIntegralValue).sum();
                    if (decGiveSum < userIntegralDO.getIntegralValue()) {
                        request.setIntegralValue(userIntegralDO.getIntegralValue() - decGiveSum);
                        this.updateIntegral(request);
                        this.addIntegralCleanRecord(platform, userIntegralDO.getUid(), userIntegralDO.getIntegralValue() - decGiveSum, behaviorDTO.getId());
                    }
                }

            });
        }

        return true;
    }

    /**
     * 增加积分扣减记录
     *
     * @param platform 平台
     * @param uid UID
     * @param integralValue 积分值
     * @param behaviorId 行为ID
     */
    private void addIntegralCleanRecord(Integer platform, Long uid, Integer integralValue, Long behaviorId) {
        AddIntegralRecordRequest integralRecordRequest = new AddIntegralRecordRequest();
        integralRecordRequest.setPlatform(platform);
        integralRecordRequest.setUid(uid);
        if (platform.equals(IntegralRulePlatformEnum.B2B.getCode())) {
            integralRecordRequest.setUname(Optional.ofNullable(enterpriseService.getById(uid)).orElse(new EnterpriseDO()).getName());
        }
        integralRecordRequest.setChangeType(UserIntegralChangeTypeEnum.EXPIRED_INVALID.getCode());
        integralRecordRequest.setIntegralValue(integralValue);
        integralRecordRequest.setBehaviorId(behaviorId);
        integralRecordRequest.setBehaviorName(IntegralBehaviorEnum.EXPIRED_INVALID.getName());
        integralGiveUseRecordService.addRecord(integralRecordRequest);
    }

    @Override
    public Integer updateIntegral(UpdateIUserIntegralRequest request) {
        int afterIntegralValue;
        // 上锁
        String lockKey = this.getLockName(request.getPlatform(), request.getUid());
        String lockId = null;
        try {
            lockId = redisDistributedLock.lock2(lockKey, 3, 3, TimeUnit.SECONDS);

            UserIntegralDO userIntegral = this.getUserIntegral(request.getUid(), request.getPlatform());
            // 操作积分和记录日志
            afterIntegralValue = _this.updateIntegralTx(request, userIntegral);

        } finally {
            // 释放锁
            redisDistributedLock.releaseLock(lockKey, lockId);
        }
        log.info("用户更新积分完成 平台={} 用户ID={} 更新类型={} 更新的积分值={}", request.getPlatform(), request.getUid(), request.getChangeType(), request.getIntegralValue());

        return afterIntegralValue;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateIntegralTx(UpdateIUserIntegralRequest request, UserIntegralDO userIntegralDO) {
        int afterIntegralValue;
        int beforeIntegralValue = 0;
        if (Objects.isNull(userIntegralDO)) {
            afterIntegralValue = request.getIntegralValue();

            UserIntegralDO integralDO = PojoUtils.map(request, UserIntegralDO.class);
            integralDO.setIntegralValue(request.getIntegralValue());
            this.save(integralDO);

        } else {
            beforeIntegralValue = userIntegralDO.getIntegralValue();
            if (request.getChangeType().equals(UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL.getCode()) || request.getChangeType().equals(UserIntegralChangeTypeEnum.SIGN_GIVE_INTEGRAL.getCode())
                    || request.getChangeType().equals(UserIntegralChangeTypeEnum.DIRECTIONAL_GIVE.getCode())) {
                afterIntegralValue = userIntegralDO.getIntegralValue() + request.getIntegralValue();
            } else {
                afterIntegralValue = userIntegralDO.getIntegralValue() - request.getIntegralValue();
            }

            userIntegralDO.setIntegralValue(afterIntegralValue);
            this.updateById(userIntegralDO);
        }

        // 增加用户积分变更日志
        UserIntegralChangeLogDO integralChangeLogDO = PojoUtils.map(request, UserIntegralChangeLogDO.class);
        integralChangeLogDO.setBeforeIntegral(beforeIntegralValue);
        integralChangeLogDO.setAfterIntegral(afterIntegralValue);
        if (request.getChangeType().equals(UserIntegralChangeTypeEnum.ORDER_GIVE_INTEGRAL.getCode()) || request.getChangeType().equals(UserIntegralChangeTypeEnum.SIGN_GIVE_INTEGRAL.getCode())
                || request.getChangeType().equals(UserIntegralChangeTypeEnum.DIRECTIONAL_GIVE.getCode())) {
            integralChangeLogDO.setChangeIntegral(request.getIntegralValue());
        } else {
            integralChangeLogDO.setChangeIntegral(-request.getIntegralValue());
        }
        integralChangeLogDO.setChangeType(request.getChangeType());
        userIntegralChangeLogService.save(integralChangeLogDO);
        log.info("积分用户增加积分 当前平台={}-用户ID={} 变更类型={} 变更前次数={} 变更次数={} 变更后次数={}", request.getPlatform(), request.getUid(), request.getChangeType(), beforeIntegralValue, integralChangeLogDO.getChangeIntegral(), afterIntegralValue);

        return afterIntegralValue;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanDirectionalGiveIntegral() {

        LambdaQueryWrapper<UserIntegralDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIntegralDO::getPlatform, IntegralRulePlatformEnum.B2B.getCode());
        wrapper.gt(UserIntegralDO::getIntegralValue, 0);
        wrapper.eq(UserIntegralDO::getCreateTime, DateUtil.parse("1970-01-01 00:00:00"));
        List<UserIntegralDO> userIntegralDOList = this.list(wrapper);

        userIntegralDOList.forEach(userIntegralDO -> {

            UpdateIUserIntegralRequest request = new UpdateIUserIntegralRequest();
            request.setPlatform(IntegralRulePlatformEnum.B2B.getCode());
            request.setUid(userIntegralDO.getUid());
            request.setChangeType(UserIntegralChangeTypeEnum.EXPIRED_INVALID.getCode());
            request.setIntegralValue(userIntegralDO.getIntegralValue());
            this.updateIntegral(request);
            this.addIntegralCleanRecord(IntegralRulePlatformEnum.B2B.getCode(), userIntegralDO.getUid(), userIntegralDO.getIntegralValue(), 6L);

        });
        log.info("定时任务清零所有用户积分完成 清零的用户数量={}", userIntegralDOList.size());

        return true;
    }


    /**
     * 获取积分值分布式锁名称
     *
     * @param platform
     * @param uid
     * @return
     */
    private String getLockName(Integer platform, Long uid) {
        return "integral:integral_value" + ":" + platform + ":" + uid;
    }

}
