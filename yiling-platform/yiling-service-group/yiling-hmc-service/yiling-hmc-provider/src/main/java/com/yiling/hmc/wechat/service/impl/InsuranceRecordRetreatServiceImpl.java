package com.yiling.hmc.wechat.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.bo.InsuranceRecordRetreatBO;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;
import com.yiling.hmc.insurance.service.InsuranceService;
import com.yiling.hmc.wechat.dao.InsuranceRecordRetreatMapper;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRetreatRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordRetreatDO;
import com.yiling.hmc.wechat.service.InsuranceRecordRetreatService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;

import cn.hutool.core.util.IdcardUtil;

/**
 * <p>
 * 退保记录表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-06
 */
@Service
public class InsuranceRecordRetreatServiceImpl extends BaseServiceImpl<InsuranceRecordRetreatMapper, InsuranceRecordRetreatDO> implements InsuranceRecordRetreatService {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceCompanyService insuranceCompanyService;

    @Autowired
    private InsuranceRecordService insuranceRecordService;

    @DubboReference
    protected NoApi noApi;

    @Override
    public Long saveInsuranceRecordRetreat(SaveInsuranceRetreatRequest request) {
        InsuranceRecordRetreatDO retreatDO = PojoUtils.map(request, InsuranceRecordRetreatDO.class);
        retreatDO.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        this.save(retreatDO);
        return retreatDO.getId();
    }

    @Override
    public InsuranceRecordRetreatDTO getByInsuranceRecordId(Long insuranceRecordId) {
        QueryWrapper<InsuranceRecordRetreatDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceRecordRetreatDO::getInsuranceRecordId, insuranceRecordId);
        InsuranceRecordRetreatDO recordRetreatDO = this.getOne(wrapper);
        return PojoUtils.map(recordRetreatDO, InsuranceRecordRetreatDTO.class);
    }

    @Override
    public InsuranceRecordRetreatBO getRetreatDetail(Long insuranceRecordId) {
        LambdaQueryWrapper<InsuranceRecordRetreatDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(InsuranceRecordRetreatDO::getInsuranceRecordId,insuranceRecordId).last("limit 1");
        InsuranceRecordRetreatDO recordRetreatDO = this.getOne(wrapper);
        InsuranceRecordRetreatBO insuranceRecordRetreatBO = new InsuranceRecordRetreatBO();
        if(Objects.isNull(recordRetreatDO)){
            return insuranceRecordRetreatBO;
        }
        PojoUtils.map(recordRetreatDO,insuranceRecordRetreatBO);
        insuranceRecordRetreatBO.setRetMoney(BigDecimal.valueOf(recordRetreatDO.getRetMoney()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
        //查询退保人信息
        InsuranceRecordDTO insuranceRecordDO = insuranceRecordService.getById(recordRetreatDO.getInsuranceRecordId());
        if(Objects.nonNull(insuranceRecordDO)){
            insuranceRecordRetreatBO.setUserId(insuranceRecordDO.getUserId()).setHolderCredentialNo(IdcardUtil.hide(insuranceRecordDO.getHolderCredentialNo(),3,14))
                    .setHolderPhone(insuranceRecordDO.getHolderPhone()).setHolderName(insuranceRecordDO.getHolderName()).setComboName(insuranceRecordDO.getComboName());
            InsuranceDO insuranceDO = insuranceService.getById(insuranceRecordDO.getInsuranceId());
            if(Objects.nonNull(insuranceDO)){
                insuranceRecordRetreatBO.setInsuranceName(insuranceDO.getInsuranceName());
            }
            InsuranceCompanyDO companyDO = insuranceCompanyService.getById(insuranceRecordDO.getInsuranceCompanyId());
            insuranceRecordRetreatBO.setCompanyName(companyDO.getCompanyName());
        }

        return insuranceRecordRetreatBO;
    }
}
