package com.yiling.hmc.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.dao.InsuranceRecordPayPlanMapper;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayPlanDTO;
import com.yiling.hmc.wechat.dto.request.InsuranceJoinNotifyContext;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayPlanRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayPlanDO;
import com.yiling.hmc.wechat.enums.PayPlanPayStatusEnum;
import com.yiling.hmc.wechat.service.InsuranceRecordPayPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * C端参保缴费计划表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-30
 */
@Service
@Slf4j
public class InsuranceRecordPayPlanServiceImpl extends BaseServiceImpl<InsuranceRecordPayPlanMapper, InsuranceRecordPayPlanDO> implements InsuranceRecordPayPlanService {

    @Override
    public Boolean saveInsuranceRecordPayPlan(List<SaveInsuranceRecordPayPlanRequest> request) {
        if (CollUtil.isEmpty(request)) {
            log.info("参数为空");
            return Boolean.FALSE;
        }
        List<InsuranceRecordPayPlanDO> list = PojoUtils.map(request, InsuranceRecordPayPlanDO.class);
        return this.saveBatch(list);
    }

    @Override
    public List<InsuranceRecordPayPlanDTO> getByPolicyNo(String policyNo) {
        QueryWrapper<InsuranceRecordPayPlanDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordPayPlanDO::getPolicyNo, policyNo);
        List<InsuranceRecordPayPlanDO> list = this.list(wrapper);
        return PojoUtils.map(list, InsuranceRecordPayPlanDTO.class);
    }

    @Override
    public Boolean hasPayPlan(Long insuranceRecordId) {
        QueryWrapper<InsuranceRecordPayPlanDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordPayPlanDO::getInsuranceRecordId, insuranceRecordId);
        wrapper.lambda().eq(InsuranceRecordPayPlanDO::getPayStatus, PayPlanPayStatusEnum.WAIT.getType());
        InsuranceRecordPayPlanDO one = this.getOne(wrapper, Boolean.FALSE);
        if (Objects.nonNull(one)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<InsuranceRecordPayPlanDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        QueryWrapper<InsuranceRecordPayPlanDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordPayPlanDO::getInsuranceRecordId, insuranceRecordId);
        List<InsuranceRecordPayPlanDO> list = this.list(wrapper);
        return PojoUtils.map(list, InsuranceRecordPayPlanDTO.class);
    }
}
