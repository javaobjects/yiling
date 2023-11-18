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
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.enterprise.service.EnterpriseService;
import com.yiling.user.integral.bo.IntegralLotteryConfigBO;
import com.yiling.user.integral.bo.IntegralRuleItemBO;
import com.yiling.user.integral.bo.IntegralUseRuleDetailBO;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;
import com.yiling.user.integral.dto.IntegralGiveUseRecordDTO;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;
import com.yiling.user.integral.dto.IntegralUseRuleDTO;
import com.yiling.user.integral.dto.request.AddIntegralRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralGiveUseRecordRequest;
import com.yiling.user.integral.dto.request.QueryIntegralRulePageRequest;
import com.yiling.user.integral.dto.request.SaveIntegralRuleBasicRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.dto.request.UpdateIntegralExchangeLotteryRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralBehaviorDO;
import com.yiling.user.integral.entity.IntegralLotteryConfigDO;
import com.yiling.user.integral.entity.IntegralUseRuleDO;
import com.yiling.user.integral.dao.IntegralUseRuleMapper;
import com.yiling.user.integral.enums.IntegralBehaviorEnum;
import com.yiling.user.integral.enums.IntegralGiveRuleProgressEnum;
import com.yiling.user.integral.enums.IntegralRulePlatformEnum;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.integral.enums.UserIntegralChangeTypeEnum;
import com.yiling.user.integral.service.IntegralBehaviorService;
import com.yiling.user.integral.service.IntegralGiveUseRecordService;
import com.yiling.user.integral.service.IntegralLotteryConfigService;
import com.yiling.user.integral.service.IntegralUseRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.UserIntegralService;
import com.yiling.user.system.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分消耗规则表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Slf4j
@Service
public class IntegralUseRuleServiceImpl extends BaseServiceImpl<IntegralUseRuleMapper, IntegralUseRuleDO> implements IntegralUseRuleService {

    @Autowired
    UserService userService;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    IntegralBehaviorService integralBehaviorService;
    @Autowired
    IntegralLotteryConfigService integralLotteryConfigService;
    @Autowired
    UserIntegralService userIntegralService;
    @Autowired
    IntegralGiveUseRecordService integralGiveUseRecordService;

