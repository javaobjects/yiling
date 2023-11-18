package com.yiling.payment.pay.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;

/**
 * <p>
 * 合并支付订单表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-20
 */
public interface PaymentMergeOrderService extends BaseService<PaymentMergeOrderDO> {

    /**
     * 执行插入合并订单操作
     * @param paymentMergeOrderDOS
     * @return
     */
    Result<String> insertPaymentMergeOrder(List<PaymentMergeOrderDO> paymentMergeOrderDOS);

    /**
     * 通过订单ID,查询订单记录
     * @param appOrderIds
     * @return
     */
    @Deprecated
    List<PaymentMergeOrderDO> selectMergerOrderByOrderIdList(String orderPlatform,Integer tradeType,List<Long> appOrderIds);

    /**
     * 通过内部交易单号查询订单支付记录
     * @param payNo
     * @return
     */
    List<PaymentMergeOrderDO> selectMergerOrderByPayNo(String payNo);


    /**
     * 通过订单号查询订单记录
     * @param appOrderNo
     * @return
     */
    @Deprecated
    List<PaymentMergeOrderDO> selectMergerOrderByOrderNoList(String appOrderNo);


    /**
     * 通过订单号查询订单记录
     * @param appOrderNo
     * @return
     */
    List<PaymentMergeOrderDO> selectMergerOrderByOrderNoList(String orderPlatform,String appOrderNo);

    /**
     * 通过订单号查询合并支付记录
     * @param orderNos
     * @param tradeType
     * @return
     */
    List<PaymentMergeOrderDO>  selectMergerOrderByOrderNoList(String orderPlatform,Integer tradeType,List<String> orderNos);

    /**
     * 查询支付完成的支付订单
     * @param appOrderId
     * @param tradeType
     * @return
     */
    List<PaymentMergeOrderDO> selectFinishMergeOrderList(Long appOrderId,Integer tradeType);


    /**
     * 查询待支付的结算订单
     * @return
     */
    List<PaymentMergeOrderDO> selectWaitPayOrderList();


    /**
     * 更新订单的退款金额
     * @param mergeId 主键ID
     * @param refundAmount 本次退款金额
     */
    Boolean updateMergeOrderFundAmount(Long mergeId, BigDecimal refundAmount);

    /**
     *  通过支付交易ID查询合并支付订单数据
     * @param payId 支付交易ID
     * @return
     */
    List<PaymentMergeOrderDO> selectMergerOrderByPayId(String payId);

    /**
     * 修改订单的支付状态
     * @param payId 交易ID
     * @param appOrderStatus 订单状态
     */
    void updateMergeOrderAppOrderStatus(String payId, Integer appOrderStatus, Date limitTime,String payNo);

    /**
     * 根据payID修改合并订单信息
     * @param paymentMergeOrderDO
     */
    void updateMergeOrderByPayId(PaymentMergeOrderDO paymentMergeOrderDO);

}
