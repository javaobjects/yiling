package com.yiling.payment.pay.ai.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.TradeContentJsonDTO;
import com.yiling.payment.channel.service.dto.request.ClosePayOrderRequest;
import com.yiling.payment.channel.service.dto.request.CreatePayRequest;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.channel.service.dto.request.RetryRequest;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.payment.pay.dto.request.InsertTradeLogRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.entity.PaymentTradeLogDO;
import com.yiling.payment.pay.event.PayCallbackEvent;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentTradeLogService;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 在线支付
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.ai.impl
 * @date: 2021/10/20
 */
@DubboService
@Slf4j
@RefreshScope
public class PayApiImpl implements PayApi,ApplicationEventPublisherAware {
    private ApplicationEventPublisher       applicationEventPublisher;
    @Autowired
    private PayFactory                      payFactory;
    @Autowired
    private PaymentMergeOrderService        paymentMergeOrderService;
    @Autowired
    private PaymentTradeService             paymentTradeService;
    @Autowired
    private PaymentTradeLogService          paymentTradeLogService;
    @Autowired
    private RedisDistributedLock            redisDistributedLock;
    @DubboReference
    MqMessageSendApi                        mqMessageSendApi;
    @Lazy
    @Autowired
    private PayApiImpl                      _this;
    @Value("${spring.profiles.active}")
    private String                          env;
    /*测试环境支付金额*/
    @Value("${payment.test.money:0.01}")
    private String                          testPayMoney;
    @Autowired
    private RedisService                    redisService;

    private final String                    redis_check_lock = "online-order-lock" + Constants.SEPARATOR_MIDDLELINE ;

    /***redis订单状态**/
    private final String                    redis_order_status = RedisKey.generate("payment", "order", "payId");

    @Value("${payment.retry.delayLevels:'2,3,4,5,14,15,16,17'}")
    private String                          delayLevels;

    @Value("${payment.retry.count:6}")
    private Integer                         retryCount;

    @Value("${payment.retry.initLevel:2}")
    private Integer                         queryInitLevel;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {

        this.applicationEventPublisher = applicationEventPublisher;
    }


    /**
     *  校验支付订单是否重复支付
     * @param tradeType 支付类型
     * @param mergeOrderDOList 预支付订单集合
     * @return 校验是否通过
     */
    private Result<String> checkPayOrderRequest(TradeTypeEnum tradeType,List<PaymentMergeOrderDO> mergeOrderDOList) {

        if (CollectionUtil.isEmpty(mergeOrderDOList)) {

            return Result.success();
        }

        List<PaymentMergeOrderDO> successList = mergeOrderDOList.stream().filter(e -> AppOrderStatusEnum.getByCode(e.getAppOrderStatus()) == AppOrderStatusEnum.SUCCESS).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(successList)) {

            return Result.success();
        }
        List<String> successOrderNOs = successList.stream().map(PaymentMergeOrderDO::getAppOrderNo).collect(Collectors.toList());

        String errorMessage;
        switch (tradeType) {
            case BACK: errorMessage = StringUtils.join(successOrderNOs,",") + "已还款，请勿勾选！"; break;
            default: errorMessage = PaymentErrorCode.ORDER_PAID_ERROR.getMessage(); break;
        }

