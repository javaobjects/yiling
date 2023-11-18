package com.yiling.user.payment.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.payment.dao.PaymentDaysCompanyMapper;
import com.yiling.user.payment.entity.PaymentDaysCompanyDO;
import com.yiling.user.payment.service.PaymentDaysCompanyService;

/**
 * <p>
 * 集团账期总额度 服务实现类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Service
public class PaymentDaysCompanyServiceImpl extends BaseServiceImpl<PaymentDaysCompanyMapper, PaymentDaysCompanyDO> implements PaymentDaysCompanyService {

    @Override
    public PaymentDaysCompanyDO get() {
        QueryWrapper<PaymentDaysCompanyDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean use(BigDecimal useAmount) {
        int n = this.baseMapper.use(useAmount);
        if (n == 0) {
            throw new BusinessException(UserErrorCode.PAYMENT_DAYS_ACCOUNT_GROUP_BALANCE_NOT_ENOUGH);
        }
        return true;
    }

    @Override
    public boolean refund(BigDecimal refundAmount) {
        int n = this.baseMapper.refund(refundAmount);
        return n > 0;
    }
}
