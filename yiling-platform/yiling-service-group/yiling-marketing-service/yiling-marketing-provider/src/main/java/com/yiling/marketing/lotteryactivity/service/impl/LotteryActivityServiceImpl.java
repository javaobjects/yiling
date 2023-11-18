package com.yiling.marketing.lotteryactivity.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.couponactivity.service.CouponActivityService;
import com.yiling.marketing.goodsgift.entity.GoodsGiftDO;
import com.yiling.marketing.goodsgift.service.GoodsGiftService;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityDetailBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityGiveScopeBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityItemBO;
import com.yiling.marketing.lotteryactivity.bo.LotteryActivityRewardSettingBO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGiveScopeDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRewardSettingDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleInstructionDTO;
import com.yiling.marketing.lotteryactivity.dto.request.AddLotteryActivityRewardSettingRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailNumberRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityListRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityBasicRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityGiveScopeRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivityJoinRuleRequest;
import com.yiling.marketing.lotteryactivity.dto.request.SaveLotteryActivitySettingRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityMapper;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityGiveScopeDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinRuleDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRewardSettingDO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRuleInstructionDO;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityErrorCode;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveEnterpriseTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveScopeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGiveUserTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityProgressEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityStatusEnum;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGetService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveEnterpriseTypeService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveMemberService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGivePromoterService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityGiveScopeService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinDetailService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRewardSettingService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRuleInstructionService;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityTimesService;
import com.yiling.marketing.strategy.service.StrategyActivityService;
import com.yiling.user.common.util.WrapperUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 抽奖活动表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Slf4j
@Service
public class LotteryActivityServiceImpl extends BaseServiceImpl<LotteryActivityMapper, LotteryActivityDO> implements LotteryActivityService {

    @Autowired
    LotteryActivityRewardSettingService lotteryActivityRewardSettingService;
    @Autowired
    LotteryActivityRuleInstructionService lotteryActivityRuleInstructionService;
    @Autowired
    LotteryActivityGiveEnterpriseTypeService lotteryActivityGiveEnterpriseTypeService;
    @Autowired
    LotteryActivityGiveEnterpriseService lotteryActivityGiveEnterpriseService;
    @Autowired
    LotteryActivityGiveMemberService lotteryActivityGiveMemberService;
    @Autowired
    LotteryActivityGivePromoterService lotteryActivityGivePromoterService;
    @Autowired
    LotteryActivityJoinDetailService lotteryActivityJoinDetailService;
    @Autowired
    LotteryActivityTimesService lotteryActivityTimesService;
    @Autowired
    LotteryActivityGiveScopeService lotteryActivityGiveScopeService;
    @Autowired
    LotteryActivityJoinRuleService lotteryActivityJoinRuleService;
    @Autowired
    LotteryActivityGetService lotteryActivityGetService;

    @Autowired
    GoodsGiftService goodsGiftService;
    @Autowired
    StrategyActivityService strategyActivityService;
    @Autowired
    CouponActivityService couponActivityService;

    @Value("${lottery.img.memberCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/vip.png}")
    private String memberCouponImg;
    @Value("${lottery.img.productCoupon:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/product.png}")
    private String productCouponImg;
    @Value("${lottery.img.emptyImg:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/cry.png}")
    private String emptyImg;
    @Value("${lottery.img.lotteryTimesImg:https://yl-public.oss-cn-zhangjiakou.aliyuncs.com/web/prd/html/img/prize.png}")
    private String lotteryTimesImg;

