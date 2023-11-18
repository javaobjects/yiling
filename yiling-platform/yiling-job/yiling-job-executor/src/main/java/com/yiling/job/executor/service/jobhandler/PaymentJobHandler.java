package com.yiling.job.executor.service.jobhandler;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.api.PayTransferApi;
import com.yiling.payment.pay.api.RefundApi;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付定时器
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.job.executor.service.jobhandler
 * @date: 2022/4/11
 */
@Component
@Slf4j
public class PaymentJobHandler {
    @DubboReference(timeout = 1000 * 60)
    RefundApi       refundApi;
    @DubboReference(timeout = 1000 * 60)
    PayTransferApi  transferApi;
    @DubboReference(async = true)
    PayApi          payApi;

    /**
     * 支付临时交易，自动取消定时器
     * @param param 定时器参数
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("PaymentCloseJobHandler")
    public ReturnT<String> paymentClose(String param) throws Exception {
        Integer limit = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 100;
        log.info("任务开始:在线支付关闭定时器开始，请求参数为:[{}]", limit);
        payApi.closeTimer(limit);
        DubboUtils.quickAsyncCall("payApi", "closeTimer");
        log.info("任务结束:在线支付退款定时器结束");
        return ReturnT.SUCCESS;
    }


    /**
     * 退款定时器
     * @param param 定时器参数
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("PaymentRefundJobHandler")
    public ReturnT<String> paymentRefund(String param) throws Exception {
        Integer limit = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 10;
        XxlJobHelper.log("任务开始:在线支付退款定时器开始，请求参数为:[{}]", limit);
        long start = System.currentTimeMillis();
        List<String> refundList = refundApi.listWaitRefundList(limit);
        if (CollectionUtil.isEmpty(refundList)) {
            XxlJobHelper.log("任务结束:退款记录为空!");
            return ReturnT.SUCCESS;
        }
        refundList.parallelStream().forEach(refundNo -> {
            refundApi.refundByRefundNo(refundNo);
        });
        XxlJobHelper.log("任务结束:在线支付退款定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    /**
     * 退款检查，补偿并查询退款中的退款记录是否退款成功
     * @param param 定时器参数
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("RefundCheckJobHandler")
    public ReturnT<String> paymentCheckRefund(String param) throws Exception {
        Integer limit = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 100;
        log.info("任务开始:在线支付退款校验定时器开始，请求参数为:[{}]", limit);
        long start = System.currentTimeMillis();
        refundApi.checkRefundTime(limit);
        List<String> refundNoList = refundApi.selectTimeOutRefundOrderList();
        if (CollectionUtil.isNotEmpty(refundNoList)) {
            log.error("退款单号:{" + StringUtils.join(refundNoList, ",") + "},超时未退款,请检查退款记录!");
        }
        log.info("任务结束:在线支付退款校验定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    /**
     * 支付打款定时器
     * @param param 定时器参数
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("TransferJobHandler")
    public ReturnT<String> transferJobHandler(String param) throws Exception {
        Integer limit = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 10;
        XxlJobHelper.log("任务开始:企业打款定时器开始，请求参数为:[{}]", limit);
        long start = System.currentTimeMillis();
        List<String> payNoList = transferApi.listWaitTransferRecords(limit);
        if (CollectionUtil.isEmpty(payNoList)) {
            XxlJobHelper.log("任务结束:打款记录为空!");
            return ReturnT.SUCCESS;
        }
        payNoList.parallelStream().forEach(payNo -> {
            transferApi.transferRequestExecuteTimer(payNo);
        });
        XxlJobHelper.log("任务结束:企业打款定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    /**
     * 打款补偿定时器，补偿查询打款中的打款记录是否成功
     * @param param 定时器参数
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("TransferCheckJobHandler")
    public ReturnT<String> transferCheck(String param) throws Exception {
        Integer limit = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 100;
        long start = System.currentTimeMillis();
        XxlJobHelper.log("任务开始:企业打款补偿定时器开始，请求参数为:[{}]", limit);
        List<String> payNoList = transferApi.listTransferIngRecords(limit);
        if (CollectionUtil.isEmpty(payNoList)) {
            XxlJobHelper.log("任务结束:补偿记录为空!");
            return ReturnT.SUCCESS;
        }
        payNoList.parallelStream().forEach(payNo -> {
            transferApi.transferQueryTimer(payNo);
        });
        XxlJobHelper.log("任务结束:企业打款补偿定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

}