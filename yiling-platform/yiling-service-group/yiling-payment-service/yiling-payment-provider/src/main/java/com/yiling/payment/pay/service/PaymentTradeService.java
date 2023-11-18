package com.yiling.payment.pay.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.pay.entity.PaymentTradeDO;

/**
 * <p>
 * 交易订单记录表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
public interface PaymentTradeService extends BaseService<PaymentTradeDO> {


    /**
     * 执行插入合并订单操作
     * @param paymentTradeDo
     * @return
     */
    Result<String> insertPaymentTrade(PaymentTradeDO paymentTradeDo);

    /**
     * 修改交易状态
     * @param paymentTradeDo
     * @return
     */
    boolean updatePaymentTradeStatus(PaymentTradeDO paymentTradeDo);

    /**
     * 通过支付交易流水号查询，交易流水数据
     * @param payNo 交易流水号
     * @return
     */
    PaymentTradeDO selectPaymentTradeByPayNo(String payNo);

    /**
     *  通过交易ID查询支付完成的交易记录
     * @param payId 交易ID
     * @return
     */
    List<PaymentTradeDO> selectFinishPaymentTradeByPayId(String payId);

    /**
     * 查询支付中的交易记录
     * @param payId
     * @return
     */
    List<PaymentTradeDO> selectPayIngTradeByPayId(String payId);

    /**
     * 查询待支付的交易记录
     * @param limit
     * @return
     */
    List<PaymentTradeDO> selectWaitPaymentTrades(Integer limit);

    /**
     * 通银行单号查询交易信息
     * @param bankOrderId
     * @return
     */
    PaymentTradeDO selectPaymentTradeByBank(String bankOrderId);
}
