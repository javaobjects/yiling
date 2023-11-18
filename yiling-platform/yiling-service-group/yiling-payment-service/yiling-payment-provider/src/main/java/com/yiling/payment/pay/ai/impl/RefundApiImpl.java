package com.yiling.payment.pay.ai.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.TradeContentJsonDTO;
import com.yiling.payment.channel.service.dto.request.CreateRefundRequest;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.api.RefundApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;
import com.yiling.payment.pay.dto.request.RefundCallBackRequest;
import com.yiling.payment.pay.dto.request.RefundParamListRequest;
import com.yiling.payment.pay.dto.request.RefundParamRequest;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentRefundDO;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentRefundService;
import com.yiling.payment.pay.service.PaymentRepeatOrderService;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付退款
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.ai.impl
 * @date: 2021/11/1
 */
@DubboService
@Slf4j
public class RefundApiImpl implements RefundApi {
    @Autowired
    private PayFactory                 payFactory;
    @Autowired
    private PaymentMergeOrderService   paymentMergeOrderService;
    @Autowired
    private PaymentTradeService        paymentTradeService;
    @Autowired
    private PaymentRefundService       paymentRefundService;
    @Autowired
    private PaymentRepeatOrderService  paymentRepeatOrderService;
    @DubboReference
    MqMessageSendApi                   mqMessageSendApi;
    @Autowired
    private RedisDistributedLock       redisDistributedLock;
    @Lazy
    @Autowired
    RefundApiImpl                      _this;
    @Value("${spring.profiles.active}")
    private String                     env;


    @Override
    public Result<List<PayOrderDTO>> selectCanRefundPayOrder(OrderPlatformEnum orderPlatform,RefundParamRequest refundParamRequest) {

        refundParamRequest.setRefundAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal("0.01") : refundParamRequest.getRefundAmount());
        List<PaymentMergeOrderDO> paymentMergeOrderDoList = paymentMergeOrderService.selectMergerOrderByOrderNoList(orderPlatform.getCode(),refundParamRequest.getAppOrderNo());
        // 查询对应平台的支付记录
        paymentMergeOrderDoList = paymentMergeOrderDoList.stream().filter(t -> orderPlatform == OrderPlatformEnum.getByCode(t.getOrderPlatform()))
                .filter(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {
            return Result.failed("该订单未查询到支付记录!");
        }
        // 未退款，且不是重复支付的订单
        paymentMergeOrderDoList = paymentMergeOrderDoList.stream().filter(t -> (t.getRefundState() == 1 || t.getRefundState() == 2) && t.getIsDuplicate() == 0).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {

            return Result.failed("退款金额大于订单支付金额!");
        }

        BigDecimal totalPayAmount = paymentMergeOrderDoList.stream().map(PaymentMergeOrderDO::getPayAmount).reduce(BigDecimal::add).get();
        // 总的已退款金额
        BigDecimal totalHasRefundAmount = paymentMergeOrderDoList.stream().map(PaymentMergeOrderDO::getRefundAmount).reduce(BigDecimal::add).get();

        if (CompareUtil.compare(NumberUtil.sub(totalPayAmount,totalHasRefundAmount,refundParamRequest.getRefundAmount()),BigDecimal.ZERO) < 0) {

            return Result.failed("退款金额大于订单支付金额!");
        }

        List<PayOrderDTO> resultList = Lists.newArrayList();
        // 如果存在多条优先退尾款金额
        paymentMergeOrderDoList = paymentMergeOrderDoList.stream().sorted(Comparator.comparing(PaymentMergeOrderDO::getTradeType,Comparator.reverseOrder())).collect(Collectors.toList());

        for (PaymentMergeOrderDO currentMergeOrder : paymentMergeOrderDoList) {

            PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(currentMergeOrder.getPayNo());

            if (paymentTradeDO == null) {

                log.error("订单:{}未查询到支付交易流水.",currentMergeOrder.getPayNo());
                continue;
            }

            PayOrderDTO payOrderDTO = PojoUtils.map(currentMergeOrder,PayOrderDTO.class);
            payOrderDTO.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
            payOrderDTO.setGoodsAmount(currentMergeOrder.getGoodsAmount());
            resultList.add(payOrderDTO);
        }

        return Result.success(resultList);
    }


