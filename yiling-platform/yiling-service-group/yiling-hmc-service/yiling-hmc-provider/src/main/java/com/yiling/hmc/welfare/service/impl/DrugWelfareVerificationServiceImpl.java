package com.yiling.hmc.welfare.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.welfare.dao.DrugWelfareVerificationMapper;
import com.yiling.hmc.welfare.dto.request.DrugWelfareGroupCouponSaveRequest;
import com.yiling.hmc.welfare.entity.DrugWelfareVerificationDO;
import com.yiling.hmc.welfare.service.DrugWelfareVerificationService;

/**
 * <p>
 * 药品福利核销表 服务实现类
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Service
public class DrugWelfareVerificationServiceImpl extends BaseServiceImpl<DrugWelfareVerificationMapper, DrugWelfareVerificationDO> implements DrugWelfareVerificationService {

    @Override
    public List<DrugWelfareVerificationDO> getDrugWelfareVerificationByGroupCouponIds(List<Long> groupCouponIds) {
        LambdaQueryWrapper<DrugWelfareVerificationDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DrugWelfareVerificationDO::getDrugWelfareGroupCouponId, groupCouponIds);
        return this.list(queryWrapper);
    }

    @Override
    public void saveVerification(DrugWelfareGroupCouponSaveRequest request) {
        DrugWelfareVerificationDO verificationDO = PojoUtils.map(request, DrugWelfareVerificationDO.class);
        verificationDO.setCreateUser(request.getOpUserId());
        this.save(verificationDO);
    }
}
