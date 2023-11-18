package com.yiling.job.executor.service.jobhandler;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderRefundApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.enums.PreSalOrderReminderTypeEnum;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/** 预售订单定时器
 * @author zhigang.guo
 * @date: 2022/10/11
 */
@Component
@Slf4j
public class PreSaleOrderHandler {

    @DubboReference
    PresaleOrderApi presaleOrderApi;
    @DubboReference(timeout = 1000 * 60)
    OrderProcessApi orderMallApi;
    @DubboReference
    OrderRefundApi   orderRefundApi;


    /**
     * 尾款支付短信提醒定时器
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("balanceNoticeHandler")
    public ReturnT<String> balanceNoticeHandler(String param) throws Exception {
        XxlJobHelper.log("任务开始:预售订单支付尾款短信提醒定时器开始，请求参数为:[{}]", param);
        long start = System.currentTimeMillis();
        List<String> sendOrderList = presaleOrderApi.selectNeedPayBalanceSmsOrders();
        if (CollectionUtil.isEmpty(sendOrderList)) {
            XxlJobHelper.log("任务结束:发短信记录为空!");
            return ReturnT.SUCCESS;
        }

        // 发送支付尾款短信提醒
        sendOrderList.forEach(orderNo -> {
            orderMallApi.sendPresaleOrderSmsNotice(orderNo, PreSalOrderReminderTypeEnum.BALANCE_PAY_REMINDER);
        });
        XxlJobHelper.log("任务结束:预售订单支付尾款短信提醒定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    /**
     * 尾款取消定时器
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("balanceCancelNoticeHandler")
    public ReturnT<String> balanceCancelNoticeHandler(String param) throws Exception {
        Integer hour = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 24;
        XxlJobHelper.log("任务开始:预售订单支付尾款取消短信定时器开始，请求参数为:[{}]", hour);
        long start = System.currentTimeMillis();
        List<String> sendOrderList = presaleOrderApi.selectNeedSendBalanceSmsOrders(hour);
        if (CollectionUtil.isEmpty(sendOrderList)) {
            XxlJobHelper.log("任务结束:发短信记录为空!");
            return ReturnT.SUCCESS;
        }

        // 发送支付尾款短信提醒
        sendOrderList.forEach(orderNo -> {
                orderMallApi.sendPresaleOrderSmsNotice(orderNo, PreSalOrderReminderTypeEnum.BALANCE_CANCEL_REMINDER);
        });
        XxlJobHelper.log("任务结束:预售订单支付尾款取消短信定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }







    /**
     * 尾款未支付超时自动取消定时器
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("presaleOrderCancelHandler")
    public ReturnT<String> presaleOrderCancelHandler(String param) throws Exception {
        XxlJobHelper.log("任务开始:预售订单支付尾款未支付取消定时器开始");
        long start = System.currentTimeMillis();
        List<Long> cancelOrderList = presaleOrderApi.selectTimeOutNotPayBalanceOrder();

        if (CollectionUtil.isEmpty(cancelOrderList)) {
            XxlJobHelper.log("任务结束:订单记录为空记录为空!");
            return ReturnT.SUCCESS;
        }

        log.info("任务开始:预售订单支付尾款未支付取消的订单为，:[{}]", JSON.toJSON(cancelOrderList));
        ForkJoinPool forkJoinPool = new ForkJoinPool(5);
        forkJoinPool.execute(() -> {
            cancelOrderList.parallelStream().forEach(t -> {
                try {
                    orderMallApi.cancel(t, 0l);
                } catch (BusinessException e) {
                    log.error("取消报错:", e);
                }
            });
        });
        log.info("任务结束:预售订单支付尾款未支付取消定时器,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }


    /**
     * 自动审核超时补偿定时器，
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("refundAuditHandler")
    public ReturnT<String> refundAuditHandler(String param) throws Exception {
        XxlJobHelper.log("任务开始:查询超时未自动审核定时器开始");
        long start = System.currentTimeMillis();
        Integer count = StringUtils.isNotBlank(param) ? Integer.valueOf(param) : 100;
        orderRefundApi.compensationAutoNotAuditData(count);
        log.info("任务开始:查询超时未自动审核定时器结束,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }



}
