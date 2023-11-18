package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.integral.bo.IntegralGiveRuleDetailBO;
import com.yiling.user.integral.bo.IntegralOrderGiveConfigBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.dto.GenerateMultipleConfigDTO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.IntegralOrderGiveConfigDTO;
import com.yiling.user.integral.dto.IntegralPeriodConfigDTO;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.SaveIntegralMultipleConfigRequest;
import com.yiling.user.integral.dto.request.SaveIntegralPeriodConfigRequest;
import com.yiling.user.integral.dto.request.SaveOrderGiveIntegralRequest;
import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.dao.IntegralGiveRuleMapper;
import com.yiling.user.integral.entity.IntegralSignPeriodDO;
import com.yiling.user.integral.enums.IntegralCustomerScopeEnum;
import com.yiling.user.integral.enums.IntegralGiveRuleProgressEnum;
import com.yiling.user.integral.enums.IntegralGoodsScopeEnum;
import com.yiling.user.integral.enums.IntegralMerchantScopeEnum;
import com.yiling.user.integral.enums.IntegralBehaviorEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.integral.enums.IntegralUserTypeEnum;
import com.yiling.user.integral.service.IntegralBehaviorService;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralOrderEnterpriseGoodsService;
import com.yiling.user.integral.service.IntegralOrderGiveConfigService;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseService;
import com.yiling.user.integral.service.IntegralOrderGiveEnterpriseTypeService;
import com.yiling.user.integral.service.IntegralOrderGiveMemberService;
import com.yiling.user.integral.service.IntegralOrderGiveMultipleConfigService;
import com.yiling.user.integral.service.IntegralOrderGivePaymentMethodService;
import com.yiling.user.integral.service.IntegralOrderPlatformGoodsService;
import com.yiling.user.integral.service.IntegralOrderSellerService;
import com.yiling.user.integral.service.IntegralPeriodConfigService;
import com.yiling.user.integral.service.IntegralSignPeriodService;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分发放规则表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralGiveRuleServiceImpl extends BaseServiceImpl<IntegralGiveRuleMapper, IntegralGiveRuleDO> implements IntegralGiveRuleService {

    @Autowired
    UserService userService;
    @Autowired
    IntegralBehaviorService integralBehaviorService;
    @Autowired
    IntegralSignPeriodService integralSignPeriodService;
    @Autowired
    IntegralPeriodConfigService integralPeriodConfigService;
    @Autowired
    IntegralOrderGiveConfigService integralOrderGiveConfigService;
    @Autowired
    IntegralOrderGiveMultipleConfigService integralOrderGiveMultipleConfigService;
    @Autowired
    IntegralOrderGiveEnterpriseTypeService integralOrderGiveEnterpriseTypeService;
    @Autowired
    IntegralOrderGivePaymentMethodService integralOrderGivePaymentMethodService;
    @Autowired
    IntegralOrderSellerService integralOrderSellerService;
    @Autowired
    IntegralOrderPlatformGoodsService integralOrderPlatformGoodsService;
    @Autowired
    IntegralOrderEnterpriseGoodsService integralOrderEnterpriseGoodsService;
    @Autowired
    IntegralOrderGiveEnterpriseService integralOrderGiveEnterpriseService;
    @Autowired
    IntegralOrderGiveMemberService integralOrderGiveMemberService;

    @Override
    public Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request) {
        Page<IntegralRuleItemBO> giveRuleBOPage = baseMapper.queryListPage(request.getPage(), request);
        if (CollUtil.isNotEmpty(giveRuleBOPage.getRecords())) {
            Date now = new Date();
            giveRuleBOPage.getRecords().forEach(integralGiveRuleBO -> {
                if (integralGiveRuleBO.getStartTime().after(now)) {
                    integralGiveRuleBO.setProgress(IntegralGiveRuleProgressEnum.UNDO.getCode());
                } else if (integralGiveRuleBO.getEndTime().before(now)) {
                    integralGiveRuleBO.setProgress(IntegralGiveRuleProgressEnum.END.getCode());
                } else {
                    integralGiveRuleBO.setProgress(IntegralGiveRuleProgressEnum.GOING.getCode());
                }
                // 停用的也设置为已结束
                if (integralGiveRuleBO.getStatus().equals(IntegralRuleStatusEnum.DISABLED.getCode())) {
                    integralGiveRuleBO.setProgress(IntegralGiveRuleProgressEnum.END.getCode());
                }

            });
        }
        return giveRuleBOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateRuleStatusRequest request) {
        IntegralGiveRuleDO giveRuleDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
        IntegralGiveRuleDO integralGiveRuleDO = new IntegralGiveRuleDO();
        integralGiveRuleDO.setId(giveRuleDO.getId());
        integralGiveRuleDO.setStatus(request.getStatus());
        integralGiveRuleDO.setOpUserId(request.getOpUserId());
        return this.updateById(integralGiveRuleDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntegralGiveRuleDTO saveBasic(SaveIntegralRuleBasicRequest request) {
        IntegralGiveRuleDO giveRuleDO = PojoUtils.map(request, IntegralGiveRuleDO.class);
        giveRuleDO.setStatus(EnableStatusEnum.ENABLED.getCode());
        // 校验生效时间不能在当前时间之前
        if (request.getStartTime().before(new Date())) {
            throw new BusinessException(UserErrorCode.INTEGRAL_RULE_START_TIME_ERROR);
        }

        Long id = request.getId();
        if (Objects.isNull(id) || id == 0) {
            // 名称不能重复
            IntegralGiveRuleDO giveRule = this.getGiveRuleByName(request.getName());
            if (Objects.nonNull(giveRule)) {
                throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
            }
            // 校验周期时间段只允许存在一个有效规则
            List<IntegralGiveRuleDTO> validGiveRuleList = this.getValidGiveRuleList();
            if (CollUtil.isNotEmpty(validGiveRuleList)) {
                IntegralBehaviorDTO behaviorDTO = integralBehaviorService.getByName(IntegralBehaviorEnum.SIGN_GIVE_INTEGRAL.getName());
                // 当前保存的规则为签到规则才进行校验
                if (request.getBehaviorId().compareTo(behaviorDTO.getId()) == 0) {
                    validGiveRuleList.forEach(integralGiveRuleDTO -> {
                        // 存在的有效规则为签到才校验时间重叠
                        if (integralGiveRuleDTO.getBehaviorId().compareTo(behaviorDTO.getId()) == 0) {
                            IntegralSignPeriodDO signPeriodDO = integralSignPeriodService.getIntegralSignPeriod(integralGiveRuleDTO.getId());
                            // 只校验签到规则
                            if (Objects.nonNull(signPeriodDO)) {
                                if (this.isOverlap(request.getStartTime(), request.getEndTime(), integralGiveRuleDTO.getStartTime(), integralGiveRuleDTO.getEndTime())) {
                                    throw new BusinessException(UserErrorCode.TIME_RANGE_NOT_MORE_SIGN_RULE);
                                }
                            }
                        }
                    });
                }
            }

            // 保存
            this.save(giveRuleDO);

        } else {
            IntegralGiveRuleDO integralGiveRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
            // 校验名称
            IntegralGiveRuleDO byNameDO = this.getGiveRuleByName(request.getName());
            if (Objects.nonNull(byNameDO) && byNameDO.getId().compareTo(integralGiveRuleDO.getId()) != 0) {
                throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
            }
            if (integralGiveRuleDO.getStatus().equals(IntegralRuleStatusEnum.DRAFT.getCode())) {
                if (integralGiveRuleDO.getName().equals(request.getName())) {
                    throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
                }
            }
            // 校验周期时间段只允许存在一个有效规则
            if (integralGiveRuleDO.getStartTime().compareTo(request.getStartTime()) != 0 || integralGiveRuleDO.getEndTime().compareTo(request.getEndTime()) != 0  ) {
                List<IntegralGiveRuleDTO> validGiveRuleList = this.getValidGiveRuleList();
                if (CollUtil.isNotEmpty(validGiveRuleList)) {
                    IntegralBehaviorDTO behaviorDTO = integralBehaviorService.getByName(IntegralBehaviorEnum.SIGN_GIVE_INTEGRAL.getName());
                    // 当前保存的规则为签到规则才进行校验
                    if (request.getBehaviorId().compareTo(behaviorDTO.getId()) == 0) {
                        // 当前发放规则的信息
                        validGiveRuleList.forEach(integralGiveRuleDTO -> {
                            // 签到才校验时间重叠
                            if (integralGiveRuleDTO.getBehaviorId().compareTo(behaviorDTO.getId()) == 0) {
                                IntegralSignPeriodDO signPeriodDO = integralSignPeriodService.getIntegralSignPeriod(integralGiveRuleDTO.getId());
                                // 只校验签到规则
                                if (Objects.nonNull(signPeriodDO) && signPeriodDO.getGiveRuleId().compareTo(request.getId()) != 0) {
                                    if (this.isOverlap(request.getStartTime(), request.getEndTime(), integralGiveRuleDTO.getStartTime(), integralGiveRuleDTO.getEndTime())) {
                                        throw new BusinessException(UserErrorCode.TIME_RANGE_NOT_MORE_SIGN_RULE);
                                    }
                                }
                            }
                        });
                    }
                }
            }


            // 更新
            this.updateById(giveRuleDO);

        }

        return PojoUtils.map(giveRuleDO, IntegralGiveRuleDTO.class);
    }

    /**
     * 两个时间段是否有重叠
     *
     * @param realStartTime 第一个时间段的开始时间
     * @param realEndTime 第一个时间段的结束时间
     * @param startTime 第二个时间段的开始时间
     * @param endTime 第二个时间段的结束时间
     * @return true 表示时间段有重合
     */
    public boolean isOverlap(Date realStartTime, Date realEndTime, Date startTime, Date endTime) {
        return startTime.before(realEndTime) && endTime.after(realStartTime);
    }

    /**
     * 根据名称获取积分发放规则
     *
     * @param name
     * @return
     */
    public IntegralGiveRuleDO getGiveRuleByName(String name) {
        LambdaQueryWrapper<IntegralGiveRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralGiveRuleDO::getName, name);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralGiveRuleDTO> getValidGiveRuleList() {
        LambdaQueryWrapper<IntegralGiveRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralGiveRuleDO::getStatus, IntegralRuleStatusEnum.ENABLED.getCode());
        wrapper.gt(IntegralGiveRuleDO::getEndTime, new Date());
        return PojoUtils.map(this.list(wrapper), IntegralGiveRuleDTO.class);
    }

    @Override
    public List<IntegralGiveRuleDTO> getDoingGiveRuleList() {
        LambdaQueryWrapper<IntegralGiveRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralGiveRuleDO::getStatus, IntegralRuleStatusEnum.ENABLED.getCode());
        wrapper.lt(IntegralGiveRuleDO::getStartTime, new Date());
        wrapper.gt(IntegralGiveRuleDO::getEndTime, new Date());
        return PojoUtils.map(this.list(wrapper), IntegralGiveRuleDTO.class);
    }

    @Override
    public IntegralGiveRuleDetailBO get(Long id) {
        IntegralGiveRuleDO giveRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));

        // 基本信息
        IntegralGiveRuleDetailBO giveRuleDetailBO = PojoUtils.map(giveRuleDO, IntegralGiveRuleDetailBO.class);
        IntegralBehaviorDO behaviorDO = Optional.ofNullable(integralBehaviorService.getById(giveRuleDetailBO.getBehaviorId())).orElse(new IntegralBehaviorDO());
        giveRuleDetailBO.setBehaviorName(behaviorDO.getName());

        // 签到送积分
        if (IntegralBehaviorEnum.SIGN_GIVE_INTEGRAL == IntegralBehaviorEnum.getByName(behaviorDO.getName())) {
            Integer signPeriod = integralSignPeriodService.getSignPeriod(id);
            giveRuleDetailBO.setSignPeriod(signPeriod);
            List<IntegralPeriodConfigDTO> periodConfigList = integralPeriodConfigService.getIntegralPeriodConfigList(id);
            giveRuleDetailBO.setPeriodConfigList(periodConfigList);
        } else if (IntegralBehaviorEnum.ORDER_GIVE_INTEGRAL == IntegralBehaviorEnum.getByName(behaviorDO.getName())) {
            // 订单送积分
            IntegralOrderGiveConfigDTO orderGiveConfigDTO = integralOrderGiveConfigService.getByGiveRuleId(id);
            IntegralOrderGiveConfigBO orderGiveConfigBO = PojoUtils.map(orderGiveConfigDTO, IntegralOrderGiveConfigBO.class);
            if (Objects.isNull(orderGiveConfigBO)) {
                return giveRuleDetailBO;
            }

            // 获取企业类型
            List<Integer> enterpriseTypeList = integralOrderGiveEnterpriseTypeService.getEnterpriseTypeList(id);
            orderGiveConfigBO.setEnterpriseTypeList(enterpriseTypeList);
            // 获取支付方式
            List<Integer> paymentMethodList = integralOrderGivePaymentMethodService.getByGiveRuleId(id);
            orderGiveConfigBO.setPaymentMethodList(paymentMethodList);
            // 订单送积分倍数配置
            List<GenerateMultipleConfigDTO> multipleConfigDTOList = integralOrderGiveMultipleConfigService.getMultipleConfig(id);
            giveRuleDetailBO.setOrderGiveMultipleConfigList(multipleConfigDTOList);

            // 指定商家数量
            if (IntegralMerchantScopeEnum.getByCode(orderGiveConfigDTO.getMerchantScope()) == IntegralMerchantScopeEnum.ASSIGN) {
                Integer sellerNum = integralOrderSellerService.countSellerByGiveRuleId(id);
                orderGiveConfigBO.setSellerNum(sellerNum);
            }
            // 指定平台SKU数量/店铺SKU数量
            if (IntegralGoodsScopeEnum.getByCode(orderGiveConfigDTO.getGoodsScope()) == IntegralGoodsScopeEnum.PLATFORM_SKU) {
                Integer platformGoodsNum = integralOrderPlatformGoodsService.countPlatformGoodsByGiveRuleId(id);
                orderGiveConfigBO.setPlatformGoodsNum(platformGoodsNum);
            } else if (IntegralGoodsScopeEnum.getByCode(orderGiveConfigDTO.getGoodsScope()) == IntegralGoodsScopeEnum.SHOP_SKU) {
                Integer enterpriseGoodsNum = integralOrderEnterpriseGoodsService.countEnterpriseGoodsByRuleId(id);
                orderGiveConfigBO.setEnterpriseGoodsNum(enterpriseGoodsNum);
            }
            // 指定客户数量
            if (IntegralCustomerScopeEnum.getByCode(orderGiveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN) {
                Integer customerNum = integralOrderGiveEnterpriseService.countGiveEnterpriseByRuleId(id);
                orderGiveConfigBO.setCustomerNum(customerNum);
            } else if (IntegralCustomerScopeEnum.getByCode(orderGiveConfigDTO.getCustomerScope()) == IntegralCustomerScopeEnum.ASSIGN_SCOPE) {
                // 用户类型：部分会员
                if (IntegralUserTypeEnum.getByCode(orderGiveConfigDTO.getUserType()) == IntegralUserTypeEnum.SOME_MEMBER) {
                    Integer memberNum = integralOrderGiveMemberService.countMemberByRuleId(id);
                    orderGiveConfigBO.setMemberNum(memberNum);
                }
            }

            giveRuleDetailBO.setOrderGiveConfig(orderGiveConfigBO);

        }

        return giveRuleDetailBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copy(Long id, Long opUserId) {
        // 基础信息
        IntegralGiveRuleDO giveRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
        giveRuleDO.setId(null);
        giveRuleDO.setOpUserId(opUserId);
        giveRuleDO.setStatus(IntegralRuleStatusEnum.DRAFT.getCode());
        giveRuleDO.setStartTime(null);
        giveRuleDO.setEndTime(null);
        this.save(giveRuleDO);

        Long newId = giveRuleDO.getId();
        IntegralBehaviorDO behaviorDO = integralBehaviorService.getById(giveRuleDO.getBehaviorId());
        // 签到送积分规则
        if (IntegralBehaviorEnum.SIGN_GIVE_INTEGRAL == IntegralBehaviorEnum.getByName(behaviorDO.getName())) {
            Integer signPeriod = integralSignPeriodService.getSignPeriod(id);
            if (Objects.nonNull(signPeriod) && signPeriod != 0) {
                List<IntegralPeriodConfigDTO> periodConfigList = integralPeriodConfigService.getIntegralPeriodConfigList(id);
                periodConfigList.forEach(integralPeriodConfigDTO -> integralPeriodConfigDTO.setGiveRuleId(newId));

                SaveSignPeriodRequest periodRequest = new SaveSignPeriodRequest();
                periodRequest.setGiveRuleId(newId);
                periodRequest.setSignPeriod(signPeriod);
                periodRequest.setIntegralPeriodConfigList(PojoUtils.map(periodConfigList, SaveIntegralPeriodConfigRequest.class));
                integralSignPeriodService.saveSignPeriod(periodRequest);
            }

        }
        log.info("积分发放规则复制操作 复制的发放规则ID={} 新生成的发放规则ID={}", id, giveRuleDO.getId());

        return newId;
    }

    @Override
    public List<IntegralGiveRuleDTO> getCurrentValidRule(Integer platform) {
        Date now = new Date();
        LambdaQueryWrapper<IntegralGiveRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralGiveRuleDO::getPlatform, platform);
        wrapper.eq(IntegralGiveRuleDO::getStatus, IntegralRuleStatusEnum.ENABLED.getCode());
        wrapper.lt(IntegralGiveRuleDO::getStartTime, now);
        wrapper.gt(IntegralGiveRuleDO::getEndTime, now);
        List<IntegralGiveRuleDO> giveRuleDOList = this.list(wrapper);
        return PojoUtils.map(giveRuleDOList, IntegralGiveRuleDTO.class);
    }

}
