package com.yiling.payment.channel.service;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service
 * @date: 2021/10/15
 */
public interface PayFactory {

    /**
     * 根据支付方式返回相应的支付接口.
     *
     * @param payWay 支付方式
     * @param paySource 支付来源
     *            支付方式
     * @return 支付接口
     */
    public PayService getPayInstance(String payWay,String paySource);

    /**
     * 获取企业打款实例
     * @param payWay 支付方式
     * @param paySource 支付来源
     *            支付方式
     * @return 支付接口
     */
    public TransferService getTransferInstance(String payWay,String paySource);
}
