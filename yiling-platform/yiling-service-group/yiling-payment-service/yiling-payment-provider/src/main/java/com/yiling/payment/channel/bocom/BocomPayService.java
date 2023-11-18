package com.yiling.payment.channel.bocom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.bocom.api.DefaultBocomClient;
import com.bocom.api.utils.ApiUtils;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.payment.channel.bocom.dto.MPNG020701ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020702ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020703ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG020705ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG210001ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG210005ResponseV1;
import com.yiling.payment.channel.bocom.dto.MPNG210006ResponseV1;
import com.yiling.payment.channel.bocom.request.MPNG020701RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG020702RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG020703RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG020705RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG020705RequestV1.MPNG020705RequestV1Biz.ReqBody;
import com.yiling.payment.channel.bocom.request.MPNG020705RequestV1.MPNG020705RequestV1Biz.ReqHead;
import com.yiling.payment.channel.bocom.request.MPNG210001RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG210005RequestV1;
import com.yiling.payment.channel.bocom.request.MPNG210006RequestV1;
import com.yiling.payment.channel.service.dto.TradeContentJsonDTO;
import com.yiling.payment.channel.service.support.config.BocomPayConfig;
import com.yiling.payment.channel.yee.request.YeePayOrder;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.PaymentErrorCode;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 交通银行支付
 *
 * @author zhigang.guo
 * @date: 2023/5/8
 */
@Component
@Slf4j
public class BocomPayService {
    @Autowired
    private DefaultBocomClient bocomClient;

    @Autowired
    private BocomPayConfig bocomPayConfig;

