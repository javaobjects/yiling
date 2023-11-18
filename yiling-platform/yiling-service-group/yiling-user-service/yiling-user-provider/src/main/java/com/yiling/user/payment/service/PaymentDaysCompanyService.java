package com.yiling.user.payment.service;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.payment.entity.PaymentDaysCompanyDO;

/**
 * <p>
 * 集团账期总额度 服务类
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
public interface PaymentDaysCompanyService extends BaseService<PaymentDaysCompanyDO> {

    /**
     * 获取集团账期信息
     *
     * @return
     */
    PaymentDaysCompanyDO get();

    /**
     * 使用集团账期额度
     *
     * @param useAmount 使用额度
     * @return
     */
    boolean use(BigDecimal useAmount);

    /**
     * 退还集团账期额度
     *
     * @param refundAmount 退还额度
     * @return
     */
    boolean refund(BigDecimal refundAmount);
}
