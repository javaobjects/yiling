package com.yiling.user.payment.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.user.payment.entity.PaymentDaysAccountDO;

/**
 * <p>
 * 客户账期账户表 Dao 接口
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Repository
public interface PaymentDaysAccountMapper extends BaseMapper<PaymentDaysAccountDO> {

    /**
     * 使用账期额度
     *
     * @param id ID
     * @param useAmount 使用额度
     * @return
     */
    int use(@Param("id") Long id, @Param("useAmount")BigDecimal useAmount);

    /**
     * 使用账期额度
     *
     * @param id ID
     * @param useAmount 使用额度
     * @return
     */
    int feiYilingUse(@Param("id") Long id, @Param("useAmount")BigDecimal useAmount);

    /**
     * 退还账期额度
     *
     * @param id ID
     * @param refundAmount 退还额度
     * @return
     */
    int refund(@Param("id") Long id, @Param("refundAmount") BigDecimal refundAmount);

    /**
     * 退还账期额度
     *
     * @param id ID
     * @param refundAmount 退还额度
     * @return
     */
    int feiYilingRefund(@Param("id") Long id, @Param("refundAmount") BigDecimal refundAmount);

    /**
     * 修改账期额度
     *
     * @param paymentDaysAccountDO
     * @return
     */
    int yilingUpdate(PaymentDaysAccountDO paymentDaysAccountDO);

    /**
     * 修改账期额度
     *
     * @param paymentDaysAccountDO
     * @return
     */
    int feiYilingUpdate(PaymentDaysAccountDO paymentDaysAccountDO);
    /**
     * 修改账期临时额度
     *
     * @param paymentDaysAccountDO
     * @return
     */
    int yilingUpdateTemporary(PaymentDaysAccountDO paymentDaysAccountDO);

    /**
     * 更新还款订单对应的账期账户金额
     * @param paymentDaysAccountDO
     * @return
     */
    int updateRepayment(PaymentDaysAccountDO paymentDaysAccountDO);
}