    @Override
    public Result<List<PayOrderDTO>> selectCanRefundPayOrder(RefundParamRequest refundParamRequest) {


        return this.selectCanRefundPayOrder(OrderPlatformEnum.B2B,refundParamRequest);
    }

    @Override
    public Result<Void> refundPayOrder(RefundParamListRequest refundParamListRequest) {
        refundParamListRequest.getRefundParamRequestList().forEach(refundParamRequest -> {
            Result<Void> result = this.refundPayOrder(refundParamRequest);
            if (HttpStatus.HTTP_OK != result.getCode()) {

                throw new PayException(result.getMessage());
            }
        });
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> retryFailurePayOrder(RefundParamRequest refundParamRequest) {

        List<PaymentRefundDO> paymentRefundDOList = paymentRefundService.listPaymentRefundsByRefundId(refundParamRequest.getRefundId());

        if (CollectionUtil.isEmpty(paymentRefundDOList)) {
            return Result.failed("未查询到退款记录!");
        }
        List<PaymentRefundDO> paymentRefundDOS = paymentRefundDOList.stream().filter(t -> RefundStateEnum.FALIUE == RefundStateEnum.getByCode(t.getRefundState()))
                .filter(t -> t.getAppOrderNo().equals(refundParamRequest.getAppOrderNo()))
                .filter(t -> t.getPayNo().equals(refundParamRequest.getPayNo()))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentRefundDOS)) {
            return Result.failed("未查询到申请失败的退款记录!");
        }
        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(refundParamRequest.getPayNo());
        if (paymentTradeDO == null || TradeStatusEnum.SUCCESS != TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {
            log.info("{},该订单未查询成功的支付流水记录",refundParamRequest.getAppOrderId());
            return Result.failed("该订单未查询成功的支付流水记录!");
        }
        // 关闭失败的退款交易,重新生成一个退款交易
        paymentRefundService.batchClosePaymentRefund(paymentRefundDOS);

        PaymentRefundDO paymentRefundDo =  new PaymentRefundDO();
        paymentRefundDo.setReason(refundParamRequest.getReason());
        paymentRefundDo.setRefundAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal("0.01") : refundParamRequest.getRefundAmount());
        paymentRefundDo.setAppOrderId(refundParamRequest.getAppOrderId());
        paymentRefundDo.setAppOrderNo(refundParamRequest.getAppOrderNo());
        paymentRefundDo.setPayId(paymentTradeDO.getPayId());
        paymentRefundDo.setRefundType(refundParamRequest.getRefundType());
        paymentRefundDo.setPayNo(refundParamRequest.getPayNo());
        paymentRefundDo.setRefundId(refundParamRequest.getRefundId());
        paymentRefundDo.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        paymentRefundDo.setCreateUser(refundParamRequest.getOpUserId());
        paymentRefundDo.setUpdateUser(refundParamRequest.getOpUserId());
        Result<String> insertRefundResult = this.paymentRefundService.insertPaymentRefundDo(paymentRefundDo);