        return Result.failed(errorMessage);
    }


    @Override
    public Result<String> createPayOrder(CreatePayOrderRequest request) {

        return this.createPayOrder(OrderPlatformEnum.B2B,request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> createPayOrder(OrderPlatformEnum orderPlatform,CreatePayOrderRequest request) {
        if (log.isDebugEnabled()) {
            log.debug("..createPayOrder...start-> {}",JSON.toJSON(request));
        }
        Assert.notEmpty(request.getAppOrderList(),"支付订单不能为空");
        Assert.notNull(request.getTradeType(),"支付类型不能为空!");
        Assert.notNull(orderPlatform,"支付平台不能为空");

        List<String> appOrderNoList = request.getAppOrderList().stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());

        // 过滤对应平台的支付订单
        List<PaymentMergeOrderDO> mergeOrderDOList =  paymentMergeOrderService.selectMergerOrderByOrderNoList(orderPlatform.getCode(),request.getTradeType().getCode(),appOrderNoList);

        Result<String> checkResult = this.checkPayOrderRequest(request.getTradeType(),mergeOrderDOList);
        if (HttpStatus.HTTP_OK != checkResult.getCode()) {

            return checkResult;
        }

        List<String> unPayMergeId = mergeOrderDOList.stream().filter(
                e -> AppOrderStatusEnum.CLOSE != AppOrderStatusEnum.getByCode(e.getAppOrderStatus())
        ).map(t -> t.getPayId()).distinct().collect(Collectors.toList());

        // 交易记录已经存在，并且是唯一，没有关闭,返回在线交易ID
        if (CollectionUtil.isNotEmpty(unPayMergeId) && unPayMergeId.size() == 1) {
            List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectMergerOrderByPayId(unPayMergeId.get(0));
            if (resultList.size() == request.getAppOrderList().size()) {
                return Result.success(unPayMergeId.stream().findFirst().get());
            }
        }

        List<PaymentMergeOrderDO> paymentMergeOrderDoList =
        request.getAppOrderList().stream().map(e -> {
            PaymentMergeOrderDO mergeOrderDO = new PaymentMergeOrderDO();
            mergeOrderDO.setAppOrderId(e.getAppOrderId());
            mergeOrderDO.setTradeType(request.getTradeType().getCode());
            mergeOrderDO.setAppOrderNo(e.getAppOrderNo());
            mergeOrderDO.setPayAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal(testPayMoney) : e.getAmount());
            mergeOrderDO.setGoodsAmount(e.getAmount());
            mergeOrderDO.setBuyerEid(e.getBuyerEid());
            mergeOrderDO.setSellerEid(e.getSellerEid());
            mergeOrderDO.setContent(request.getContent());
            mergeOrderDO.setUserId(e.getUserId());
            mergeOrderDO.setLimitTime(DateUtil.offsetMinute(new Date(),10));
            mergeOrderDO.setAppOrderStatus(AppOrderStatusEnum.WAIT_PAY.getCode());
            mergeOrderDO.setRefundState(1);
            mergeOrderDO.setCreateUser(request.getOpUserId());
            mergeOrderDO.setOrderPlatform(orderPlatform.getCode());
            return mergeOrderDO;
        }).collect(Collectors.toList());

        Result<String> result =  paymentMergeOrderService.insertPaymentMergeOrder(paymentMergeOrderDoList);

        // 延迟10分钟，自动取消临时交易订单
        if (HttpStatus.HTTP_OK == result.getCode()) {
            // 发送延迟消息
            MqMessageBO payMessageBo = new MqMessageBO(Constants.TOPIC_PAY_ORDER_EXPIRED,"",result.getData(), MqDelayLevel.TEN_MINUTES);
            payMessageBo = mqMessageSendApi.prepare(payMessageBo);
            mqMessageSendApi.send(payMessageBo);
        }

        return result;
    }
    /**
     * 构建交易流水数据
     * @param payWay
     * @param paySource
     * @param payId
     * @param payAmount
     * @return
     */
    private PaymentTradeDO buildPaymentTrade(String payWay,String paySource,String tradeSource,String payIp,String payId,BigDecimal payAmount,Long userId) {
        PaymentTradeDO tradeDO = new PaymentTradeDO();
        tradeDO.setPayId(payId);
        tradeDO.setPayAmount(payAmount);
        tradeDO.setTradeDate(new Date());
        tradeDO.setPayWay(payWay);
        tradeDO.setPaySource(paySource);
        tradeDO.setTradeSource(tradeSource);
        tradeDO.setPayIp(payIp);
        tradeDO.setTradeStatus(TradeStatusEnum.WAIT_PAY.getCode());
        tradeDO.setCreateUser(userId);
        return tradeDO;
    }


    /**
     * 关闭支付中第三方远程支付交易记录
     * @param paymentTradeIngList 关闭订单
     */
    @Async("asyncExecutor")
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = Exception.class)
    public void closePaymentTradeIngList(List<PaymentTradeDO> paymentTradeIngList) {

        if (CollectionUtil.isEmpty(paymentTradeIngList)) {
            return;
        }

        for (PaymentTradeDO trade: paymentTradeIngList) {
            // 易宝交易商户号
            String merchantNo = trade.getContent() != null ? JSON.parseObject(trade.getContent(), TradeContentJsonDTO.class).getMerchantNo() : null ;
            PayService payService = payFactory.getPayInstance(trade.getPayWay(),trade.getPaySource());
            // 关闭第三方支付交易记录
            Result<Void> result = payService.closeOrder(ClosePayOrderRequest.builder().payNo(trade.getPayNo()).thirdTradeNo(trade.getThirdTradeNo()).merchantNo(merchantNo).build());
            if (HttpStatus.HTTP_OK != result.getCode()) {
               log.warn("取消订单异常:{}",result.getMessage());
               continue;
            }

            PaymentTradeDO updateTrade = new PaymentTradeDO();
            updateTrade.setPayNo(trade.getPayNo());
            updateTrade.setTradeStatus(TradeStatusEnum.CLOSE.getCode());
            paymentTradeService.updatePaymentTradeStatus(updateTrade);
        }
    }


    /**
     * 校验redis中支付记录的状态,无效请求拦截
     * @param payId
     * @return
     */
    private Result checkOrderRedisStatus(String payId) {
        Object appOrderStatus = redisService.get(redis_order_status + RedisKey.SYMBOL + payId);
        // 校验缓存中订单支付状态,防止无效请求
        if (ObjectUtil.isNotNull(appOrderStatus)) {
            AppOrderStatusEnum appOrderStatusEnum  = AppOrderStatusEnum.getByCode(Integer.valueOf(appOrderStatus.toString()));
            switch (appOrderStatusEnum) {
                case SUCCESS :
                    return Result.failed(PaymentErrorCode.ORDER_PAID_ERROR);
                case CLOSE:
                    return Result.failed(PaymentErrorCode.PAYMENT_ORDER_PAID_CANCEL);
                default: break;
            }
        }
        return Result.success();
    }


    /**
     * 设置订单redis支付状态(缓存2小时)
     * @param payId
     * @param appOrderStatusEnum
     */
    private void setOrderRedisStatus(String payId,AppOrderStatusEnum appOrderStatusEnum) {
        // 设置缓存支付结果
        redisService.set(redis_order_status + RedisKey.SYMBOL + payId,appOrderStatusEnum.getCode(),60 * 60 * 2);
    }


    /**
     * 创建支付交易记录事务入库部分
     * @param resultList
     * @param payParamRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result<Map<String,Object>> createPayTradeTx(List<PaymentMergeOrderDO> resultList,CreatePayTradeRequest payParamRequest) {

        // 支付支付中的交易记录
        List<PaymentTradeDO> paymentTradeIngList = paymentTradeService.selectPayIngTradeByPayId(payParamRequest.getPayId());
        BigDecimal amount = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        PaymentTradeDO tradeDo = this.buildPaymentTrade(payParamRequest.getPayWay(),payParamRequest.getPaySource(),payParamRequest.getTradeSource(),payParamRequest.getUserIp(),payParamRequest.getPayId(),amount,payParamRequest.getOpUserId());

        // 创建支付交易流水
        Result<String> insertTradeResult = paymentTradeService.insertPaymentTrade(tradeDo);

        if (HttpStatus.HTTP_OK != insertTradeResult.getCode()) {

            return Result.failed("创建支付流水数据失败!");
        }
        // 如果存在支付中的交易记录，需要关闭第三方支付交易
        _this.closePaymentTradeIngList(paymentTradeIngList);

        CreatePayRequest createPayRequest = new CreatePayRequest();
        createPayRequest.setPayId(payParamRequest.getPayId());
        createPayRequest.setAmount(amount);
        createPayRequest.setPayWay(payParamRequest.getPayWay());
        createPayRequest.setPaySource(payParamRequest.getPaySource());
        createPayRequest.setPayNo(insertTradeResult.getData());
        createPayRequest.setTradeTypeEnum(TradeTypeEnum.getByCode(resultList.get(0).getTradeType()));
        createPayRequest.setOrderPlatformEnum(OrderPlatformEnum.getByCode(resultList.get(0).getOrderPlatform()));
        createPayRequest.setUserId(resultList.get(0).getUserId());
        createPayRequest.setUserIp(payParamRequest.getUserIp());
        createPayRequest.setOpenId(payParamRequest.getOpenId());
        createPayRequest.setRedirectUrl(payParamRequest.getRedirectUrl());
        createPayRequest.setAppId(payParamRequest.getAppId());
        // 对账备注
        String remark = resultList.stream().map(t -> t.getAppOrderNo()).collect(Collectors.joining(","));
        createPayRequest.setRemark(remark);
        PayService payService = payFactory.getPayInstance(payParamRequest.getPayWay(),payParamRequest.getPaySource());
        // 创建第三方支付申请
        Result<Map<String,Object>> insertPayDataResult =  payService.payData(createPayRequest);

        // 交易失败取消，交易流水记录
        if (HttpStatus.HTTP_OK != insertTradeResult.getCode()) {
            tradeDo.setTradeStatus(TradeStatusEnum.CLOSE.getCode());
            tradeDo.setErrorMessage(insertPayDataResult.getMessage());
        }

        Date paymentTimes = (Date)insertPayDataResult.getData().get("paymentTimes");
        Object thirdTradeNo = insertPayDataResult.getData().get("thirdTradeNo");
        tradeDo.setLimitTime(paymentTimes);
        Object content = insertPayDataResult.getData().get("content");
        Object bank = insertPayDataResult.getData().get("bankOrderId");

        tradeDo.setContent(content != null ? content.toString() : "");
        String merchantNo = content != null ? JSON.parseObject(tradeDo.getContent(), TradeContentJsonDTO.class).getMerchantNo() : null ;

        Optional.ofNullable(thirdTradeNo).ifPresent(e ->  tradeDo.setThirdTradeNo(thirdTradeNo.toString()));
        // 银行信息
        Optional.ofNullable(bank).ifPresent(e ->  tradeDo.setBank(bank.toString()));

        // 修改预交易订单信息，过期时间
        this.paymentMergeOrderService.updateMergeOrderAppOrderStatus(payParamRequest.getPayId(),AppOrderStatusEnum.PAY_ING.getCode(),paymentTimes,createPayRequest.getPayNo());
        // 完善交易申请信息
        this.paymentTradeService.updatePaymentTradeStatus(tradeDo);

        // 订单查询执行任务
        this.parkQueryTask(tradeDo.getPayNo(),thirdTradeNo.toString(),merchantNo);

        if (log.isDebugEnabled()) {
            log.debug("createPayOrder end :{}",JSONUtil.toJsonStr(insertPayDataResult));
        }

        Object contentResult = insertPayDataResult.getData().get("content");
        // 去除敏感信息
        if (contentResult != null) {
            insertPayDataResult.getData().remove("content");
        }
        return insertPayDataResult;
    }

    /**
     * 创建支付交易申请记录
     * @param payParamRequest 交易参数
     * @return 返回请求第三方支付所需参数
     */
    @Override
    public Result<Map<String,Object>> createPayTrade(CreatePayTradeRequest payParamRequest) {
        if (log.isDebugEnabled()) {
            log.debug("createPayOrder start :{}",JSONUtil.toJsonStr(payParamRequest));
        }
        Assert.notEmpty(payParamRequest.getPayId(),"支付订单号不能为空");
        Assert.notNull(payParamRequest.getPayWay(),"支付方式不能为空!");
        String payId = payParamRequest.getPayId();

        Result<Map<String,Object>> checkStatus = this.checkOrderRedisStatus(payId);

        if (HttpStatus.HTTP_OK != checkStatus.getCode()) {

            return checkStatus;
        }

        List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectMergerOrderByPayId(payId);
        if (CollectionUtil.isEmpty(resultList)) {
            return Result.failed("当前支付订单交易记录不存在!");
        }

        boolean isSuccess = resultList.stream().anyMatch(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus()));
        if (isSuccess) {
            return Result.failed(PaymentErrorCode.ORDER_PAID_ERROR);
        }

        boolean isClosed = resultList.stream().anyMatch(e -> AppOrderStatusEnum.CLOSE == AppOrderStatusEnum.getByCode(e.getAppOrderStatus()));
        if (isClosed) {

            return Result.failed(PaymentErrorCode.PAYMENT_ORDER_PAID_CANCEL);
        }

        PaymentMergeOrderDO paymentOrderDo = resultList.stream().findFirst().get();
        String lockName = this.redis_check_lock + paymentOrderDo.getId();
        String lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);

        try {
            return  _this.createPayTradeTx(resultList,payParamRequest);

        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    /**
     * 执行查询订单延迟任务
     * @param payNo 商家支付单号
     * @param thirdTradeNo 第三方交易单号
     */
    private void parkQueryTask(String payNo, String thirdTradeNo,String merchantNo) {
        RetryRequest<QueryPayOrderRequest> retryMessage = new RetryRequest<>();

        QueryPayOrderRequest queryPayOrderRequest = new QueryPayOrderRequest();
        queryPayOrderRequest.setPayNo(payNo);
        queryPayOrderRequest.setThirdTradeNo(thirdTradeNo);
        queryPayOrderRequest.setMerchantNo(merchantNo);
        retryMessage.setTotalCount(retryCount);
        retryMessage.setCurrentCount(1);
        retryMessage.setDelayLevels(delayLevels);
        retryMessage.setTopic(Constants.TOPIC_PAY_ORDER_QUERY);
        retryMessage.setData(queryPayOrderRequest);

        MqDelayLevel delayLevel = Optional.ofNullable(MqDelayLevel.getByLevel(queryInitLevel)).orElse(MqDelayLevel.FIVE_SECONDS);

        // 发送延迟消息,查询订单的状态
        MqMessageBO payMessageBo = new MqMessageBO(retryMessage.getTopic(),"",JSONObject.toJSONString(retryMessage), delayLevel);

        payMessageBo = mqMessageSendApi.prepare(payMessageBo);

        mqMessageSendApi.send(payMessageBo);

    }

    @Override
    public Boolean insertOperationCallBackLog(InsertTradeLogRequest insertTradeLogRequest) {

        PaymentTradeLogDO paymentTradeLogDo = PojoUtils.map(insertTradeLogRequest,PaymentTradeLogDO.class);
        return paymentTradeLogService.save(paymentTradeLogDo);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<PayOrderDTO> payCallback(PayCallBackRequest callBackRequest) {
        PaymentTradeDO paymentTradeDO =  paymentTradeService.selectPaymentTradeByPayNo(callBackRequest.getPayNo());
        if (paymentTradeDO == null) {
            log.error("operationCallBackThird param :{}", JSONUtil.toJsonStr(callBackRequest));
            throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
        }
        if (TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {

            return Collections.emptyList();
        }
        // 如果交易取消,添加错误提示
        if (TradeStatusEnum.CLOSE == TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {
            log.error("operationCallBackThird close param :{}", JSONUtil.toJsonStr(callBackRequest));
        }
        List<PaymentMergeOrderDO> paymentMergeOrderDOList = paymentMergeOrderService.selectMergerOrderByPayId(paymentTradeDO.getPayId());
        if (CollectionUtil.isEmpty(paymentMergeOrderDOList)) {
            log.error("operationCallBackThird param :{}", JSONUtil.toJsonStr(callBackRequest));
            throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
        }
        // 查询是否存在支付成功的交易流水记录，重复支付
        List<PaymentTradeDO> successList = paymentTradeService.selectFinishPaymentTradeByPayId(paymentTradeDO.getPayId());
        PaymentTradeDO updatePaymentTrade = new PaymentTradeDO();
        updatePaymentTrade.setId(paymentTradeDO.getId());
        updatePaymentTrade.setThirdTradeNo(StringUtils.isNotBlank(callBackRequest.getThirdId()) ? callBackRequest.getThirdId() : paymentTradeDO.getThirdTradeNo() );
        updatePaymentTrade.setErrorMessage(callBackRequest.getErrorMessage());
        updatePaymentTrade.setTradeStatus(callBackRequest.getTradeState());
        updatePaymentTrade.setThirdStatus(callBackRequest.getThirdState());
        updatePaymentTrade.setTradeDate(Optional.ofNullable(callBackRequest.getTradeDate()).orElse(new Date()));
        updatePaymentTrade.setBank(callBackRequest.getBank());
        paymentTradeService.updateById(updatePaymentTrade);

        // 表示订单存在其他支付交易记录，已支付成功
        if (CollectionUtil.isNotEmpty(successList)) {
            List<PayOrderDTO> payOrderDTOList =
                    paymentMergeOrderDOList.stream().map(t -> {
                        PayOrderDTO orderDTO = PojoUtils.map(t,PayOrderDTO.class);
                        orderDTO.setPayNo(paymentTradeDO.getPayNo());
                        return orderDTO;
                    }).collect(Collectors.toList());
            this.applicationEventPublisher.publishEvent(new PayCallbackEvent(this,payOrderDTOList,true));
            return Collections.emptyList();
        }
        // 关闭支付订单
        if (TradeStatusEnum.CLOSE == TradeStatusEnum.getByCode(callBackRequest.getTradeState())) {
            PaymentMergeOrderDO payOrderDo = new PaymentMergeOrderDO();
            payOrderDo.setPayId(paymentTradeDO.getPayId());
            payOrderDo.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode());
            payOrderDo.setPayNo(paymentTradeDO.getPayNo());
            payOrderDo.setPayWay(paymentTradeDO.getPayWay());
            payOrderDo.setPaySource(paymentTradeDO.getPaySource());
            this.paymentMergeOrderService.updateMergeOrderByPayId(payOrderDo);
        }
        List<PayOrderDTO> payOrderDTOList = Lists.newArrayList();
        // 如果订单支付成功
        if (TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(callBackRequest.getTradeState())) {
            // 更改订单支付状态
            PaymentMergeOrderDO payOrderDo = new PaymentMergeOrderDO();
            payOrderDo.setPayId(paymentTradeDO.getPayId());
            payOrderDo.setAppOrderStatus(AppOrderStatusEnum.SUCCESS.getCode());
            payOrderDo.setPayNo(paymentTradeDO.getPayNo());
            payOrderDo.setPayWay(paymentTradeDO.getPayWay());
            payOrderDo.setPaySource(paymentTradeDO.getPaySource());
            payOrderDo.setPayDate(Optional.ofNullable(callBackRequest.getTradeDate()).orElse(new Date()));
            this.paymentMergeOrderService.updateMergeOrderByPayId(payOrderDo);
            // 通知
            paymentMergeOrderDOList.stream().forEach(e -> {
                e.setAppOrderStatus(AppOrderStatusEnum.SUCCESS.getCode());
                e.setPayWay(paymentTradeDO.getPayWay());
                e.setPaySource(paymentTradeDO.getPaySource());
                PayOrderDTO payOrderDTO = PojoUtils.map(e,PayOrderDTO.class);
                payOrderDTO.setThirdTradeNo(updatePaymentTrade.getThirdTradeNo());
                payOrderDTO.setPayId(e.getPayId());
                payOrderDTO.setPayNo(paymentTradeDO.getPayNo());
                payOrderDTO.setPayDate(Optional.ofNullable(callBackRequest.getTradeDate()).orElse(new Date()));
                payOrderDTO.setBankNo(Optional.ofNullable(callBackRequest.getBank()).orElse(""));
                payOrderDTOList.add(payOrderDTO);
            });
            // 设置缓存支付结果
            this.setOrderRedisStatus(paymentTradeDO.getPayId(),AppOrderStatusEnum.SUCCESS);
        } else {
            paymentMergeOrderDOList.stream().forEach(t -> {
                PayOrderDTO payOrderDTO = PojoUtils.map(t,PayOrderDTO.class);
                payOrderDTO.setThirdTradeNo(updatePaymentTrade.getThirdTradeNo());
                // 通知订单支付
                payOrderDTOList.add(payOrderDTO);
            });
        }
        return payOrderDTOList;
    }

    @Override
    public Result<String> operationCallBackThird(PayCallBackRequest callBackRequest) throws PayException {
        log.info("operationCallBackThird start :{}", JSONUtil.toJsonStr(callBackRequest));
        //获取锁ID
        String lockId = "";
        //锁名称
        String lockName = this.redis_check_lock + callBackRequest.getPayNo();
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);
            List<PayOrderDTO> resultList  =  _this.payCallback(callBackRequest);
            if (CollectionUtil.isNotEmpty(resultList)) {
                // 通知支付回调监听，处理发送消息逻辑
                this.applicationEventPublisher.publishEvent(new PayCallbackEvent(this,resultList,false));
            }
            return Result.success();
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }


    @Override
    public Pair<Boolean,List<PayOrderDTO>> selectOrderByBankOrderId(String bankOrderId) {
        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByBank(bankOrderId);

        if (paymentTradeDO == null) {

            return  new Pair<>(false,ListUtil.empty());
        }

        List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectMergerOrderByPayNo(paymentTradeDO.getPayNo());
        List<PayOrderDTO> results =  PojoUtils.map(resultList,PayOrderDTO.class);

        return  new Pair<>(orderQueryTrade(paymentTradeDO).getData(),results);
    }

    /**
     * 查询支付交易状态
     * @param paymentTradeDO
     * @return
     */
    private Result<Boolean> orderQueryTrade(PaymentTradeDO paymentTradeDO) {

        if (TradeStatusEnum.SUCCESS ==  TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {

            return Result.success(true);
        }

        // 校验订单是否已经全部支付成功
        Result checkOrderRedisStatus = this.checkOrderRedisStatus(paymentTradeDO.getPayId());

        if (ObjectUtil.equal(PaymentErrorCode.ORDER_PAID_ERROR.getCode(),checkOrderRedisStatus.getCode())) {
            log.info(".orderQueryByPayNo...redis.. status .. success");

            return Result.success(true);
        }

        TradeContentJsonDTO tradeContentJsonDTO =  JSON.parseObject(paymentTradeDO.getContent(), TradeContentJsonDTO.class);
        PayService payService = payFactory.getPayInstance(paymentTradeDO.getPayWay(),paymentTradeDO.getPaySource());
        QueryPayOrderRequest orderRequest = new QueryPayOrderRequest();
        orderRequest.setPayNo(paymentTradeDO.getPayNo());
        orderRequest.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        orderRequest.setMerchantNo(tradeContentJsonDTO != null ? tradeContentJsonDTO.getMerchantNo() : null);
        // 调用第三方查询接口,查询订单是否支付成功
        PayOrderResultDTO result =  payService.orderQuery(orderRequest);

        if (!result.getIsSuccess()) {

            paymentTradeDO.setTradeStatus(TradeStatusEnum.BANK_ING.getCode());
            paymentTradeService.updatePaymentTradeStatus(paymentTradeDO);

            return  Result.success(result.getIsSuccess());
        }

        Integer tradeState = result.getIsSuccess() ? TradeStatusEnum.SUCCESS.getCode() : TradeStatusEnum.FALIUE.getCode();

        PayCallBackRequest callBackRequest = PayCallBackRequest
                .builder()
                .payNo(paymentTradeDO.getPayNo())
                .payWay(paymentTradeDO.getPayWay())
                .thirdId(result.getTradeNo())
                .tradeState(tradeState)
                .thirdState(result.getThirdState())
                .thirdPaySource(result.getPaySource())
                .tradeDate(result.getTradeDate())
                .build();

        // 通知订单支付成功
        this.operationCallBackThird(callBackRequest);

        return  Result.success(result.getIsSuccess());
    }

    @Override
    public Result<Boolean> orderQueryByPayNo(String payNo) {
        PaymentTradeDO paymentTradeDO =  paymentTradeService.selectPaymentTradeByPayNo(payNo);

        if (paymentTradeDO == null) {
            log.error("orderQuery param :{}", JSONUtil.toJsonStr(payNo));
            throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
        }

        return this.orderQueryTrade(paymentTradeDO);
    }


    @Override
    public Result<Boolean> orderQueryByOrderNo(OrderPlatformEnum orderPlatform,TradeTypeEnum tradeType, String orderNo) {
        // 过滤出当前平台的订单来源
        List<PaymentMergeOrderDO> paymentMergeOrderDOList = paymentMergeOrderService.selectMergerOrderByOrderNoList(orderPlatform.getCode(),tradeType.getCode(),ListUtil.toList(orderNo));

        if (CollectionUtil.isEmpty(paymentMergeOrderDOList)) {

            return Result.success(false);
        }

        Boolean success = paymentMergeOrderDOList.stream().anyMatch(t -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(t.getAppOrderStatus()));

        if (success) {
            return Result.success(true);
        }

        List<PaymentMergeOrderDO> payIngOrderList =  paymentMergeOrderDOList.stream().filter(t -> AppOrderStatusEnum.PAY_ING == AppOrderStatusEnum.getByCode(t.getAppOrderStatus())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(payIngOrderList)) {

            return Result.success(false);
        }

        // 查询订单中是否有成功
        Boolean isSuccess = payIngOrderList.stream().anyMatch(t -> this.orderQueryByPayNo(t.getPayNo()).getData());

        return Result.success(isSuccess);
    }

    @Override
    public Result<Boolean> orderQueryByOrderNo(TradeTypeEnum tradeType, String orderNo) {

        return this.orderQueryByOrderNo(OrderPlatformEnum.B2B,tradeType,orderNo);
    }

    @Override
    public Boolean selectAppOrderPayStatus(OrderPlatformEnum orderPlatform,TradeTypeEnum tradeTypeEnum,Long appOrderId) {
        List<PaymentMergeOrderDO> finishMergeList =  paymentMergeOrderService.selectFinishMergeOrderList(appOrderId,tradeTypeEnum.getCode());
        // 当前平台存在支付成功记录
        if (CollectionUtil.isNotEmpty(finishMergeList) && finishMergeList.stream().anyMatch(t -> orderPlatform == OrderPlatformEnum.getByCode(t.getOrderPlatform()))) {

            return  true;
        }

        return false;
    }

    @Override
    public List<PayOrderDTO> listPayOrdersByPayId(String payId) {
        List<PaymentMergeOrderDO> resultList =  paymentMergeOrderService.selectMergerOrderByPayId(payId);
        if (CollectionUtil.isEmpty(resultList)) {
            return ListUtil.empty();
        }
        List<PayOrderDTO> results =  resultList.stream().map(e -> {
            PayOrderDTO dto = PojoUtils.map(e,PayOrderDTO.class);
            if (StringUtils.isNotBlank(e.getPayNo())) {
                PaymentTradeDO tradeDo =  paymentTradeService.selectPaymentTradeByPayNo(e.getPayNo());
                Optional.ofNullable(tradeDo).ifPresent(t -> dto.setThirdTradeNo(tradeDo.getThirdTradeNo()));
            }
            return dto;
        }).collect(Collectors.toList());

        return results;
    }

    @Override
    public BigDecimal selectOrderAmountByPayId(String payId) {

        List<PaymentMergeOrderDO> resultList =  paymentMergeOrderService.selectMergerOrderByPayId(payId);

        if (CollectionUtil.isEmpty(resultList)) {

            return BigDecimal.ZERO;
        }

        return resultList.stream().map(t -> t.getPayAmount()).reduce(BigDecimal::add).get();
    }

    @Override
    public Result<Void> closeTimer(Integer limit) {

        List<PaymentTradeDO> paymentTradeDOS = paymentTradeService.selectWaitPaymentTrades(limit);

        if (CollectionUtil.isNotEmpty(paymentTradeDOS)) {

            paymentTradeDOS.forEach(t -> _this.closeTrade(t));
        }

        List<PaymentMergeOrderDO> waitPaymentTradeList =  paymentMergeOrderService.selectWaitPayOrderList();

        for (PaymentMergeOrderDO t : waitPaymentTradeList) {

            this.closePayOrder(t);
        }

        return Result.success();
    }


    @Override
    public Result<Void> cancelPayOrder(String payId) {
        List<PaymentTradeDO> paymentTradeDOList =  paymentTradeService.selectPayIngTradeByPayId(payId);

        // 校验是否发起了第三交易支付，如果发起了第三方交易支付，需要取消第三方交易记录
        if (CollectionUtil.isNotEmpty(paymentTradeDOList)) {

            paymentTradeDOList.forEach(PaymentTradeDO -> _this.closeTrade(PaymentTradeDO));

        } else {

            List<PaymentMergeOrderDO> resultList =  paymentMergeOrderService.selectMergerOrderByPayId(payId);

            if (CollectionUtil.isEmpty(resultList)) {

                return Result.failed(PaymentErrorCode.ORDER_NOT_EXISTS);
            }
            resultList = resultList.stream().filter(t -> AppOrderStatusEnum.WAIT_PAY == AppOrderStatusEnum.getByCode(t.getAppOrderStatus())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(resultList)) {

                return Result.success();
            }
            for (PaymentMergeOrderDO mergeOrderDO : resultList) {

                this.closePayOrder(mergeOrderDO);
            }
        }


        return Result.success();
    }

    /**
     * 关闭支付交易记录
     * @param t
     */
    public void closePayOrder(PaymentMergeOrderDO t) {

        paymentMergeOrderService.updateMergeOrderAppOrderStatus(t.getPayId(),AppOrderStatusEnum.CLOSE.getCode(),null,t.getPayNo());

        // 设置缓存支付结果
        this.setOrderRedisStatus(t.getPayId(),AppOrderStatusEnum.CLOSE);

        // 会员购买记录，取消通知会员模块
        if (TradeTypeEnum.MEMBER == TradeTypeEnum.getByCode(t.getTradeType())) {
            PayOrderDTO payOrderDTO = PojoUtils.map(t,PayOrderDTO.class);
            payOrderDTO.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode());

            this.applicationEventPublisher.publishEvent(new PayCallbackEvent(this,Collections.singletonList(payOrderDTO),false));
        }
    }

    /**
     * 定时器关闭支付交易记录
     * @param paymentTradeDO
     */
    @Transactional(rollbackFor = Exception.class)
    @Async("asyncExecutor")
    public void closeTrade(PaymentTradeDO paymentTradeDO) {

        PayService payService = payFactory.getPayInstance(paymentTradeDO.getPayWay(),paymentTradeDO.getPaySource());

        TradeContentJsonDTO tradeContentJsonDTO =  JSON.parseObject(paymentTradeDO.getContent(), TradeContentJsonDTO.class);
        String merchantNo = tradeContentJsonDTO != null ? tradeContentJsonDTO.getMerchantNo() : null ;

        QueryPayOrderRequest orderRequest = new QueryPayOrderRequest();
        orderRequest.setPayNo(paymentTradeDO.getPayNo());
        orderRequest.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        orderRequest.setMerchantNo(merchantNo);

        // 关闭交易之前查询一次交易状态，防止误关闭订单
        PayOrderResultDTO payOrderResultDTO = payService.orderQuery(orderRequest);

        if (payOrderResultDTO.getIsSuccess()) {

            Integer tradeState = payOrderResultDTO.getIsSuccess() ? TradeStatusEnum.SUCCESS.getCode() : TradeStatusEnum.FALIUE.getCode();
            PayCallBackRequest callBackRequest = PayCallBackRequest.builder().payNo(paymentTradeDO.getPayNo()).payWay(paymentTradeDO.getPayWay()).thirdId(payOrderResultDTO.getTradeNo()).tradeState(tradeState).thirdState(payOrderResultDTO.getThirdState()).thirdPaySource(payOrderResultDTO.getPaySource()).build();
            // 通知订单支付成功
            this.operationCallBackThird(callBackRequest);

        } else {

            // 关闭第三方交易订单
            payService.closeOrder(ClosePayOrderRequest.builder().payNo(paymentTradeDO.getPayNo()).thirdTradeNo(paymentTradeDO.getThirdTradeNo()).merchantNo(merchantNo).build());

            PaymentTradeDO updateTrade = new PaymentTradeDO();
            updateTrade.setPayNo(paymentTradeDO.getPayNo());
            updateTrade.setTradeStatus(TradeStatusEnum.CLOSE.getCode());
            paymentTradeService.updatePaymentTradeStatus(updateTrade);

            // 如果订单已支付,可能其他交易类型已经支付成功，无需关闭
            Result<Map<String,Object>> checkStatus  = this.checkOrderRedisStatus(paymentTradeDO.getPayId());

            if (HttpStatus.HTTP_OK != checkStatus.getCode()) {

                return;
            }

            List<PaymentMergeOrderDO> paymentMergeOrderDOList = paymentMergeOrderService.selectMergerOrderByPayId(paymentTradeDO.getPayId());
            if (CollectionUtil.isNotEmpty(paymentMergeOrderDOList) && paymentMergeOrderDOList.stream().anyMatch(t -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(t.getAppOrderStatus()))){

                return;
            }

            paymentMergeOrderService.updateMergeOrderAppOrderStatus(paymentTradeDO.getPayId(),AppOrderStatusEnum.CLOSE.getCode(),null,paymentTradeDO.getPayNo());
            // 设置缓存支付交易状态
            this.setOrderRedisStatus(paymentTradeDO.getPayId(),AppOrderStatusEnum.CLOSE);


            // 会员订单取消，通知会员模块
            if (paymentMergeOrderDOList.stream().anyMatch(t -> TradeTypeEnum.MEMBER == TradeTypeEnum.getByCode(t.getTradeType()))) {
                // 会员订单取消，通知会员模块
                List<PayOrderDTO> resultList = PojoUtils.map(paymentMergeOrderDOList,PayOrderDTO.class);
                resultList.forEach(t -> t.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode()));
                this.applicationEventPublisher.publishEvent(new PayCallbackEvent(this,resultList,false));
            }
        }

    }

}
