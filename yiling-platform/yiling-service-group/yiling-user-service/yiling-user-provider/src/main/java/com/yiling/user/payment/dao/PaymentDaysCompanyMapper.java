package com.yiling.user.payment.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.payment.entity.PaymentDaysCompanyDO;

/**
 * <p>
 * 集团账期总额度 Dao 接口
 * </p>
 *
 * @author tingwei.chen
 * @date 2021-07-02
 */
@Repository
public interface PaymentDaysCompanyMapper extends BaseMapper<PaymentDaysCompanyDO> {

    /**
     * 使用集团额度
     *
     * @param useAmount 使用额度
     * @return
     */
    int use(@Param("useAmount") BigDecimal useAmount);

    /**
     * 退还集团额度
     *
     * @param refundAmount 退还额度
     * @return
     */
    int refund(@Param("refundAmount") BigDecimal refundAmount);

    /**
     * 还款集团额度
     *
     * @param repaymentAmount 还款额度
     * @return
     */
    int repayment(@Param("repaymentAmount") BigDecimal repaymentAmount);
}