    /**
     * 创建预订单信息
     *
     * @param order
     * @param paySourceEnum
     * @param <O>
     * @return
     */
    public <O extends YeePayOrder> Map<String, Object> createPreOrder(O order, PaySourceEnum paySourceEnum) {

        Date dateTime = new Date();
        MPNG210005RequestV1 request = new MPNG210005RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.CREATE_ORDER_URL_V1);
        MPNG210005RequestV1.MPNG210005RequestV1Biz bizContent = new MPNG210005RequestV1.MPNG210005RequestV1Biz();
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqHead reqHead = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("1.0");
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody reqBody = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);

        List<MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RoyaltyInfo> royaltyInfo = new ArrayList<MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RoyaltyInfo>();
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RoyaltyInfo royalty_info = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RoyaltyInfo();
        royaltyInfo.add(royalty_info);
        reqBody.setRoyaltyInfo(royaltyInfo);
        royalty_info.setAmount("");
        royalty_info.setSerialNo("");
        reqBody.setValidPeriod(DateUtil.format(order.getExpirationTime(), DatePattern.PURE_DATETIME_PATTERN));

        // 目前使用小程序支付
        if (ObjectUtil.equal(PaySourceEnum.BOCOM_PAY_ALIPAY, paySourceEnum)) {
            reqBody.setTranScene("B2C-H5-ALIPAY");
        } else {
            reqBody.setTranScene("B2C-MINIPROGRAM-WECHAT");
        }

        reqBody.setIp(order.getUserIp());
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
        reqBody.setMerTradeTime(DateUtil.format(dateTime, DatePattern.PURE_TIME_PATTERN));
        reqBody.setNotifyUrl(bocomPayConfig.getAppId() + "@" + bocomPayConfig.getNotifyUrl());
        reqBody.setMerTradeDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));
        // reqBody.setPartnerId("partner_id");
        reqBody.setPayMerTranNo(order.getOutTradeNo());
        reqBody.setMerMemo(order.getAddition());
        reqBody.setSubAppId(order.getAppId());
        reqBody.setTotalAmount(order.getPrice().toString());
        reqBody.setSubOpenId(order.getOpenId());

        List<MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields> requireFields = new ArrayList<MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields>();
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields bank_tran_no = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields();
        bank_tran_no.setRequireField("bank_tran_no");
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields third_party = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields();
        third_party.setRequireField("third_party");
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields third_party_tran_no = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields();
        third_party_tran_no.setRequireField("third_party_tran_no");
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields open_id = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields();
        open_id.setRequireField("open_id");
        MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields sub_openid = new MPNG210005RequestV1.MPNG210005RequestV1Biz.ReqBody.RequireFields();
        sub_openid.setRequireField("sub_openid");

        requireFields.add(bank_tran_no);
        requireFields.add(third_party);
        requireFields.add(third_party_tran_no);
        requireFields.add(open_id);
        requireFields.add(sub_openid);

        reqBody.setRequireFields(requireFields);
        reqBody.setLocation("ONLINE");
        reqBody.setCurrency("CNY");
        reqBody.setJumpUrl(order.getRedirectUrl());
        reqBody.setTranContent(order.getSubject());
        // reqBody.setNoDsctAmount("50.0");
        // reqBody.setDisablePayChannels("credit");
        reqBody.setShopId("");
        request.setBizContent(bizContent);
        MPNG210005ResponseV1 response;


        long start = System.currentTimeMillis();

        try {
            response = bocomClient.execute(request, IdUtil.fastSimpleUUID());
        } catch (Exception exception) {
            log.error("交行下单失败：{}", exception.getMessage());
            throw new BusinessException(PaymentErrorCode.BOCOM_CREATE_ORDER_FAIL);
        }

        log.info("订单:{},交行预支付数据耗时:{}", order.getOutTradeNo(),(System.currentTimeMillis() - start) / 1000);
        log.info("...交行创建预定订单返回报文:{}", JSON.toJSONString(response));

        if (!response.isSuccess()) {
            log.error("交行下单失败，原因:{}", response.getRspMsg());
            throw new BusinessException(PaymentErrorCode.BOCOM_CREATE_ORDER_FAIL);
        }

        if (BocomPayConstants.FAILED.equals(response.getRspHead().getResponseStatus())) {
            log.error("交行下单失败，原因:{}", response.getRspHead().getResponseMsg());
            throw new BusinessException(PaymentErrorCode.BOCOM_CREATE_ORDER_FAIL);
        }

        Map<String, Object> result = MapUtil.newHashMap();
        //返回交行内部订单号
        result.put("thirdTradeNo", response.getRspBody().getSysOrderNo());
        TradeContentJsonDTO contentJsonDTO = new TradeContentJsonDTO();
        contentJsonDTO.setMerchantNo("");
        // 返回交易商户号
        result.put("content", JSON.toJSONString(contentJsonDTO));
        // 调用SDK信息
        result.put("tranPackage", response.getRspBody().getTranPackage());

        return result;
    }


    /**
     * 关闭订单
     *
     * @param assistOrder
     * @return
     */
    @SneakyThrows
    public MPNG020705ResponseV1 close(AssistOrder assistOrder) {

        Date dateTime = new Date();
        MPNG020705RequestV1 request = new MPNG020705RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.CLOSE_ORDER_URL_V1);

        MPNG020705RequestV1.MPNG020705RequestV1Biz bizContent = new MPNG020705RequestV1.MPNG020705RequestV1Biz();
        ReqHead reqHead = new ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("V-1.0");
        ReqBody reqBody = new ReqBody();
        bizContent.setReqBody(reqBody);
        reqBody.setMerTradeDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));

        //  reqBody.setPartnerId("12223666998512");
        reqBody.setPayMerTranNo(assistOrder.getOutTradeNo());
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
        reqBody.setCloseMerTranNo(assistOrder.getTradeNo());
        request.setBizContent(bizContent);

        MPNG020705ResponseV1 response = bocomClient.execute(request, IdUtil.fastSimpleUUID());

        if (log.isDebugEnabled()) {

            log.debug("预订单:{},关闭订单返回报文:{}", assistOrder.getOutTradeNo(),JSON.toJSONString(response));
        }

        return response;
    }


    /**
     * 查询订单
     *
     * @param assistOrder
     * @return
     */
    @SneakyThrows
    public MPNG020702ResponseV1 queryOrder(AssistOrder assistOrder) {

        Date dateTime = new Date();
        MPNG020702RequestV1 request = new MPNG020702RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.QUERY_ORDER_URL_V1);

        MPNG020702RequestV1.MPNG020702RequestV1Biz bizContent = new MPNG020702RequestV1.MPNG020702RequestV1Biz();
        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqHead reqHead = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("1.0");
        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody reqBody = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);
        reqBody.setMerTradeDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));
        //					reqBody.setPartnerId("ISV202103308895");
        reqBody.setPayMerTranNo(assistOrder.getOutTradeNo());
        reqBody.setTranScene("B2C-API-SCANCODE");

        List<MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields> require_fieldsList = new ArrayList<MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields>();
        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields require_fields = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields);
        require_fields.setRequireField("open_id");
        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields require_fields_2 = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields_2);
        require_fields_2.setRequireField("sub_openid");

        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields require_fields_3 = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields_3);
        require_fields_3.setRequireField("bank_tran_no");
        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields require_fields_4 = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields_4);
        require_fields_4.setRequireField("third_party_tran_no");

        MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields require_fields_5 = new MPNG020702RequestV1.MPNG020702RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields_5);
        require_fields_5.setRequireField("third_party");


        reqBody.setRequireFields(require_fieldsList);
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
        reqBody.setSysOrderNo(assistOrder.getTradeNo());
        request.setBizContent(bizContent);

        MPNG020702ResponseV1 response = bocomClient.execute(request, IdUtil.fastSimpleUUID());

        if (log.isDebugEnabled()) {

            log.debug("预订单:{},调用建行支付查询返回报文:{}",assistOrder.getOutTradeNo(),JSON.toJSONString(response));
        }

        return response;
    }

    /**
     * 申请退款
     *
     * @param refundOrder
     * @return
     */
    @SneakyThrows
    public MPNG020701ResponseV1 refund(RefundOrder refundOrder) {

        Date dateTime = new Date();

        MPNG020701RequestV1 request = new MPNG020701RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.REFUND_ORDER_URL_V1);

        MPNG020701RequestV1.MPNG020701RequestV1Biz bizContent = new MPNG020701RequestV1.MPNG020701RequestV1Biz();
        MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqHead reqHead = new MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(refundOrder.getOrderDate(), DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("1.0");
        MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqBody reqBody = new MPNG020701RequestV1.MPNG020701RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);
        reqBody.setAmount(refundOrder.getRefundAmount().toString());
        reqBody.setTranScene("B2C-API-SCANCODE");
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
        // reqBody.setNotifyUrl(bocomPayConfig.getNotifyUrl());
        reqBody.setMerTradeDate(DateUtil.format(refundOrder.getOrderDate(), DatePattern.PURE_DATE_PATTERN));
        //  reqBody.setShopId("");
        //   reqBody.setPartnerId("");
        reqBody.setPayMerTranNo(refundOrder.getOutTradeNo());
        // reqBody.setMerMemo(refundOrder.getDescription());
        reqBody.setRefundMerTranNo(refundOrder.getRefundNo());
        reqBody.setCurrency("CNY");
        reqBody.setMerRefundTime(DateUtil.format(dateTime, DatePattern.PURE_TIME_PATTERN));
        reqBody.setMerRefundDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));
        reqBody.setTranContent(refundOrder.getDescription());
        reqBody.setSysOrderNo(refundOrder.getTradeNo());
        request.setBizContent(bizContent);

        MPNG020701ResponseV1 response = bocomClient.execute(request, IdUtil.fastSimpleUUID());

        if (log.isDebugEnabled()) {

            log.debug("退款单:{},调用建行申请退款返回报文:{}",refundOrder.getRefundNo(),JSON.toJSONString(response));
        }

        return response;
    }

    /**
     * 查询退款
     *
     * @param refundOrder
     * @return
     */
    @SneakyThrows
    public MPNG020703ResponseV1 refundQuery(RefundOrder refundOrder) {

        Date dateTime = new Date();
        MPNG020703RequestV1 request = new MPNG020703RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.REFUND_QUERY_ORDER_URL_V1);

        MPNG020703RequestV1.MPNG020703RequestV1Biz bizContent = new MPNG020703RequestV1.MPNG020703RequestV1Biz();
        MPNG020703RequestV1.MPNG020703RequestV1Biz.ReqHead reqHead = new MPNG020703RequestV1.MPNG020703RequestV1Biz.ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("1.0");
        MPNG020703RequestV1.MPNG020703RequestV1Biz.ReqBody reqBody = new MPNG020703RequestV1.MPNG020703RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);
        //  reqBody.setPartnerId("000202102036258");
        reqBody.setTranScene("B2C-API-DISPLAYCODE");
        reqBody.setRefundMerTranNo(refundOrder.getRefundNo());
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
        reqBody.setMerRefundDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));
        reqBody.setSysOrderNo(refundOrder.getTradeNo());
        request.setBizContent(bizContent);
        MPNG020703ResponseV1 response = bocomClient.execute(request, IdUtil.fastSimpleUUID());

        if (log.isDebugEnabled()) {

            log.debug("退款单:{},调用建行申请退款查询返回报文:{}",refundOrder.getRefundNo(),JSON.toJSONString(response));
        }

        return response;
    }


    /**
     * 解密检行参数
     *
     * @param notifyJson
     * @return
     */
    public Map<String, Object> decrypt(String notifyJson) {
       /* PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz request = null;
        try {
            request = (PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz) ApiUtils.parseNotifyJsonWithBocomSign(notifyJson, "UTF-8", bocomPayConfig.getPublicKey(), PmmsMpngNotifyRequestV1.PmmsMpngNotifyRequestV1Biz.class);
        } catch (Exception xcp) {

            throw new RuntimeException("交通银行支付回调解析失败", xcp);
        }*/

        HashMap reqMap = null;

        try {
            reqMap = ApiUtils.parseCommunicationJsonWithBocomSign(notifyJson, bocomPayConfig.getPrivateKey(), bocomPayConfig.getPublicKey());
        } catch (Exception xcp) {

            throw new RuntimeException("交通银行支付回调解析失败", xcp);
        }

        if (log.isDebugEnabled()) {

            log.debug("交通银行支付回调");
        }

        return reqMap;
    }

    @SneakyThrows
    public MPNG210006ResponseV1 getOpenId() {

        Date dateTime = new Date();

        MPNG210006RequestV1 request = new MPNG210006RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.QUERY_OPEN_ID_URL_V1);

        MPNG210006RequestV1.MPNG210006RequestV1Biz bizContent = new MPNG210006RequestV1.MPNG210006RequestV1Biz();
        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqHead reqHead = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqHead();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("1");

        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody reqBody = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);
        reqBody.setValidPeriod("1");
        reqBody.setLatitude("1");
        reqBody.setScanCodeText("1");
        reqBody.setTerminalInfo("1");
        // reqBody.setPartnerId("1");
        reqBody.setMerMemo("1");

        List<MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.RequireFields> require_fieldsList = new ArrayList<MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.RequireFields>();
        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.RequireFields require_fields = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.RequireFields();
        require_fieldsList.add(require_fields);
        reqBody.setRequireFields(require_fieldsList);
        require_fields.setRequireField("1");
        reqBody.setCurrency("1");

        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.LocationInfo location_info = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.LocationInfo();
        reqBody.setLocationInfo(location_info);
        location_info.setTelecomNetworkId("1");
        location_info.setLbsSignal3("1");
        location_info.setLbsSignal1("1");
        location_info.setLbsSignal2("1");
        location_info.setLocationCd1("1");
        location_info.setIccId("1");
        location_info.setLocationCd3("1");
        location_info.setTelecomLbsSignal("1");
        location_info.setMobileCountryCd("1");
        location_info.setLocationCd2("1");
        location_info.setTelecomSystemId("1");
        location_info.setMobileNetworkNum("1");
        location_info.setLbsNum2("1");
        location_info.setTelecomLbs("1");
        location_info.setLbsNum1("1");
        location_info.setLbsNum3("1");
        reqBody.setLongitude("1");
        reqBody.setTermBatchNo("1");
        reqBody.setTranScene("1");
        reqBody.setIp("1");
        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.AddiTradeData addi_trade_data = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.AddiTradeData();
        reqBody.setAddiTradeData(addi_trade_data);
        addi_trade_data.setMethod("1");
        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.AddiTradeData.Value value = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.AddiTradeData.Value();
        addi_trade_data.setValue(value);
        value.setReverse1("1");
        value.setCardNo("1");
        value.setName("1");
        value.setDynamicTokenOutBizNo("1");
        value.setCardType("1");
        value.setReverse2("1");
        value.setReverse3("1");
        value.setReverse4("1");
        value.setReverse5("1");
        reqBody.setMerPtcId("1");
        MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.TermInfo term_info = new MPNG210006RequestV1.MPNG210006RequestV1Biz.ReqBody.TermInfo();

        reqBody.setTermInfo(term_info);
        term_info.setDeviceType("1");
        term_info.setSecretText("1");
        term_info.setAppVersion("1");
        term_info.setSerialNum("1");
        term_info.setEncryptRandNum("1");
        term_info.setNetworkLicense("1");
        reqBody.setMerTradeTime("1");
        reqBody.setTermPosNo("1");
        reqBody.setMerTradeDate("1");
        reqBody.setShopId("1");
        reqBody.setPayMerTranNo("1");
        reqBody.setTotalAmount("1");
        reqBody.setLocation("1");
        reqBody.setDetail("1");
        reqBody.setTranContent("1");
        request.setBizContent(bizContent);

        MPNG210006ResponseV1 response = bocomClient.execute(request,IdUtil.fastSimpleUUID());

        return response;
    }




    @SneakyThrows
    public <O extends YeePayOrder> MPNG210001ResponseV1 scanCodePay(O order, PaySourceEnum paySourceEnum) {

        MPNG210001RequestV1 request = new MPNG210001RequestV1();
        request.setServiceUrl(bocomPayConfig.getApiUrlAddress() + BocomPayConstants.SCAN_CODE_URL_V1);
        Date dateTime = new Date();

        MPNG210001RequestV1.MPNG210001RequestV1Biz bizContent = new MPNG210001RequestV1.MPNG210001RequestV1Biz();
        MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqHead reqHead = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqHead ();
        bizContent.setReqHead(reqHead);
        reqHead.setTransTime(DateUtil.format(dateTime, DatePattern.PURE_DATETIME_PATTERN));
        reqHead.setVersion("V-1.0");
        MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody reqBody = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody();
        bizContent.setReqBody(reqBody);
        reqBody.setValidPeriod(DateUtil.format(order.getExpirationTime(), DatePattern.PURE_DATETIME_PATTERN));
        reqBody.setLatitude("1");
        //  reqBody.setTerminalInfo("00000001");
        //   reqBody.setLocationId("102.13，112.09");
        // reqBody.setPartnerId("102365214532");
        //    reqBody.setJumpUrl("");
        // reqBody.setMerMemo("备注");

        // List<MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RequireFields> require_fieldsList = new ArrayList<MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RequireFields>();
        // MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RequireFields require_fields = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RequireFields();
        // require_fieldsList.add(require_fields);
        // reqBody.setRequireFields(require_fieldsList);
        // require_fields.setRequireField("1");
        reqBody.setDisablePayChannels("credit");
        reqBody.setCurrency("CNY");
      /*  MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.LocationInfo location_info = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.LocationInfo ();
        reqBody.setLocationInfo(location_info);
        location_info.setTelecomnetworkid("1");
        location_info.setLbssignal3("1");
        location_info.setLbssignal1("1");
        location_info.setLbssignal2("1");
        location_info.setLocationcd1("1");
        location_info.setIccid("1");
        location_info.setLocationcd3("1");
        location_info.setTelecomlbssignal("1");
        location_info.setMobilecountrycd("1");
        location_info.setLocationcd2("1");
        location_info.setTelecomsystemid("1");
        location_info.setMobilenetworknum("1");
        location_info.setLbsnum2("1");
        location_info.setTelecomlbs("1");
        location_info.setLbsnum1("1");
        location_info.setLbsnum3("1");*/
        //  reqBody.setLongitude("1");

        List<MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RoyaltyInfo> royalty_infoList = new ArrayList<MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RoyaltyInfo>();
        /*MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RoyaltyInfo royalty_info = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.RoyaltyInfo();
        royalty_infoList.add(royalty_info);
        reqBody.setRoyaltyInfo(royalty_infoList);
        royalty_info.setAmount("1");
        royalty_info.setSerialNo("1");*/
        reqBody.setTranScene("B2C-API-DISPLAYCODE");
        reqBody.setIp(order.getUserIp());
        reqBody.setMerPtcId(bocomPayConfig.getMerPtcId());
       /* MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.TermInfo term_info = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.TermInfo();
        reqBody.setTermInfo(term_info);
        term_info.setDevicetype("1");
        term_info.setSecrettext("1");
        term_info.setAppversion("1");
        term_info.setSerialnum("1");
        term_info.setEncryptrandnum("1");
        term_info.setNetworklicense("1");*/
        reqBody.setMerTradeTime(DateUtil.format(dateTime, DatePattern.PURE_TIME_PATTERN));
        reqBody.setNotifyUrl(bocomPayConfig.getAppId() + "@" + bocomPayConfig.getNotifyUrl());
        reqBody.setMerTradeDate(DateUtil.format(dateTime, DatePattern.PURE_DATE_PATTERN));
        // reqBody.setShopId("332211001");
     /*   MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.FeeSplitInfo fee_split_info = new MPNG210001RequestV1.MPNG210001RequestV1Biz.ReqBody.FeeSplitInfo();
        reqBody.setFeeSplitInfo(fee_split_info);
        fee_split_info.setPartnerAmount("1");
        fee_split_info.setMerAmount("1");*/
        reqBody.setPayMerTranNo(order.getOutTradeNo());
        reqBody.setTotalAmount(order.getPrice() + "");
        reqBody.setLocation("ONLINE");
        reqBody.setTranContent(order.getSubject());
        request.setBizContent(bizContent);


        MPNG210001ResponseV1 response = bocomClient.execute(request,IdUtil.fastSimpleUUID());

        return response;
    }
}