    @Override
    public Page<LotteryActivityItemBO> queryListPage(QueryLotteryActivityPageRequest request) {
        if (Objects.nonNull(request.getStartCreateTime())) {
            request.setStartCreateTime(DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (Objects.nonNull(request.getEndCreateTime())) {
            request.setEndCreateTime(DateUtil.endOfDay(request.getEndCreateTime()));
        }

        Page<LotteryActivityItemBO> lotteryActivityBOPage = baseMapper.queryListPage(request.getPage(), request);
        if (CollUtil.isEmpty(lotteryActivityBOPage.getRecords())) {
            return lotteryActivityBOPage;
        }

        List<Long> idList = lotteryActivityBOPage.getRecords().stream().map(BaseDTO::getId).collect(Collectors.toList());
        // 获取抽奖活动中奖数量
        QueryJoinDetailNumberRequest numberRequest = new QueryJoinDetailNumberRequest();
        numberRequest.setLotteryActivityIdList(idList);
        numberRequest.setNotInRewardTypeList(ListUtil.toList(LotteryActivityRewardTypeEnum.EMPTY.getCode()));
        Map<Long, Long> hitNumberMap = lotteryActivityJoinDetailService.getJoinDetailNumber(numberRequest);

        // 获取抽奖活动获得的抽奖机会、抽奖的数量
        Map<Long, Integer> getNumberMap = lotteryActivityGetService.getNumberByLotteryActivityId(idList);
        Map<Long, Integer> useTimesMap = lotteryActivityTimesService.getUseTimes(idList);

        // 查询关联活动数量
        Map<Long, Integer> giftMap = strategyActivityService.countStrategyByGiftId(idList);

        lotteryActivityBOPage.getRecords().forEach(lotteryActivityBO -> {
            // 活动进度
            Date now = new Date();
            if (lotteryActivityBO.getStartTime().after(now)) {
                lotteryActivityBO.setProgress(LotteryActivityProgressEnum.UNDO.getCode());
            } else if (lotteryActivityBO.getStartTime().before(now) && lotteryActivityBO.getEndTime().after(now)) {
                lotteryActivityBO.setProgress(LotteryActivityProgressEnum.GOING.getCode());
            } else if (lotteryActivityBO.getEndTime().before(now)) {
                lotteryActivityBO.setProgress(LotteryActivityProgressEnum.END.getCode());
            }

            // 参与/中奖次数
            lotteryActivityBO.setGetJoinNum(getNumberMap.getOrDefault(lotteryActivityBO.getId(), 0));
            lotteryActivityBO.setJoinNum(useTimesMap.getOrDefault(lotteryActivityBO.getId(), 0));
            lotteryActivityBO.setHitNum(Math.toIntExact(hitNumberMap.getOrDefault(lotteryActivityBO.getId(), 0L)));
            // 关联活动数量
            lotteryActivityBO.setUnionActivityNum(giftMap.getOrDefault(lotteryActivityBO.getId(), 0));
        });

        return lotteryActivityBOPage;
    }

    @Override
    public List<LotteryActivityDTO> queryList(QueryLotteryActivityListRequest request) {
        QueryWrapper<LotteryActivityDO> wrapper = WrapperUtils.getWrapper(request);

        if (Objects.nonNull(request.getProgress()) && request.getProgress() != 0) {
            if (LotteryActivityProgressEnum.GOING == LotteryActivityProgressEnum.getByCode(request.getProgress())) {
                wrapper.lambda().le(LotteryActivityDO::getStartTime, new Date());
                wrapper.lambda().gt(LotteryActivityDO::getEndTime, new Date());

            } else if (LotteryActivityProgressEnum.UNDO == LotteryActivityProgressEnum.getByCode(request.getProgress())) {
                wrapper.lambda().gt(LotteryActivityDO::getStartTime, new Date());
            } else if (LotteryActivityProgressEnum.END == LotteryActivityProgressEnum.getByCode(request.getProgress())) {
                wrapper.lambda().le(LotteryActivityDO::getEndTime, new Date());
            }
        }
        return PojoUtils.map(this.list(wrapper), LotteryActivityDTO.class);
    }

    @Override
    public LotteryActivityDetailBO get(Long id, LotteryActivityPlatformEnum platformEnum) {
        LotteryActivityDO lotteryActivityDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
        if (Objects.nonNull(platformEnum) && LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) != platformEnum) {
            throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST);
        }

        LotteryActivityDetailBO activityDetailBO = new LotteryActivityDetailBO();
        // 基础信息
        LotteryActivityDTO lotteryActivityBasic = PojoUtils.map(lotteryActivityDO, LotteryActivityDTO.class);
        activityDetailBO.setLotteryActivityBasic(lotteryActivityBasic);
        activityDetailBO.setBudgetAmount(lotteryActivityDO.getBudgetAmount());
        activityDetailBO.setBgPicture(lotteryActivityDO.getBgPicture());

        // 获取活动设置信息
        this.getActivityExtra(id, lotteryActivityDO, activityDetailBO);

        return activityDetailBO;
    }

