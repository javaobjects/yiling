package com.yiling.payment.pay.dao;

import org.springframework.stereotype.Repository;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.payment.pay.entity.PaymentTradeDO;

/**
 * <p>
 * 交易订单记录表 Dao 接口
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
@Repository
public interface PaymentTradeMapper extends BaseMapper<PaymentTradeDO> {

}
