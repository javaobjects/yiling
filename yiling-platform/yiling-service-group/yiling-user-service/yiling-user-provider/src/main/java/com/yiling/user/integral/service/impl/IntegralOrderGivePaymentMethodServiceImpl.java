package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.integral.entity.IntegralOrderGiveEnterpriseTypeDO;
import com.yiling.user.integral.entity.IntegralOrderGivePaymentMethodDO;
import com.yiling.user.integral.dao.IntegralOrderGivePaymentMethodMapper;
import com.yiling.user.integral.service.IntegralOrderGivePaymentMethodService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 订单送积分-支付方式表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
@Service
public class IntegralOrderGivePaymentMethodServiceImpl extends BaseServiceImpl<IntegralOrderGivePaymentMethodMapper, IntegralOrderGivePaymentMethodDO> implements IntegralOrderGivePaymentMethodService {

    @Override
    public List<Integer> getByGiveRuleId(Long giveRuleId) {
        LambdaQueryWrapper<IntegralOrderGivePaymentMethodDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGivePaymentMethodDO::getGiveRuleId, giveRuleId);
        return this.list(wrapper).stream().map(IntegralOrderGivePaymentMethodDO::getPaymentMethod).distinct().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePaymentMethod(Long giveRuleId, List<Integer> paymentMethodList, Long opUserId) {
        // 删除存在的企业类型
        IntegralOrderGivePaymentMethodDO paymentMethodDO = new IntegralOrderGivePaymentMethodDO();
        paymentMethodDO.setOpUserId(opUserId);
        LambdaQueryWrapper<IntegralOrderGivePaymentMethodDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralOrderGivePaymentMethodDO::getGiveRuleId, giveRuleId);
        this.batchDeleteWithFill(paymentMethodDO, wrapper);
        // 添加企业类型
        if (CollUtil.isNotEmpty(paymentMethodList)) {
            List<IntegralOrderGivePaymentMethodDO> paymentMethodDOList = paymentMethodList.stream().map(paymentMethod -> {
                IntegralOrderGivePaymentMethodDO givePaymentMethodDO = new IntegralOrderGivePaymentMethodDO();
                givePaymentMethodDO.setGiveRuleId(giveRuleId);
                givePaymentMethodDO.setPaymentMethod(paymentMethod);
                givePaymentMethodDO.setOpUserId(opUserId);
                return givePaymentMethodDO;
            }).collect(Collectors.toList());

            this.saveBatch(paymentMethodDOList);
        }

        return true;
    }
}