    private void getActivityExtra(Long id, LotteryActivityDO lotteryActivityDO, LotteryActivityDetailBO activityDetailBO) {
        if (LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
            // B端赠送范围
            LotteryActivityGiveScopeDTO giveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(id);
            LotteryActivityGiveScopeBO giveScopeBO = PojoUtils.map(giveScopeDTO, LotteryActivityGiveScopeBO.class);
            // 统计数量：指定客户时，统计指定客户数量
            if (Objects.nonNull(giveScopeDTO) && LotteryActivityGiveScopeEnum.getByCode(giveScopeDTO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_CUSTOMER) {
                List<Long> list = lotteryActivityGiveEnterpriseService.getGiveEnterpriseByActivityId(id);
                giveScopeBO.setEidListCount(list.size());
            } else if (Objects.nonNull(giveScopeDTO) && LotteryActivityGiveScopeEnum.getByCode(giveScopeDTO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_SCOPE_CUSTOMER) {
                // 指定方案会员、指定推广方会员时数量
                if (LotteryActivityGiveUserTypeEnum.ASSIGN_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                    List<Long> list = lotteryActivityGiveMemberService.getGiveMemberByActivityId(id);
                    giveScopeBO.setMemberIdListCount(list.size());
                } else if (LotteryActivityGiveUserTypeEnum.ASSIGN_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType())) {
                    List<Long> list = lotteryActivityGivePromoterService.getGivePromoterByActivityId(id);
                    giveScopeBO.setPromoterIdListCount(list.size());
                }
            }

            // 指定范围企业类型集合
            if (Objects.nonNull(giveScopeDTO) && LotteryActivityGiveEnterpriseTypeEnum.getByCode(giveScopeDTO.getGiveEnterpriseType()) == LotteryActivityGiveEnterpriseTypeEnum.ASSIGN_TYPE) {
                List<Integer> enterpriseTypeList = lotteryActivityGiveEnterpriseTypeService.getByLotteryActivityId(id);
                giveScopeBO.setEnterpriseTypeList(enterpriseTypeList);
            }
            activityDetailBO.setActivityGiveScope(giveScopeBO);

        } else {
            // C端参与规则
            LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(id);
            activityDetailBO.setActivityJoinRule(joinRuleDTO);
        }

        // 抽奖活动奖品设置集合
        List<LotteryActivityRewardSettingDTO> rewardSettingDTOList = lotteryActivityRewardSettingService.getByLotteryActivityId(id);
        if (CollUtil.isNotEmpty(rewardSettingDTOList)) {
            // 根据抽奖活动奖品ID查询奖品剩余数量
            List<LotteryActivityRewardSettingBO> rewardSettingBOList = rewardSettingDTOList.stream().map(this::getRewardRemainNumber).collect(Collectors.toList());
            // 设置奖品图片地址
            rewardSettingBOList.forEach(rewardSettingBO -> {
                if (LotteryActivityRewardTypeEnum.REAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())
                        || LotteryActivityRewardTypeEnum.VIRTUAL_GOODS == LotteryActivityRewardTypeEnum.getByCode(rewardSettingBO.getRewardType())) {

                    GoodsGiftDO goodsGiftDO = Optional.ofNullable(goodsGiftService.getById(rewardSettingBO.getRewardId())).orElse(new GoodsGiftDO());
                    rewardSettingBO.setRewardImg(goodsGiftDO.getPictureUrl());
                } else {
                    rewardSettingBO.setRewardImg(this.getRewardImg(rewardSettingBO.getRewardType()));
                }
            });
            activityDetailBO.setActivityRewardSettingList(rewardSettingBOList);
        }

        // 活动规则说明
        LotteryActivityRuleInstructionDTO ruleInstructionDTO = Optional.ofNullable(lotteryActivityRuleInstructionService.getByLotteryActivityId(id)).orElse(new LotteryActivityRuleInstructionDTO());
        activityDetailBO.setContent(ruleInstructionDTO.getContent());
    }

