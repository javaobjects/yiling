package com.yiling.payment.pay.api;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.pojo.Result;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.dto.request.CreatePaymentTransferRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;

/**
 * 企业打款API
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.api
 * @date: 2021/11/19
 */
public interface PayTransferApi {

    /**
     * 创建企业打款
     * @param transferRequest
     * @return
     */
    Result<Map<Long,String>> createPayTransfer(CreatePaymentTransferRequest transferRequest);

    /**
     * 打款回调
     * @param payCallBackRequest
     * @return
     * @throws PayException
     */
    Result<String> transferCallBack(PayCallBackRequest payCallBackRequest) throws PayException;

    /**
     *  查询打款中，待查询的打款记录
     * @param limit 查询数据条数
     * @return 打款订单号
     */
    List<String> listTransferIngRecords(Integer limit);


    /**
     *  查询待打款的打款记录
     * @param limit 查询条数
     * @return 打款单号
     */
    List<String> listWaitTransferRecords(Integer limit);


    /**
     * 企业打款查询
     * @param payNo 打款单号
     * @return
     * @throws PayException
     */
    Result<String> transferQueryTimer(String payNo) throws PayException;


    /**
     * 企业打款执行定时器(操作打款定时器，异步打款第三方)
     * @param payNo 打款单号
     * @return
     * @throws PayException
     */
    Result<Void> transferRequestExecuteTimer(String payNo) throws PayException;
}
