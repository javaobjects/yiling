package com.yiling.payment.pay.ai.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.TransferService;
import com.yiling.payment.channel.service.dto.PaymentSettlementDTO;
import com.yiling.payment.channel.service.dto.request.CreatePaymentRequest;
import com.yiling.payment.channel.yee.dto.QueryTransferOrderDTO;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.exception.PayException;
import com.yiling.payment.pay.api.PayTransferApi;
import com.yiling.payment.pay.dto.PaymentTransferResultDTO;
import com.yiling.payment.pay.dto.request.CreatePaymentTransferRequest;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.pay.entity.PaymentTransferRecordDO;
import com.yiling.payment.pay.service.PaymentTransferRecordService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * 企业打款
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.ai.impl
 * @date: 2021/11/19
 */
@DubboService
@Slf4j
public class PayTransferApiImpl implements PayTransferApi {
    @Autowired
    private PaymentTransferRecordService paymentTransferRecordService;
    @Autowired
    private PayFactory payFactory;
    @Autowired
    private RedisDistributedLock redisDistributedLock;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;
    @Lazy
    @Autowired
    PayTransferApiImpl _this;
    @Value("${spring.profiles.active}")
    private String env;

    @GlobalTransactional
    public MqMessageBO transferCallBackPrepare(PayCallBackRequest callBackRequest) {
        PaymentTransferRecordDO paymentTradeDO = paymentTransferRecordService.selectPaymentTransferRecordByPayNo(callBackRequest.getPayNo());
        if (paymentTradeDO == null || StringUtils.isBlank(paymentTradeDO.getPayNo())) {
            log.error("transferCallBack param :{}", JSONUtil.toJsonStr(callBackRequest));
            throw new BusinessException(PaymentErrorCode.ORDER_NOT_EXISTS);
        }
        // 表示打款记录已经成功,并且本次打款成功记录还是成功,就无需回调了
        if (TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())
                && TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(callBackRequest.getTradeState()) ) {
            return null;
        }
        PaymentTransferRecordDO recordDO = new PaymentTransferRecordDO();
        recordDO.setPayNo(callBackRequest.getPayNo());
        recordDO.setTradeStatus(callBackRequest.getTradeState());
        recordDO.setThirdTradeNo(callBackRequest.getThirdId());
        recordDO.setErrorMessage(callBackRequest.getErrorMessage());
        recordDO.setFeeCharge(callBackRequest.getFee());
        if (TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(callBackRequest.getTradeState())) {
            recordDO.setTradeDate(new Date());
        }
        // 更改订单支付状态
        paymentTransferRecordService.batchUpdateTransferRecordByPayNO(Collections.singletonList(recordDO));
        // 如果订单支付成功
        PaymentTransferResultDTO resultDTO = new PaymentTransferResultDTO();
        resultDTO.setTradeType(paymentTradeDO.getTradeType());
        resultDTO.setBusinessId(paymentTradeDO.getBusinessId());
        resultDTO.setPayNo(paymentTradeDO.getPayNo());
        resultDTO.setCreateStatus(TradeStatusEnum.SUCCESS == TradeStatusEnum.getByCode(callBackRequest.getTradeState()));
        resultDTO.setThirdTradeNo(callBackRequest.getThirdId());
        resultDTO.setTradeStatusEnum(TradeStatusEnum.getByCode(callBackRequest.getTradeState()));
        resultDTO.setErrMSg(callBackRequest.getErrorMessage());
        // 企业支付通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_TRANSFER_PAY_NOTIFY, "", JSON.toJSONString(resultDTO));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }

