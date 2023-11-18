package com.yiling.payment.pay.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.pay.entity.PaymentRefundDO;

/**
 * <p>
 * 支付退款表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
public interface PaymentRefundService extends BaseService<PaymentRefundDO> {

    /**
     * 执行插入退款订单操作
     * @param PaymentRefundDo
     * @return
     */
    Result<String> insertPaymentRefundDo(PaymentRefundDO PaymentRefundDo);

    /**
     * 查询交易订单对应的退款单
     * @param appOrderId
     * @return
     */
    List<PaymentRefundDO> listPaymentRefundsByAppOrderId(Long appOrderId,String payNo);

    /**
     * 根据订单系统退款ID查询退款流水记录
     * @param refundId 订单系统退单单据ID
     * @return
     */
    List<PaymentRefundDO> listPaymentRefundsByRefundId(Long refundId);

    /**
     * 修改交易状态
     * @param paymentRefundDo
     * @return
     */
    boolean updatePaymentRefundStatus(PaymentRefundDO paymentRefundDo);

    /**
     * 批量关闭支付交易状态
     * @param paymentRefundDOList
     * @return
     */
    boolean batchClosePaymentRefund(List<PaymentRefundDO> paymentRefundDOList);

    /**
     * 查询等待退款的集合
     * @param limit
     * @return
     */
    List<PaymentRefundDO> listWaitRefundList(Integer limit);

    /**
     * 查询退款中的订单
     * @param limit
     * @return
     */
    List<PaymentRefundDO> listRefundIngList(Integer limit);

    /**
     * 通过退款申请单号查询退款申请记录
     * @param refundNo
     * @return
     */
    List<PaymentRefundDO> selectRefundList(String refundNo);


    /**
     * 查询超过一天内超时未退款的记录
     * @return
     */
    List<PaymentRefundDO> selectTimeOutRefundOrderList();

}
