package com.yiling.user.integral.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.integral.dto.IntegralGiveRuleDTO;
import com.yiling.user.integral.dto.request.SaveIntegralPeriodConfigRequest;
import com.yiling.user.integral.dto.request.SaveSignPeriodRequest;
import com.yiling.user.integral.dto.request.UpdateRuleStatusRequest;
import com.yiling.user.integral.entity.IntegralGiveRuleDO;
import com.yiling.user.integral.entity.IntegralPeriodConfigDO;
import com.yiling.user.integral.entity.IntegralSignPeriodDO;
import com.yiling.user.integral.dao.IntegralSignPeriodMapper;
import com.yiling.user.integral.enums.IntegralRuleStatusEnum;
import com.yiling.user.integral.service.IntegralGiveRuleService;
import com.yiling.user.integral.service.IntegralPeriodConfigService;
import com.yiling.user.integral.service.IntegralSignPeriodService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分签到周期表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralSignPeriodServiceImpl extends BaseServiceImpl<IntegralSignPeriodMapper, IntegralSignPeriodDO> implements IntegralSignPeriodService {

    @Autowired
    IntegralGiveRuleService integralGiveRuleService;
    @Autowired
    IntegralPeriodConfigService integralPeriodConfigService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSignPeriod(SaveSignPeriodRequest request) {
        // 校验规则周期和天数配置相匹配
        if (request.getIntegralPeriodConfigList().size() != request.getSignPeriod()) {
            throw new BusinessException(UserErrorCode.SIGN_PERIOD_DAYS_ERROR);
        }

        // 保存签到周期
        IntegralSignPeriodDO signPeriodDO = this.getIntegralSignPeriod(request.getGiveRuleId());

        IntegralSignPeriodDO periodDO = PojoUtils.map(request, IntegralSignPeriodDO.class);
        if (Objects.isNull(signPeriodDO)) {
            this.save(periodDO);
        } else {
            periodDO.setId(signPeriodDO.getId());
            this.updateById(periodDO);
        }

        // 保存签到周期配置
        List<SaveIntegralPeriodConfigRequest> periodConfigList = request.getIntegralPeriodConfigList();
        periodConfigList.forEach(configRequest -> {
            configRequest.setGiveRuleId(request.getGiveRuleId());
            configRequest.setOpUserId(request.getOpUserId());
        });

        IntegralPeriodConfigDO configDO = new IntegralPeriodConfigDO();
        configDO.setOpUserId(request.getOpUserId());
        LambdaQueryWrapper<IntegralPeriodConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralPeriodConfigDO::getGiveRuleId, request.getGiveRuleId());
        integralPeriodConfigService.batchDeleteWithFill(configDO, wrapper);

        List<IntegralPeriodConfigDO> periodConfigDOList = PojoUtils.map(periodConfigList, IntegralPeriodConfigDO.class);
        integralPeriodConfigService.saveBatch(periodConfigDOList);
        log.info("保存积分签到周期={}天 天数配置信息={}", request.getSignPeriod(), JSONObject.toJSONString(periodConfigDOList));

        return true;
    }

    @Override
    public Integer getSignPeriod(Long giveRuleId) {
        IntegralSignPeriodDO signPeriodDO = Optional.ofNullable(this.getIntegralSignPeriod(giveRuleId)).orElse(new IntegralSignPeriodDO());
        return signPeriodDO.getSignPeriod();
    }

    @Override
    public IntegralSignPeriodDO getIntegralSignPeriod(Long giveRuleId) {
        LambdaQueryWrapper<IntegralSignPeriodDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralSignPeriodDO::getGiveRuleId, giveRuleId);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }

}