        if (HttpStatus.HTTP_OK != insertRefundResult.getCode()) {
            log.info("{},创建退款流水记录失败!",refundParamRequest.getAppOrderId());
            return Result.failed(insertRefundResult.getMessage());
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> refundPayOrder(RefundParamRequest refundParamRequest) {
        refundParamRequest.setRefundAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal("0.01") : refundParamRequest.getRefundAmount());
        log.info("refundPayOrder start :{}", JSONUtil.toJsonStr(refundParamRequest));
        if (StringUtils.isBlank(refundParamRequest.getPayNo())) {
            return Result.failed("申请交易单号为空!");
        }

        List<PaymentMergeOrderDO> paymentMergeOrderDoList = paymentMergeOrderService.selectMergerOrderByPayNo(refundParamRequest.getPayNo());
        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {
            log.info("{},该订单未查询到支付记录",refundParamRequest.getAppOrderId());
            return Result.failed("该订单未查询到支付记录!");
        }
        paymentMergeOrderDoList = paymentMergeOrderDoList.stream().filter(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus()) && e.getAppOrderNo().equals(refundParamRequest.getAppOrderNo())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {
            log.info("{},该订单未查询到支付记录",refundParamRequest.getAppOrderId());
            return Result.failed("该订单未查询到支付记录!");
        }
        PaymentMergeOrderDO currentMergeOrder ;
        // 重复支付，需要退款最新的一条支付记录
        if (StringUtils.isNotBlank(refundParamRequest.getPayNo())) {
            currentMergeOrder = paymentMergeOrderDoList.stream().filter(e -> e.getPayNo().equals(refundParamRequest.getPayNo()) && CompareUtil.compare(e.getPayAmount(),e.getRefundAmount()) > 0).findFirst().get();
        } else {
            currentMergeOrder =   paymentMergeOrderDoList.stream().filter(e -> CompareUtil.compare(e.getPayAmount(),e.getRefundAmount()) > 0).findFirst().get();
        }
        if (currentMergeOrder == null) {
            log.info("{},该订单未查询成功的支付流水记录",refundParamRequest.getAppOrderId());
            return Result.failed("该订单未查询成功的支付流水记录!");
        }
        List<PaymentRefundDO> paymentRefundDOList = paymentRefundService.listPaymentRefundsByAppOrderId(refundParamRequest.getAppOrderId(),currentMergeOrder.getPayNo());
        BigDecimal totalPayAmount = paymentMergeOrderDoList.stream().map(PaymentMergeOrderDO::getPayAmount).reduce(BigDecimal::add).get();
        BigDecimal totalRefundAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(paymentRefundDOList)) {
            // 过滤掉失败的结果集
            paymentRefundDOList  = paymentRefundDOList.stream().filter(e -> RefundStateEnum.CLOSE != RefundStateEnum.getByCode(e.getRefundState())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(paymentRefundDOList)) {
                totalRefundAmount  = paymentRefundDOList.stream().map(PaymentRefundDO::getRefundAmount).reduce(BigDecimal::add).get();
            }
        }
        BigDecimal waitRefundAmount = NumberUtil.sub(totalPayAmount,totalRefundAmount);
        if (CompareUtil.compare(BigDecimal.ZERO,waitRefundAmount) > 0 || CompareUtil.compare(refundParamRequest.getRefundAmount(),waitRefundAmount) > 0) {
            log.info("{},该订单退款金额大于订单支付金额",refundParamRequest.getAppOrderId());
            return Result.failed("退款金额大于订单支付金额!");
        }
        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(currentMergeOrder.getPayNo());
        if (paymentTradeDO == null || TradeStatusEnum.SUCCESS != TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {
            log.info("{},该订单未查询成功的支付流水记录",refundParamRequest.getAppOrderId());
            return Result.failed("该订单未查询成功的支付流水记录!");
        }
        PaymentRefundDO paymentRefundDo =  new PaymentRefundDO();
        paymentRefundDo.setReason(refundParamRequest.getReason());
        paymentRefundDo.setRefundAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal("0.01") : refundParamRequest.getRefundAmount());
        paymentRefundDo.setAppOrderId(refundParamRequest.getAppOrderId());
        paymentRefundDo.setAppOrderNo(currentMergeOrder.getAppOrderNo());
        paymentRefundDo.setPayId(currentMergeOrder.getPayId());
        paymentRefundDo.setRefundType(refundParamRequest.getRefundType());
        paymentRefundDo.setPayNo(currentMergeOrder.getPayNo());
        paymentRefundDo.setRefundId(refundParamRequest.getRefundId());
        paymentRefundDo.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        paymentRefundDo.setCreateUser(refundParamRequest.getOpUserId());
        paymentRefundDo.setUpdateUser(refundParamRequest.getOpUserId());
        Result<String> insertRefundResult = this.paymentRefundService.insertPaymentRefundDo(paymentRefundDo);
        if (HttpStatus.HTTP_OK != insertRefundResult.getCode()) {
            log.info("{},创建退款流水记录失败!",refundParamRequest.getAppOrderId());
            return Result.failed(insertRefundResult.getMessage());
        }
        return Result.success();
    }



    @Override
    public Result<Void> checkRefundTime(Integer limit) {

        List<PaymentRefundDO> refundDOList =  paymentRefundService.listRefundIngList(limit);
        if (CollectionUtil.isEmpty(refundDOList)) {
            return Result.success();
        }
        for (PaymentRefundDO refund : refundDOList) {
            PaymentTradeDO tradeDO =  paymentTradeService.selectPaymentTradeByPayNo(refund.getPayNo());
            if (tradeDO == null) {
                continue;
            }
            TradeContentJsonDTO tradeContentJsonDTO =  JSON.parseObject(tradeDO.getContent(), TradeContentJsonDTO.class);
            PayService payService = payFactory.getPayInstance(tradeDO.getPayWay(),tradeDO.getPaySource());

            QueryPayOrderRequest request = new QueryPayOrderRequest();
            request.setPayNo(refund.getPayNo());
            request.setThirdTradeNo(tradeDO.getThirdTradeNo());
            request.setRefund_no(refund.getRefundNo());
            request.setThird_fund_no(refund.getThirdTradeNo());
            request.setMerchantNo(tradeContentJsonDTO != null ? tradeContentJsonDTO.getMerchantNo() : null);

            PayOrderResultDTO resultDTO = payService.orderRefundQuery(request);
            if (resultDTO.getIsSuccess()) {
                RefundCallBackRequest callBackRequest = RefundCallBackRequest.builder()
                        .payNo(refund.getPayNo())
                        .payWay(tradeDO.getPayWay())
                        .refundStatus(RefundStateEnum.SUCCESS.getCode())
                        .third_fund_no(resultDTO.getTradeNo())
                        .refund_no(refund.getRefundNo())
                        .thirdTradeNo(refund.getThirdTradeNo())
                        .thirdState(resultDTO.getThirdState())
                        .refundDate(resultDTO.getTradeDate())
                        .errorMessage(resultDTO.getErrorMessage())
                        .build();
                this.operationRefundCallBackThird(callBackRequest);
            }
        }
        return Result.success();
    }


    @Override
    public List<String> listWaitRefundList(Integer limit) {
        List<PaymentRefundDO> paymentRefundDOList = paymentRefundService.listWaitRefundList(limit);
        if (CollectionUtil.isEmpty(paymentRefundDOList)) {
            return Collections.emptyList();
        }
        return paymentRefundDOList.stream().map(t -> t.getRefundNo()).collect(Collectors.toList());
    }


    /**
     *  调用退款逻辑
     * @param paymentRefundDo
     * @return
     */
    @GlobalTransactional
    public MqMessageBO refundPrepare(PaymentRefundDO paymentRefundDo) {
        MqMessageBO mqMessageBO = null;
        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(paymentRefundDo.getPayNo());
        if (paymentTradeDO == null || TradeStatusEnum.SUCCESS != TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {
            log.info("{},该订单未查询成功的支付流水记录",paymentRefundDo);
            return null;
        }
        List<PaymentMergeOrderDO> paymentMergeOrderDoList = paymentMergeOrderService.selectMergerOrderByPayId(paymentTradeDO.getPayId());
        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {
            log.info("{},该订单未查询到支付记录",paymentMergeOrderDoList);
            return null;
        }
        PaymentMergeOrderDO currentMergeOrder =  paymentMergeOrderDoList.stream().filter(
                e -> CompareUtil.compare(e.getPayAmount(),e.getRefundAmount()) > 0
                        && paymentRefundDo.getAppOrderId().equals(e.getAppOrderId())
                        && paymentRefundDo.getPayNo().equals(e.getPayNo())
        ) .findFirst().get();
        if (currentMergeOrder == null) {
            log.info("{},该订单未查询到支付记录",paymentMergeOrderDoList);
            return null;
        }

        // 退款topic
        String topic = OrderPlatformEnum.HMC == OrderPlatformEnum.getByCode(currentMergeOrder.getOrderPlatform()) ? Constants.TOPIC_HMC_PAY_REFUND_NOTIFY : Constants.TOPIC_ORDER_PAY_REFUND_NOTIFY;
        // 交易商户号存储
        TradeContentJsonDTO tradeContentJsonDTO =  JSON.parseObject(paymentTradeDO.getContent(), TradeContentJsonDTO.class);
        PayService payService = payFactory.getPayInstance(paymentTradeDO.getPayWay(),paymentTradeDO.getPaySource());
        CreateRefundRequest createRefundRequest = PojoUtils.map(paymentRefundDo,CreateRefundRequest.class);
        createRefundRequest.setPayWay(paymentTradeDO.getPayWay());
        createRefundRequest.setRefundNo(paymentRefundDo.getRefundNo());
        createRefundRequest.setRefundAmount(paymentRefundDo.getRefundAmount());
        createRefundRequest.setAmount(paymentTradeDO.getPayAmount());
        createRefundRequest.setMerchantNo(tradeContentJsonDTO != null ? tradeContentJsonDTO.getMerchantNo() : null);
        createRefundRequest.setRemark(paymentRefundDo.getAppOrderNo());

        RefundOrderResultDTO refundOrderResultDto =  payService.refundData(createRefundRequest);

        paymentRefundDo.setRefundState(refundOrderResultDto.getRefundStateEnum().getCode());
        paymentRefundDo.setErrorMessage(refundOrderResultDto.getErrorMessage());
        paymentRefundDo.setThirdFundNo(refundOrderResultDto.getRefundId());
        // 修改订单上的已退款金额
        if (refundOrderResultDto.getRefundStateEnum() == RefundStateEnum.SUCCESS) {
            paymentRefundDo.setRefundDate(new Date());
            paymentMergeOrderService.updateMergeOrderFundAmount(currentMergeOrder.getId(),paymentRefundDo.getRefundAmount());
        }

        paymentRefundService.updatePaymentRefundStatus(paymentRefundDo);

        // 如果为重复退款，无需发送退款消息
        if (2 == paymentRefundDo.getRefundType()) {
            // 修改重复退款的退款状态
            PaymentRepeatOrderDO repeatOrderDo = new PaymentRepeatOrderDO();
            repeatOrderDo.setId(paymentRefundDo.getRefundId());
            repeatOrderDo.setRefundState(refundOrderResultDto.getRefundStateEnum().getCode());
            repeatOrderDo.setThirdFundNo(refundOrderResultDto.getRefundId());
            repeatOrderDo.setRefundDate(new Date());
            repeatOrderDo.setErrorMessage(refundOrderResultDto.getErrorMessage());
            paymentRepeatOrderService.updateById(repeatOrderDo);

            return null;
        }
        if (1 == paymentRefundDo.getRefundType()) {
            refundOrderResultDto.setAppRefundId(paymentRefundDo.getRefundId());
            refundOrderResultDto.setRealReturnAmount(paymentRefundDo.getRefundAmount());
            refundOrderResultDto.setOpUserId(paymentRefundDo.getCreateUser());
            refundOrderResultDto.setTradeTypeEnum(TradeTypeEnum.getByCode(currentMergeOrder.getTradeType()));
            refundOrderResultDto.setOrderPlatform(OrderPlatformEnum.getByCode(currentMergeOrder.getOrderPlatform()));
            refundOrderResultDto.setAppOrderNo(currentMergeOrder.getAppOrderNo());
            // 退款交易单号
            refundOrderResultDto.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
            // 退款交易记录ID
            refundOrderResultDto.setThirdFundNo(refundOrderResultDto.getRefundId());
            refundOrderResultDto.setBankNo(paymentTradeDO.getBank());
            refundOrderResultDto.setAppOrderId(paymentRefundDo.getAppOrderId());

            // 如果退款成功,通知退款申请，修改退款状态
            mqMessageBO = new MqMessageBO(topic, "", JSON.toJSONString(refundOrderResultDto));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        }

        return mqMessageBO;
    }


    @Override
    public Result<Void> refundByRefundNo(String refundNO) {
        List<PaymentRefundDO> paymentRefundDOList = paymentRefundService.selectRefundList(refundNO);

        if (CollectionUtil.isEmpty(paymentRefundDOList)) {
            return Result.success();
        }

        paymentRefundDOList = paymentRefundDOList.stream().filter(t -> RefundStateEnum.WAIT_REFUND == RefundStateEnum.getByCode(t.getRefundState())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(paymentRefundDOList)) {

            return Result.success();
        }
        PaymentRefundDO paymentRefundDo = paymentRefundDOList.stream().findFirst().get();
        MqMessageBO mqMessageBO =  _this.refundPrepare(paymentRefundDo);

        // 发送退款消息
        if (mqMessageBO != null) {
            mqMessageSendApi.send(mqMessageBO);
        }
        return Result.success();
    }

    @GlobalTransactional
    public MqMessageBO operationRefundCallBackPrepare(PaymentRefundDO paymentRefundDo,RefundCallBackRequest  refundCallBackRequest) {
        MqMessageBO mqMessageBO = null;
        List<PaymentMergeOrderDO> paymentMergeOrderDoList = paymentMergeOrderService.selectMergerOrderByPayId(paymentRefundDo.getPayId());
        if (CollectionUtil.isEmpty(paymentMergeOrderDoList)) {
            log.warn("{},该订单未查询到支付记录",paymentMergeOrderDoList);
            return null;
        }
        List<PaymentMergeOrderDO> mergeOrderList =  paymentMergeOrderDoList.stream().filter(
                e -> CompareUtil.compare(e.getPayAmount(),e.getRefundAmount()) > 0
                        && paymentRefundDo.getAppOrderId().equals(e.getAppOrderId())
        ).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(mergeOrderList)) {
            log.warn("{},该订单未查询到可以退款的支付订单",refundCallBackRequest.getPayNo());
            return null;
        }
        PaymentMergeOrderDO currentMergeOrder =  mergeOrderList.stream().filter(e -> paymentRefundDo.getPayNo().equals(e.getPayNo())).findFirst().get();
        if (currentMergeOrder == null) {
            log.warn("{},该订单未查询到可以退款的支付订单",refundCallBackRequest.getPayNo());
            return null;
        }
        // 退款通知topic
        String topic = OrderPlatformEnum.HMC == OrderPlatformEnum.getByCode(currentMergeOrder.getOrderPlatform()) ? Constants.TOPIC_HMC_PAY_REFUND_NOTIFY : Constants.TOPIC_ORDER_PAY_REFUND_NOTIFY;

        paymentRefundDo.setRefundState(refundCallBackRequest.getRefundStatus());
        paymentRefundDo.setErrorMessage(refundCallBackRequest.getErrorMessage());
        paymentRefundDo.setThirdFundNo(refundCallBackRequest.getThird_fund_no());
        paymentRefundDo.setRefundDate(refundCallBackRequest.getRefundDate());
        paymentRefundService.updatePaymentRefundStatus(paymentRefundDo);
        // 修改订单上的已退款金额
        if (RefundStateEnum.getByCode(refundCallBackRequest.getRefundStatus()) == RefundStateEnum.SUCCESS) {
            paymentMergeOrderService.updateMergeOrderFundAmount(currentMergeOrder.getId(),paymentRefundDo.getRefundAmount());
        }
        RefundOrderResultDTO refundOrderResultDto = new RefundOrderResultDTO();
        refundOrderResultDto.setRefundStateEnum(RefundStateEnum.getByCode(refundCallBackRequest.getRefundStatus()));
        if (1 == paymentRefundDo.getRefundType()) {
            refundOrderResultDto.setAppRefundId(paymentRefundDo.getRefundId());
            refundOrderResultDto.setRealReturnAmount(paymentRefundDo.getRefundAmount());
            refundOrderResultDto.setOpUserId(paymentRefundDo.getCreateUser());
            refundOrderResultDto.setTradeTypeEnum(TradeTypeEnum.getByCode(currentMergeOrder.getTradeType()));
            refundOrderResultDto.setOrderPlatform(OrderPlatformEnum.getByCode(currentMergeOrder.getOrderPlatform()));
            refundOrderResultDto.setAppOrderNo(currentMergeOrder.getAppOrderNo());
            refundOrderResultDto.setThirdTradeNo(refundCallBackRequest.getThirdTradeNo());
            refundOrderResultDto.setThirdFundNo(refundCallBackRequest.getThird_fund_no());
            refundOrderResultDto.setAppOrderId(paymentRefundDo.getAppOrderId());
            // 回传错误信息给退款订单信息表
            refundOrderResultDto.setErrorMessage(refundCallBackRequest.getErrorMessage());
            refundOrderResultDto.setBankNo("");

            // 如果退款成功,通知退款申请，修改退款状态
            mqMessageBO = new MqMessageBO(topic, "", JSON.toJSONString(refundOrderResultDto));
            mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        } else if (2 == paymentRefundDo.getRefundType()){
            // 修改重复退款的退款状态
            PaymentRepeatOrderDO repeatOrderDo = new PaymentRepeatOrderDO();
            repeatOrderDo.setId(paymentRefundDo.getRefundId());
            repeatOrderDo.setRefundState(refundOrderResultDto.getRefundStateEnum().getCode());
            repeatOrderDo.setThirdFundNo(refundOrderResultDto.getRefundId());
            repeatOrderDo.setRefundDate(new Date());
            repeatOrderDo.setErrorMessage(refundOrderResultDto.getErrorMessage());
            paymentRepeatOrderService.updateById(repeatOrderDo);
        }
        return  mqMessageBO;
    }

    @Override
    public Result<String> operationRefundCallBackThird(RefundCallBackRequest refundCallBackRequest) throws PayException {
        log.info("operationRefundCallBackThird start :{}", JSONUtil.toJsonStr(refundCallBackRequest));
        //获取锁ID
        String lockId = "";
        //锁名称
        String lockName = "un-online-refund-lock" + Constants.SEPARATOR_MIDDLELINE + refundCallBackRequest.getPayNo();
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);
            PaymentTradeDO paymentTradeDO =  paymentTradeService.selectPaymentTradeByPayNo(refundCallBackRequest.getPayNo());
            if (paymentTradeDO == null) {
                log.error("operationCallBackThird param :{}", JSONUtil.toJsonStr(refundCallBackRequest));
                throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
            }
            List<PaymentRefundDO> refundDOList =  paymentRefundService.selectRefundList(refundCallBackRequest.getRefund_no());
            if (CollectionUtil.isEmpty(refundDOList)) {
                log.error("operationRefundCallBackThird param :{}", JSONUtil.toJsonStr(refundCallBackRequest));
                throw new BusinessException(PaymentErrorCode.REFUND_NOTIFY_ERROR);
            }
            List<PaymentRefundDO> refundIngList = refundDOList.stream().filter(t -> RefundStateEnum.REFUND_ING == RefundStateEnum.getByCode(t.getRefundState())
                    || RefundStateEnum.FALIUE == RefundStateEnum.getByCode(t.getRefundState())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(refundIngList)) {
                return Result.success();
            }
            MqMessageBO mqMessageBO =  _this.operationRefundCallBackPrepare(refundIngList.get(0),refundCallBackRequest);
            if (mqMessageBO != null) {
                mqMessageSendApi.send(mqMessageBO);
            }
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return Result.success();
    }


    /**
     * 查询超时未退款监控记录
     * @return
     */
    @Override
    public List<String> selectTimeOutRefundOrderList() {
        List<PaymentRefundDO>  paymentRefundDOList = paymentRefundService.selectTimeOutRefundOrderList();
        if (CollectionUtil.isEmpty(paymentRefundDOList)) {

            return Collections.emptyList();
        }
        return paymentRefundDOList.stream().map(t -> t.getRefundNo()).collect(Collectors.toList());
    }
}
