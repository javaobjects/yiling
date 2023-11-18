package com.yiling.job.executor.service.jobhandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.order.api.OrderProcessApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderB2BApi;
import com.yiling.order.order.dto.OrderDTO;

import cn.hutool.core.collection.CollectionUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单取消定时器
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.job.executor.service.jobhandler
 * @date: 2021/9/23
 */
@Component
@Slf4j
public class OrderCancelHandler {
    @DubboReference(timeout = 1000 * 60)
    OrderApi        orderApi;
    @DubboReference(timeout = 1000 * 60)
    OrderB2BApi     orderB2BApi;
    @DubboReference(timeout = 1000 * 60)
    OrderProcessApi orderMallApi;

    /**
     * 销售助手，已转发给客户，24H客户未为确认，自动取消订单
     *
     * @param param 定时器参数
     * @return
     */
    @JobLog
    @XxlJob("customerCancelOrderHandler")
    @SneakyThrows
    public ReturnT<String> customerCancelOrderHandler(String param) {
        List<String> orderNoList = Optional.ofNullable(param).map(e -> Arrays.asList(e.split(","))).orElse(Collections.emptyList());
        long start = System.currentTimeMillis();
        log.debug("orderCancelHandler:任务开始，参数{}", orderNoList);
        List<OrderDTO> notConfirmOrderList = orderApi.listCustomerNotConfirmByOrderNos(orderNoList);

        if (CollectionUtil.isEmpty(notConfirmOrderList)) {
            log.info("任务结束:没有查询到待取消的记录!");
            return ReturnT.SUCCESS;
        }

        log.info("任务开始:销售助手已转发未确认的订单为，:[{}]", JSON.toJSON(notConfirmOrderList));
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> {
            notConfirmOrderList.parallelStream().forEach(t -> {
                try {
                    orderMallApi.cancel(t.getId(), 0l);
                } catch (BusinessException e) {
                    log.error("取消报错:", e);
                }
            });
        });

        log.info("任务结束:销售助手未确认自动取消定时器,耗时：" + (System.currentTimeMillis() - start));

        return ReturnT.SUCCESS;
    }

    /**
     * B2B在线支付订单，15分钟未支付。自动取消订单
     *
     * @param param 定时器参数,默认15分钟
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("CancelB2BOrderHandler")
    public ReturnT<String> cancelB2BOrderHandler(String param) throws Exception {
        long start = System.currentTimeMillis();
        List<Long> orderIdList = orderB2BApi.selectOnlineNotPayOrder(15);
        log.info("任务开始:B2B自动取消定时器参数，请求参数为:[{}]", orderIdList);
        if (CollectionUtil.isEmpty(orderIdList)) {
            log.info("任务结束:没有查询到待取消的记录!");
            return ReturnT.SUCCESS;
        }
        log.info("任务开始:B2B自动取消定时器的订单为，:[{}]", JSON.toJSON(orderIdList));
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> {
            orderIdList.parallelStream().forEach(t -> {
                try {
                    orderMallApi.cancel(t, 0l);
                } catch (BusinessException e) {
                    log.error("取消报错:", e);
                }
            });
        });
        log.info("任务结束:B2B自动取消定时器,耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }

    /**
     * 取消没有选择支付方式的预订单
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("cancelExpectOrderHandler")
    public Boolean cancelExpectOrderHandler(String param) throws Exception {
        long start = System.currentTimeMillis();
        List<OrderDTO> orderList = orderApi.overTimeCancelExpectOrder(48);
        List<Long> orderIdList = orderList.stream().map(o -> o.getId()).collect(Collectors.toList());
        log.info("任务开始:取消没有选择支付方式的预订单，请求参数为:[{}]", orderIdList);
        if (CollectionUtil.isEmpty(orderIdList)) {
            log.info("任务结束:没有查询到待取消的记录!");
            return Boolean.TRUE;
        }
        log.info("任务开始:取消没有选择支付方式的预订单，:[{}]", JSON.toJSON(orderIdList));
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.execute(() -> {
            orderIdList.parallelStream().forEach(t -> {
                try {
                    orderMallApi.cancelOrderExpect(t,0L);
                } catch (BusinessException e) {
                    log.error("取消报错:", e);
                }
            });
        });
        log.info("任务结束:取消没有选择支付方式的预订单,耗时：" + (System.currentTimeMillis() - start));
        return Boolean.TRUE;
    }

}