    /**
     * 设置奖品图片地址
     *
     * @param rewardType
     */
    @Override
    public String getRewardImg(Integer rewardType) {
        LotteryActivityRewardTypeEnum rewardTypeEnum = LotteryActivityRewardTypeEnum.getByCode(rewardType);
        switch (rewardTypeEnum) {
            case REAL_GOODS:


            case GOODS_COUPON:
                return productCouponImg;

            case MEMBER_COUPON:
                return memberCouponImg;

            case EMPTY:
                return emptyImg;

            case DRAW:
                return lotteryTimesImg;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copyLottery(Long id, Long opUserId) {
        // 基础信息
        LotteryActivityDO lotteryActivityDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
        lotteryActivityDO.setId(null);
        lotteryActivityDO.setOpUserId(opUserId);
        lotteryActivityDO.setStatus(LotteryActivityStatusEnum.DRAFT.getCode());
        lotteryActivityDO.setStartTime(null);
        lotteryActivityDO.setEndTime(null);
        this.save(lotteryActivityDO);
        log.info("抽奖活动复制操作 复制的活动ID={} 新生成的活动ID={}", id, lotteryActivityDO.getId());

        // 设置信息
        if (LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
            // B端赠送范围
            LotteryActivityGiveScopeDTO giveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(id);
            LotteryActivityGiveScopeDO giveScopeDO = PojoUtils.map(giveScopeDTO, LotteryActivityGiveScopeDO.class);
            giveScopeDO.setId(null);
            giveScopeDO.setLotteryActivityId(lotteryActivityDO.getId());
            giveScopeDO.setOpUserId(opUserId);
            this.lotteryActivityGiveScopeService.save(giveScopeDO);

            if (LotteryActivityGiveScopeEnum.getByCode(giveScopeDO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_CUSTOMER) {
                List<Long> giveEnterpriseList = lotteryActivityGiveEnterpriseService.getGiveEnterpriseByActivityId(id);
                lotteryActivityGiveEnterpriseService.updateGiveEnterpriseByLotteryActivityId(lotteryActivityDO.getId(), giveEnterpriseList, opUserId);

            } else if (LotteryActivityGiveScopeEnum.getByCode(giveScopeDO.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_SCOPE_CUSTOMER) {

                // 指定范围企业类型集合
                if (LotteryActivityGiveEnterpriseTypeEnum.getByCode(giveScopeDTO.getGiveEnterpriseType()) == LotteryActivityGiveEnterpriseTypeEnum.ASSIGN_TYPE) {
                    List<Integer> enterpriseTypeList = lotteryActivityGiveEnterpriseTypeService.getByLotteryActivityId(id);
                    // 保存企业类型
                    lotteryActivityGiveEnterpriseTypeService.updateGiveEnterpriseTypeByLotteryActivityId(lotteryActivityDO.getId(), enterpriseTypeList, opUserId);
                }

                // 指定范围用户类型
                if (LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType()) == LotteryActivityGiveUserTypeEnum.ASSIGN_MEMBER) {
                    // 保存指定方案会员
                    List<Long> memberIdList = lotteryActivityGiveMemberService.getGiveMemberByActivityId(id);
                    lotteryActivityGiveMemberService.updateGiveMemberByLotteryActivityId(lotteryActivityDO.getId(), memberIdList, opUserId);

                } else if (LotteryActivityGiveUserTypeEnum.getByCode(giveScopeDTO.getGiveUserType()) == LotteryActivityGiveUserTypeEnum.ASSIGN_PROMOTER_MEMBER) {
                    // 保存指定推广方会员
                    List<Long> givePromoterIdList = lotteryActivityGivePromoterService.getGivePromoterByActivityId(id);
                    lotteryActivityGivePromoterService.updateGivePromoterByLotteryActivityId(lotteryActivityDO.getId(), givePromoterIdList, opUserId);
                }
            }

        } else {
            // 保存C端参与规则
            LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(id);
            LotteryActivityJoinRuleDO joinRuleDO = PojoUtils.map(joinRuleDTO, LotteryActivityJoinRuleDO.class);
            joinRuleDO.setId(null);
            joinRuleDO.setLotteryActivityId(lotteryActivityDO.getId());
            joinRuleDO.setOpUserId(opUserId);
            lotteryActivityJoinRuleService.save(joinRuleDO);
        }

        // 奖品设置
        List<LotteryActivityRewardSettingDTO> rewardSettingDTOList = lotteryActivityRewardSettingService.getByLotteryActivityId(id);
        List<LotteryActivityRewardSettingDO> rewardSettingDOList = PojoUtils.map(rewardSettingDTOList, LotteryActivityRewardSettingDO.class);
        rewardSettingDOList.forEach(lotteryActivityRewardSettingDO -> {
            lotteryActivityRewardSettingDO.setLotteryActivityId(lotteryActivityDO.getId());
            lotteryActivityRewardSettingDO.setOpUserId(opUserId);
        });
        lotteryActivityRewardSettingService.saveBatch(rewardSettingDOList);

        // 活动规则说明
        LotteryActivityRuleInstructionDTO ruleInstructionDTO = lotteryActivityRuleInstructionService.getByLotteryActivityId(id);
        if (Objects.nonNull(ruleInstructionDTO)) {
            LotteryActivityRuleInstructionDO ruleInstructionDO = new LotteryActivityRuleInstructionDO();
            ruleInstructionDO.setLotteryActivityId(lotteryActivityDO.getId());
            ruleInstructionDO.setContent(ruleInstructionDTO.getContent());
            ruleInstructionDO.setOpUserId(opUserId);
            lotteryActivityRuleInstructionService.save(ruleInstructionDO);
        }

        return lotteryActivityDO.getId();
    }

    @Override
    public LotteryActivityRewardSettingBO getRewardRemainNumber(LotteryActivityRewardSettingDTO rewardSettingDTO) {
        LotteryActivityRewardSettingBO rewardSettingBO = PojoUtils.map(rewardSettingDTO, LotteryActivityRewardSettingBO.class);
        if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS || LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
            // 真实物品和虚拟物品查询赠品库产品库存
            Map<Long, Long> availQuentMap = goodsGiftService.getAvailQuentByIds(ListUtil.toList(rewardSettingDTO.getRewardId()));
            rewardSettingBO.setRemainNumber(Math.toIntExact(availQuentMap.getOrDefault(rewardSettingDTO.getRewardId(), 0L)));

        } else if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.GOODS_COUPON || LotteryActivityRewardTypeEnum.getByCode(rewardSettingDTO.getRewardType()) == LotteryActivityRewardTypeEnum.MEMBER_COUPON) {
            // 商品优惠券查询商品优惠券库存 或 会员优惠券查询会员优惠券库存
            Map<Long, Integer> remainMap = couponActivityService.getRemainByActivityIds(ListUtil.toList(rewardSettingDTO.getRewardId()));
            rewardSettingBO.setRemainNumber(remainMap.getOrDefault(rewardSettingDTO.getRewardId(), 0));
        }

        return rewardSettingBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LotteryActivityDTO saveActivityBasic(SaveLotteryActivityBasicRequest activityBasicRequest) {
        Long id = activityBasicRequest.getId();
        if (Objects.isNull(id) || id == 0) {
            // 校验抽奖活动时间不能在当前时间之前
            if (activityBasicRequest.getStartTime().before(new Date())) {
                throw new BusinessException(LotteryActivityErrorCode.ACTIVITY_START_TIME_ERROR);
            }
            // 名称不能重复
            LotteryActivityDO activityDO = getLotteryActivityByName(activityBasicRequest.getActivityName());
            if (Objects.nonNull(activityDO)) {
                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NAME_EXIST);
            }

            LotteryActivityDO lotteryActivityDO = PojoUtils.map(activityBasicRequest, LotteryActivityDO.class);
            lotteryActivityDO.setStatus(LotteryActivityStatusEnum.ENABLED.getCode());
            this.save(lotteryActivityDO);
            return PojoUtils.map(lotteryActivityDO, LotteryActivityDTO.class);

        } else {
            LotteryActivityDO lotteryActivityDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
            // 校验状态
            if (activityBasicRequest.getStartTime().before(new Date())) {
                throw new BusinessException(LotteryActivityErrorCode.ACTIVITY_START_TIME_ERROR);
            }
            if (lotteryActivityDO.getStatus().equals(LotteryActivityStatusEnum.DISABLED.getCode())) {
                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_HAD_STOP);
            }
            LotteryActivityDO byNameDO = this.getLotteryActivityByName(activityBasicRequest.getActivityName());
            if (Objects.nonNull(byNameDO) && byNameDO.getId().compareTo(lotteryActivityDO.getId()) != 0) {
                throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NAME_EXIST);
            }
            if (lotteryActivityDO.getStatus().equals(LotteryActivityStatusEnum.DRAFT.getCode())) {
                if (lotteryActivityDO.getActivityName().equals(activityBasicRequest.getActivityName())) {
                    throw new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NAME_EXIST);
                }
            }

