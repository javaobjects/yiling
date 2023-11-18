package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.entity.IntegralOrderGivePaymentMethodDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 订单送积分-支付方式表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-30
 */
public interface IntegralOrderGivePaymentMethodService extends BaseService<IntegralOrderGivePaymentMethodDO> {

    /**
     * 根据规则ID获取支付方式
     *
     * @param giveRuleId
     * @return
     */
    List<Integer> getByGiveRuleId(Long giveRuleId);

    /**
     * 保存支付方式
     *
     * @param giveRuleId
     * @param paymentMethodList
     * @param opUserId
     * @return
     */
    boolean savePaymentMethod(Long giveRuleId, List<Integer> paymentMethodList, Long opUserId);
}
