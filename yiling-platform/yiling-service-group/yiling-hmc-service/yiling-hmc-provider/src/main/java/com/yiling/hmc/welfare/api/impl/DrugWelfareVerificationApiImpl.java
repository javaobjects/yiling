package com.yiling.hmc.welfare.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.api.DrugWelfareVerificationApi;
import com.yiling.hmc.welfare.dto.DrugWelfareVerificationDTO;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponSaveRequest;
import com.yiling.hmc.welfare.service.DrugWelfareVerificationService;

/**
 * @author: hongyang.zhang
 * @data: 2022/10/08
 */
@DubboService
public class DrugWelfareVerificationApiImpl implements DrugWelfareVerificationApi {

    @Autowired
    private DrugWelfareVerificationService verificationService;

    @Override
    public List<DrugWelfareVerificationDTO> getDrugWelfareVerificationByGroupCouponIds(List<Long> groupCouponIds) {
        return PojoUtils.map(verificationService.getDrugWelfareVerificationByGroupCouponIds(groupCouponIds), DrugWelfareVerificationDTO.class);
    }

    @Override
    public void saveVerification(DrugWelfareGroupCouponSaveRequest request) {
        verificationService.saveVerification(request);
    }
}
