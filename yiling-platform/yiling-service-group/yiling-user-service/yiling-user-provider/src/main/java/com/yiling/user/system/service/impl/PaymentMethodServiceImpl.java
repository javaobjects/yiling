package com.yiling.user.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.user.system.dao.PaymentMethodMapper;
import com.yiling.user.system.entity.PaymentMethodDO;
import com.yiling.user.system.service.PaymentMethodService;

/**
 * <p>
 * 支付方式表 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-03
 */
@Service
public class PaymentMethodServiceImpl extends BaseServiceImpl<PaymentMethodMapper, PaymentMethodDO> implements PaymentMethodService {

    @Override
    public List<PaymentMethodDO> listByPlatform(PlatformEnum platformEnum, EnableStatusEnum statusEnum) {
        LambdaQueryWrapper<PaymentMethodDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PaymentMethodDO::getPlatform, platformEnum.getCode());

        if (statusEnum != EnableStatusEnum.ALL) {
            lambdaQueryWrapper.eq(PaymentMethodDO::getStatus, statusEnum.getCode());
        }

        lambdaQueryWrapper.orderByAsc(PaymentMethodDO::getSort);
        return this.list(lambdaQueryWrapper);
    }
}