    @Override
    public Page<IntegralRuleItemBO> queryListPage(QueryIntegralRulePageRequest request) {
        Page<IntegralRuleItemBO> useRuleBOPage = baseMapper.queryListPage(request.getPage(), request);
        if (CollUtil.isNotEmpty(useRuleBOPage.getRecords())) {
            Date now = new Date();
            useRuleBOPage.getRecords().forEach(integralUseRuleBO -> {
                if (integralUseRuleBO.getStartTime().after(now)) {
                    integralUseRuleBO.setProgress(IntegralGiveRuleProgressEnum.UNDO.getCode());
                } else if (integralUseRuleBO.getEndTime().before(now)) {
                    integralUseRuleBO.setProgress(IntegralGiveRuleProgressEnum.END.getCode());
                } else {
                    integralUseRuleBO.setProgress(IntegralGiveRuleProgressEnum.GOING.getCode());
                }
                // 停用的也设置为已结束
                if (integralUseRuleBO.getStatus().equals(IntegralRuleStatusEnum.DISABLED.getCode())) {
                    integralUseRuleBO.setProgress(IntegralGiveRuleProgressEnum.END.getCode());
                }

            });
        }
        return useRuleBOPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(UpdateRuleStatusRequest request) {
        IntegralUseRuleDO useRuleDO = Optional.ofNullable(this.getById(request.getId())).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
        IntegralUseRuleDO integralUseRuleDO = new IntegralUseRuleDO();
        integralUseRuleDO.setId(useRuleDO.getId());
        integralUseRuleDO.setStatus(request.getStatus());
        integralUseRuleDO.setOpUserId(request.getOpUserId());
        return this.updateById(integralUseRuleDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public IntegralUseRuleDTO saveBasic(SaveIntegralRuleBasicRequest request) {
        IntegralUseRuleDO useRuleDO = PojoUtils.map(request, IntegralUseRuleDO.class);
        useRuleDO.setStatus(EnableStatusEnum.ENABLED.getCode());
        // 校验生效时间不能在当前时间之前
        if (request.getStartTime().before(new Date())) {
            throw new BusinessException(UserErrorCode.INTEGRAL_RULE_START_TIME_ERROR);
        }

        Long id = request.getId();
        if (Objects.isNull(id) || id == 0) {
            // 名称不能重复
            IntegralUseRuleDO giveRule = this.getUseRuleByName(request.getName());
            if (Objects.nonNull(giveRule)) {
                throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
            }
            // 保存
            this.save(useRuleDO);

        } else {
            IntegralUseRuleDO integralUseRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
            // 校验名称
            IntegralUseRuleDO byNameDO = this.getUseRuleByName(request.getName());
            if (Objects.nonNull(byNameDO) && byNameDO.getId().compareTo(integralUseRuleDO.getId()) != 0) {
                throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
            }
            if (integralUseRuleDO.getStatus().equals(IntegralRuleStatusEnum.DRAFT.getCode())) {
                if (integralUseRuleDO.getName().equals(request.getName())) {
                    throw new BusinessException(UserErrorCode.INTEGRAL_RULE_NAME_EXIST);
                }
            }
            // 更新
            this.updateById(useRuleDO);

        }

        return PojoUtils.map(useRuleDO, IntegralUseRuleDTO.class);
    }

    /**
     * 根据名称获取积分发放规则
     *
     * @param name
     * @return
     */
    public IntegralUseRuleDO getUseRuleByName(String name) {
        LambdaQueryWrapper<IntegralUseRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralUseRuleDO::getName, name);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

    @Override
    public List<IntegralUseRuleDTO> getValidUseRuleList() {
        LambdaQueryWrapper<IntegralUseRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralUseRuleDO::getStatus, IntegralRuleStatusEnum.ENABLED.getCode());
        wrapper.gt(IntegralUseRuleDO::getEndTime, new Date());
        return PojoUtils.map(this.list(wrapper), IntegralUseRuleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long copy(Long id, Long opUserId) {
        // 基础信息
        IntegralUseRuleDO useRuleDO = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BusinessException(UserErrorCode.INTEGRAL_RULE_NOT_EXIST));
        useRuleDO.setId(null);
        useRuleDO.setStatus(IntegralRuleStatusEnum.DRAFT.getCode());
        useRuleDO.setStartTime(null);
        useRuleDO.setEndTime(null);
        useRuleDO.setOpUserId(opUserId);
        this.save(useRuleDO);

        Long newId = useRuleDO.getId();
        log.info("积分消耗规则复制操作 复制的发放规则ID={} 新生成的发放规则ID={}", id, useRuleDO.getId());

        return newId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchangeLotteryTimes(UpdateIntegralExchangeLotteryRequest request) {
        IntegralLotteryConfigDTO lotteryConfigDTO = Optional.ofNullable(integralLotteryConfigService.getRuleByLotteryActivityId(request.getLotteryActivityId()))
                .orElseThrow(() -> new BusinessException(UserErrorCode.LOTTERY_ACTIVITY_NOT_UNION_RULE));

        Long useRuleId = lotteryConfigDTO.getUseRuleId();
        IntegralUseRuleDO useRuleDO = this.getById(useRuleId);
        // 获取用户积分值
        Integer integralValue = userIntegralService.getUserIntegralByUid(request.getUid(), request.getPlatform());
        // 需要消耗的积分值
        int useIntegralValue = lotteryConfigDTO.getUseIntegralValue() * request.getExchangeTimes();
        // 积分不足
        if (useIntegralValue > integralValue) {
            throw new BusinessException(UserErrorCode.INTEGRAL_LESS);
        }
        // 积分参与抽奖次数已达上限
        QueryIntegralGiveUseRecordRequest recordRequest = new QueryIntegralGiveUseRecordRequest();
        recordRequest.setRuleId(useRuleId);
        recordRequest.setPlatform(request.getPlatform());
        recordRequest.setUid(request.getUid());
        recordRequest.setChangeType(UserIntegralChangeTypeEnum.JOIN_ACTIVITY_USE.getCode());
        List<IntegralGiveUseRecordDTO> recordDTOList = integralGiveUseRecordService.queryList(recordRequest);
        if (lotteryConfigDTO.getUseSumTimes() > 0 && recordDTOList.size() >= lotteryConfigDTO.getUseSumTimes()) {
            throw new BusinessException(UserErrorCode.INTEGRAL_JOIN_HAVE_TOP);
        }
        // 当日积分参与抽奖次数已达上限
        recordRequest.setStartOperTime(DateUtil.beginOfDay(new Date()));
        recordRequest.setEndOperTime(DateUtil.endOfDay(new Date()));
        List<IntegralGiveUseRecordDTO> giveUseRecordDTOList = integralGiveUseRecordService.queryList(recordRequest);
        if (lotteryConfigDTO.getEveryDayTimes() > 0 && giveUseRecordDTOList.size() >= lotteryConfigDTO.getEveryDayTimes()) {
            throw new BusinessException(UserErrorCode.TODAY_INTEGRAL_JOIN_HAVE_TOP);
        }

        // 增加积分和变更日志
        UpdateIUserIntegralRequest integralRequest = new UpdateIUserIntegralRequest();
        integralRequest.setOpUserId(request.getOpUserId());
        integralRequest.setPlatform(request.getPlatform());
        integralRequest.setUid(request.getUid());
        integralRequest.setIntegralValue(useIntegralValue);
        integralRequest.setChangeType(UserIntegralChangeTypeEnum.JOIN_ACTIVITY_USE.getCode());
        userIntegralService.updateIntegral(integralRequest);

        // 增加积分扣减记录
        AddIntegralRecordRequest integralRecordRequest = new AddIntegralRecordRequest();
        integralRecordRequest.setRuleId(useRuleId);
        integralRecordRequest.setRuleName(useRuleDO.getName());
        integralRecordRequest.setOpUserId(request.getOpUserId());
        integralRecordRequest.setPlatform(request.getPlatform());
        integralRecordRequest.setUid(request.getUid());
        if (request.getPlatform().equals(IntegralRulePlatformEnum.B2B.getCode())) {
            integralRecordRequest.setUname(Optional.ofNullable(enterpriseService.getById(request.getUid())).orElse(new EnterpriseDO()).getName());
        }
        integralRecordRequest.setChangeType(UserIntegralChangeTypeEnum.JOIN_ACTIVITY_USE.getCode());
        integralRecordRequest.setIntegralValue(useIntegralValue);
        integralRecordRequest.setOpRemark(request.getLotteryActivityId().toString());
        integralRecordRequest.setBehaviorId(useRuleDO.getBehaviorId());
        integralRecordRequest.setBehaviorName(IntegralBehaviorEnum.JOIN_ACTIVITY_USE.getName());
        integralGiveUseRecordService.addRecord(integralRecordRequest);
        log.info("抽奖活动兑换积分完成 兑换前用户的积分数量为={} 抽奖活动ID={} 平台={} UID={} 兑换抽奖次数={}", integralValue, request.getLotteryActivityId(), request.getPlatform(), request.getUid(), request.getExchangeTimes());

        return true;
    }

}