            // 更新活动信息
            LotteryActivityDO activityDO = PojoUtils.map(activityBasicRequest, LotteryActivityDO.class);
            activityDO.setStatus(LotteryActivityStatusEnum.ENABLED.getCode());
            this.updateById(activityDO);
            return PojoUtils.map(activityDO, LotteryActivityDTO.class);
        }
    }

    private LotteryActivityDO getLotteryActivityByName(String activityName) {
        LambdaQueryWrapper<LotteryActivityDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityDO::getActivityName, activityName);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveActivitySetting(SaveLotteryActivitySettingRequest activitySettingRequest) {
        // 抽奖活动ID
        Long lotteryActivityId = activitySettingRequest.getLotteryActivityId();
        Long opUserId = activitySettingRequest.getOpUserId();
        // 获取抽奖活动基本信息
        LotteryActivityDO lotteryActivityDO = Optional.ofNullable(this.getById(lotteryActivityId)).orElseThrow(() -> new BusinessException(LotteryActivityErrorCode.LOTTERY_ACTIVITY_NOT_EXIST));
        // 更新预算金额、背景图
        LotteryActivityDO activityDO = new LotteryActivityDO();
        activityDO.setId(lotteryActivityDO.getId());
        activityDO.setBudgetAmount(activitySettingRequest.getBudgetAmount());
        activityDO.setBgPicture(activitySettingRequest.getBgPicture());
        activityDO.setStatus(LotteryActivityStatusEnum.ENABLED.getCode());
        this.updateById(activityDO);

        SaveLotteryActivityGiveScopeRequest activityGiveScope = activitySettingRequest.getActivityGiveScope();
        SaveLotteryActivityJoinRuleRequest joinRuleRequest = activitySettingRequest.getActivityJoinRule();
        if (Objects.nonNull(activityGiveScope) && LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
            // 赠送范围
            LotteryActivityGiveScopeDTO giveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(lotteryActivityId);
            LotteryActivityGiveScopeDO giveScopeDO = PojoUtils.map(activityGiveScope, LotteryActivityGiveScopeDO.class);
            giveScopeDO.setOpUserId(opUserId);
            if (Objects.isNull(giveScopeDTO)) {
                giveScopeDO.setLotteryActivityId(lotteryActivityId);
                lotteryActivityGiveScopeService.save(giveScopeDO);
            } else {
                LambdaQueryWrapper<LotteryActivityGiveScopeDO> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(LotteryActivityGiveScopeDO::getLotteryActivityId, lotteryActivityId);
                lotteryActivityGiveScopeService.update(giveScopeDO, wrapper);

                // 如果存在C端设置信息，则删除
                lotteryActivityJoinRuleService.deleteByLotteryActivityId(lotteryActivityId, opUserId);
            }

            // 如果赠送范围为：指定范围客户 - 指定企业类型
            if (LotteryActivityGiveScopeEnum.getByCode(activityGiveScope.getGiveScope()) == LotteryActivityGiveScopeEnum.ASSIGN_SCOPE_CUSTOMER) {
                if (LotteryActivityGiveEnterpriseTypeEnum.ASSIGN_TYPE == LotteryActivityGiveEnterpriseTypeEnum.getByCode(activityGiveScope.getGiveEnterpriseType())) {
                    List<Integer> enterpriseTypeList = activityGiveScope.getEnterpriseTypeList();
                    if (CollUtil.isEmpty(enterpriseTypeList)) {
                        throw new BusinessException(LotteryActivityErrorCode.ASSIGN_ENTERPRISE_TYPE_NOT_NULL);
                    }
                    lotteryActivityGiveEnterpriseTypeService.updateGiveEnterpriseTypeByLotteryActivityId(lotteryActivityId, enterpriseTypeList, opUserId);
                }
            }

        } else if (Objects.nonNull(joinRuleRequest) && LotteryActivityPlatformEnum.getByCode(lotteryActivityDO.getPlatform()) != LotteryActivityPlatformEnum.B2B) {
            // C端规则
            LotteryActivityJoinRuleDTO joinRuleDTO = lotteryActivityJoinRuleService.getByLotteryActivityId(lotteryActivityId);
            if (Objects.isNull(joinRuleDTO)) {
                LotteryActivityJoinRuleDO activityJoinRuleDO = PojoUtils.map(joinRuleRequest, LotteryActivityJoinRuleDO.class);
                activityJoinRuleDO.setLotteryActivityId(lotteryActivityId);
                activityJoinRuleDO.setOpUserId(opUserId);
                lotteryActivityJoinRuleService.save(activityJoinRuleDO);

            } else {
                // 如果为C端规则，则更新C端规则信息
                LotteryActivityJoinRuleDO activityJoinRuleDO = new LotteryActivityJoinRuleDO();
                activityJoinRuleDO.setId(joinRuleDTO.getId());
                activityJoinRuleDO.setLotteryActivityId(lotteryActivityId);
                activityJoinRuleDO.setJoinStage(Objects.nonNull(joinRuleRequest.getJoinStage()) ? joinRuleRequest.getJoinStage() : 0);
                activityJoinRuleDO.setEveryGive(Objects.nonNull(joinRuleRequest.getEveryGive()) ? joinRuleRequest.getEveryGive() : 0);
                activityJoinRuleDO.setSignGive(Objects.nonNull(joinRuleRequest.getSignGive()) ? joinRuleRequest.getSignGive() : 0);
                activityJoinRuleDO.setInviteGive(Objects.nonNull(joinRuleRequest.getInviteGive()) ? joinRuleRequest.getInviteGive() : 0);
                activityJoinRuleDO.setOpUserId(opUserId);
                lotteryActivityJoinRuleService.updateById(activityJoinRuleDO);

                // 如果为C端规则，则更新C端规则信息，同时删除B端若存在的数据
                LotteryActivityGiveScopeDTO activityGiveScopeDTO = lotteryActivityGiveScopeService.getByLotteryActivityId(lotteryActivityId);
                if (Objects.nonNull(activityGiveScopeDTO)) {
                    lotteryActivityGiveScopeService.deleteByLotteryActivityId(lotteryActivityId, opUserId);
                    if (activityGiveScopeDTO.getGiveScope().equals(LotteryActivityGiveScopeEnum.ASSIGN_CUSTOMER.getCode())) {
                        // 删除赠送指定客户表数据
                        lotteryActivityGiveEnterpriseService.updateGiveEnterpriseByLotteryActivityId(lotteryActivityId, ListUtil.toList(), opUserId);

                    } else if (activityGiveScopeDTO.getGiveScope().equals(LotteryActivityGiveScopeEnum.ASSIGN_SCOPE_CUSTOMER.getCode())) {
                        if (LotteryActivityGiveEnterpriseTypeEnum.ASSIGN_TYPE == LotteryActivityGiveEnterpriseTypeEnum.getByCode(activityGiveScopeDTO.getGiveEnterpriseType())) {
                            // 删除赠送范围企业类型表数据
                            lotteryActivityGiveEnterpriseTypeService.updateGiveEnterpriseTypeByLotteryActivityId(lotteryActivityId, ListUtil.toList(), opUserId);
                        }

                        if (LotteryActivityGiveUserTypeEnum.ASSIGN_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(activityGiveScopeDTO.getGiveUserType())) {
                            // 删除用户类型-指定方案会员
                            lotteryActivityGiveMemberService.updateGiveMemberByLotteryActivityId(lotteryActivityId, ListUtil.toList(), opUserId);

                        } else if (LotteryActivityGiveUserTypeEnum.ASSIGN_PROMOTER_MEMBER == LotteryActivityGiveUserTypeEnum.getByCode(activityGiveScopeDTO.getGiveUserType())) {
                            // 删除用户类型-指定推广方会员
                            lotteryActivityGivePromoterService.updateGivePromoterByLotteryActivityId(lotteryActivityId, ListUtil.toList(), opUserId);
                        }
                    }
                }
            }

        }

        // 保存奖品设置和活动规则说明
        this.saveRewardSetting(activitySettingRequest, lotteryActivityId, opUserId);
        log.info("保存抽奖活动设置信息完成 抽奖活动ID={}", lotteryActivityId);

        return true;
    }

    @Override
    public List<String> getNameList(Integer limit) {
        LambdaQueryWrapper<LotteryActivityDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(LotteryActivityDO::getEndTime, new Date());
        wrapper.eq(LotteryActivityDO::getStatus, LotteryActivityStatusEnum.ENABLED.getCode());
        wrapper.last("limit " + limit);
        return this.list(wrapper).stream().map(LotteryActivityDO::getActivityName).collect(Collectors.toList());
    }

    /**
     * 保存奖品设置和活动规则说明
     *
     * @param activitySettingRequest 抽奖活动设置信息请求对象
     * @param lotteryActivityId 抽奖活动ID
     * @param opUserId 操作人
     */
    private void saveRewardSetting(SaveLotteryActivitySettingRequest activitySettingRequest, Long lotteryActivityId, Long opUserId) {
        // 校验最多8个奖项和中奖概率之和
        List<AddLotteryActivityRewardSettingRequest> activityRewardSettingList = activitySettingRequest.getActivityRewardSettingList();
        if (activityRewardSettingList.size() > 8) {
            throw new BusinessException(LotteryActivityErrorCode.REWARD_NOT_MORE_THAN);
        }
        BigDecimal sumHit = activityRewardSettingList.stream().map(AddLotteryActivityRewardSettingRequest::getHitProbability).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sumHit.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BusinessException(LotteryActivityErrorCode.HIT_PROBABILITY_SUM_ERROR);
        }
        // 奖品设置校验
        activityRewardSettingList.forEach(addRewardSettingRequest -> {
            // 每天最大抽中数量列的值，必须是中奖数量值的整数倍
            if (addRewardSettingRequest.getEveryMaxNumber() > 0 && addRewardSettingRequest.getRewardNumber() > 0) {
                int remainder = addRewardSettingRequest.getEveryMaxNumber() % addRewardSettingRequest.getRewardNumber();
                if (remainder != 0) {
                    throw new BusinessException(LotteryActivityErrorCode.EVERY_MAX_NUMBER_MUST_INTEGER);
                }
            }
        });
        // 校验奖品必须包含空奖
        List<AddLotteryActivityRewardSettingRequest> settingRequests = activityRewardSettingList.stream().filter(addRewardSettingRequest -> addRewardSettingRequest.getRewardType().equals(LotteryActivityRewardTypeEnum.EMPTY.getCode())).collect(Collectors.toList());
        if (CollUtil.isEmpty(settingRequests)) {
            throw new BusinessException(LotteryActivityErrorCode.REWARD_SETTING_MUST_EMPTY);
        }

        // 奖品设置
        List<LotteryActivityRewardSettingDTO> rewardSettingDTOS = lotteryActivityRewardSettingService.getByLotteryActivityId(lotteryActivityId);
        if (CollUtil.isNotEmpty(rewardSettingDTOS)) {
            LotteryActivityRewardSettingDO rewardSettingDO = new LotteryActivityRewardSettingDO();
            rewardSettingDO.setOpUserId(opUserId);
            LambdaQueryWrapper<LotteryActivityRewardSettingDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(LotteryActivityRewardSettingDO::getLotteryActivityId, lotteryActivityId);
            lotteryActivityRewardSettingService.batchDeleteWithFill(rewardSettingDO, wrapper);
        }
        List<LotteryActivityRewardSettingDO> rewardSettingDOList = PojoUtils.map(activityRewardSettingList, LotteryActivityRewardSettingDO.class);
        rewardSettingDOList.forEach(rewardSettingDO -> {
            rewardSettingDO.setLotteryActivityId(lotteryActivityId);
            rewardSettingDO.setOpUserId(opUserId);
            // 真实物品和虚拟物品，需要查询到奖品图片存入
            if (LotteryActivityRewardTypeEnum.getByCode(rewardSettingDO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS || LotteryActivityRewardTypeEnum.getByCode(rewardSettingDO.getRewardType()) == LotteryActivityRewardTypeEnum.VIRTUAL_GOODS) {
                GoodsGiftDO goodsGiftDO = goodsGiftService.getById(rewardSettingDO.getRewardId());
                rewardSettingDO.setRewardImg(goodsGiftDO.getPictureUrl());
            }
        });
        lotteryActivityRewardSettingService.saveBatch(rewardSettingDOList);

        // 活动规则说明
        LotteryActivityRuleInstructionDTO ruleInstructionDTO = lotteryActivityRuleInstructionService.getByLotteryActivityId(lotteryActivityId);
        if (Objects.isNull(ruleInstructionDTO)) {
            LotteryActivityRuleInstructionDO ruleInstructionDO = new LotteryActivityRuleInstructionDO();
            ruleInstructionDO.setLotteryActivityId(lotteryActivityId);
            ruleInstructionDO.setContent(activitySettingRequest.getContent());
            ruleInstructionDO.setOpUserId(opUserId);
            lotteryActivityRuleInstructionService.save(ruleInstructionDO);
        } else {
            LotteryActivityRuleInstructionDO rulesDO = new LotteryActivityRuleInstructionDO();
            rulesDO.setId(ruleInstructionDTO.getId());
            rulesDO.setContent(activitySettingRequest.getContent());
            rulesDO.setOpUserId(opUserId);
            lotteryActivityRuleInstructionService.updateById(rulesDO);
        }

    }


}