    /**
     * 创建企业打款
     *
     * @param transferRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Map<Long, String>> createPayTransfer(CreatePaymentTransferRequest transferRequest) {
        log.info("...createPayTransfer..{}", JSONUtil.toJsonStr(transferRequest));
        Assert.notEmpty(transferRequest.getTransferRequestList(), "打款信息不能为空!");
        List<Long> businessIds = transferRequest.getTransferRequestList().stream().map(CreatePaymentTransferRequest.PaymentTransferRequest::getBusinessId).collect(Collectors.toList());
        List<PaymentTransferRecordDO> transferIngList = paymentTransferRecordService.listTransferIngByBusinessId(businessIds, transferRequest.getTradeType());
        if (CollectionUtil.isNotEmpty(transferIngList)) {
            log.warn("..createPayTransfer..." + JSON.toJSONString(transferIngList));
            return Result.failed("存在打款中的订单,请勿重复打款!");
        }
        List<PaymentTransferRecordDO> transferRecordDOList = transferRequest.getTransferRequestList().stream().map(t -> {
            PaymentTransferRecordDO recordDO = PojoUtils.map(t, PaymentTransferRecordDO.class);
            recordDO.setTradeDate(new Date());
            recordDO.setTradeStatus(TradeStatusEnum.WAIT_PAY.getCode());
            recordDO.setPayAmount(Constants.DEBUG_ENV_LIST.contains(env) ? new BigDecimal("0.01") : t.getAmount());
            recordDO.setEid(t.getEid());
            recordDO.setSellerEid(t.getSellerEid());
            recordDO.setPayWay(transferRequest.getPayWay());
            recordDO.setTradeType(transferRequest.getTradeType());
            recordDO.setCreateUser(transferRequest.getOpUserId());
            return recordDO;
        }).collect(Collectors.toList());
        Boolean result = paymentTransferRecordService.batchInsertTransferRecord(transferRecordDOList);
        if (!result) {
            return Result.failed("创建打款记录失败!");
        }
        Map<Long, String> resultList = transferRecordDOList.stream().collect(Collectors.toMap(PaymentTransferRecordDO::getBusinessId, PaymentTransferRecordDO::getPayNo));
        log.info("...createPayTransfer..result{}", JSON.toJSON(resultList));
        return Result.success(resultList);
    }


    /**
     * 企业打款回调
     *
     * @param callBackRequest
     * @return
     * @throws PayException
     */
    @Override
    public Result<String> transferCallBack(PayCallBackRequest callBackRequest) throws PayException {
        log.info("transferCallBack start :{}", JSONUtil.toJsonStr(callBackRequest));
        //获取锁ID
        String lockId = "";
        //锁名称
        String lockName = "un-transfer-order-lock" + Constants.SEPARATOR_MIDDLELINE + callBackRequest.getPayNo();
        try {
            lockId = redisDistributedLock.lock2(lockName, 60, 120, TimeUnit.SECONDS);
            MqMessageBO mqMessageBO = _this.transferCallBackPrepare(callBackRequest);
            if (mqMessageBO == null) {
                return Result.success();
            }
            mqMessageSendApi.send(mqMessageBO);
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
        return Result.success();
    }

    /**
     * 企业打款补偿定时器
     *
     * @param payNo 打款单号
     * @return
     * @throws PayException
     */
    @Override
    public Result<String> transferQueryTimer(String payNo) throws PayException {
        PaymentTransferRecordDO transfer = paymentTransferRecordService.selectPaymentTransferRecordByPayNo(payNo);
        if (transfer == null || TradeStatusEnum.BANK_ING != TradeStatusEnum.getByCode(transfer.getTradeStatus())) {
            log.warn("transferRequestExecuteTimer,单号异常:{}", payNo);
            return Result.success();
        }
        TransferService transferInstance = payFactory.getTransferInstance(transfer.getPayWay(), "");
        QueryTransferOrderDTO transferOrderDTO = transferInstance.transferQuery(transfer.getPayNo());
        PayCallBackRequest request = PayCallBackRequest.builder().payNo(transfer.getPayNo()).tradeState(transferOrderDTO.getTradeStatus()).thirdState("").payWay(transfer.getPayWay()).thirdId(transferOrderDTO.getThirdTradeNo()).errorMessage(transferOrderDTO.getErrMsg()).fee(transferOrderDTO.getFee()).bank("").build();
        return this.transferCallBack(request);
    }


    /**
     * 定时发起打款请求
     *
     * @param payNo 打款单号
     * @return
     * @throws PayException
     */
    @Override
    public Result<Void> transferRequestExecuteTimer(String payNo) throws PayException {
        PaymentTransferRecordDO paymentTransferRecordDO = paymentTransferRecordService.selectPaymentTransferRecordByPayNo(payNo);
        if (paymentTransferRecordDO == null || TradeStatusEnum.WAIT_PAY != TradeStatusEnum.getByCode(paymentTransferRecordDO.getTradeStatus())) {
            log.warn("transferRequestExecuteTimer,单号异常:{}", payNo);
            return Result.success();
        }
        MqMessageBO mqMessageBO = this.transferRequestExecute(paymentTransferRecordDO);
        mqMessageSendApi.send(mqMessageBO);
        return Result.success();
    }


    /**
     * 生成打款记录，并生成通知消息
     *
     * @param paymentTransferRecordDo
     * @return
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    public MqMessageBO transferRequestExecute(PaymentTransferRecordDO paymentTransferRecordDo) {
        TransferService transferInstance = payFactory.getTransferInstance(paymentTransferRecordDo.getPayWay(), "");
        CreatePaymentRequest request = PojoUtils.map(paymentTransferRecordDo, CreatePaymentRequest.class);
        request.setAmount(paymentTransferRecordDo.getPayAmount());
        List<PaymentSettlementDTO> paymentSettlementDTOList = transferInstance.createPaymentTransfer(ListUtil.toList(request));
        List<PaymentTransferRecordDO> updateTransferRecord = paymentSettlementDTOList.stream().map(t -> {
            PaymentTransferRecordDO recordDO = new PaymentTransferRecordDO();
            recordDO.setPayNo(t.getPayNo());
            recordDO.setErrorMessage(t.getErrMSg());
            recordDO.setTradeStatus(t.getCreateStatus() ? TradeStatusEnum.BANK_ING.getCode() : TradeStatusEnum.CLOSE.getCode());
            if (t.getCreateStatus()) {
                recordDO.setTradeDate(new Date());
            }
            return recordDO;
        }).collect(Collectors.toList());
        // 修改交易订单状态
        log.info("...transferRequestExecute..result{}", JSON.toJSON(paymentSettlementDTOList));
        PaymentSettlementDTO callBackRequest = paymentSettlementDTOList.stream().findFirst().get();
        // 如果订单支付成功
        PaymentTransferResultDTO resultDTO = new PaymentTransferResultDTO();
        resultDTO.setTradeType(paymentTransferRecordDo.getTradeType());
        resultDTO.setBusinessId(paymentTransferRecordDo.getBusinessId());
        resultDTO.setPayNo(paymentTransferRecordDo.getPayNo());
        resultDTO.setCreateStatus(callBackRequest.getCreateStatus());
        resultDTO.setTradeStatusEnum(callBackRequest.getTradeStatusEnum());
        resultDTO.setErrMSg(callBackRequest.getErrMSg());
        paymentTransferRecordService.batchUpdateTransferRecordByPayNO(updateTransferRecord);

        // 企业支付通知
        MqMessageBO mqMessageBO = new MqMessageBO(Constants.TOPIC_TRANSFER_PAY_NOTIFY, "", JSON.toJSONString(resultDTO));
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);

        return mqMessageBO;
    }


    @Override
    public List<String> listTransferIngRecords(Integer limit) {
        List<PaymentTransferRecordDO> transferRecordDOList = paymentTransferRecordService.listTransferIngRecords(limit);
        if (CollectionUtil.isEmpty(transferRecordDOList)) {
            return Collections.emptyList();
        }
        return transferRecordDOList.stream().map(t -> t.getPayNo()).collect(Collectors.toList());
    }

    @Override
    public List<String> listWaitTransferRecords(Integer limit) {
        List<PaymentTransferRecordDO> transferRecordDOList = paymentTransferRecordService.listWaitTransferRecords(limit);
        if (CollectionUtil.isEmpty(transferRecordDOList)) {
            return Collections.emptyList();
        }
        return transferRecordDOList.stream().map(t -> t.getPayNo()).collect(Collectors.toList());
    }
}
