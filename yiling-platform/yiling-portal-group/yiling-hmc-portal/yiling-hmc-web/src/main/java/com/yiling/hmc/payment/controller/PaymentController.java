package com.yiling.hmc.payment.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.payment.form.OnlinePayForm;
import com.yiling.hmc.payment.form.OrderPayAmountForm;
import com.yiling.hmc.payment.form.OrderPayAmountListForm;
import com.yiling.hmc.payment.form.QueryReceiptDeskOrderListForm;
import com.yiling.hmc.payment.vo.QueryResultVO;
import com.yiling.hmc.payment.vo.ReceiptDeskPageVO;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.HmcDiagnosisRecordDTO;
import com.yiling.ih.patient.dto.request.HmcDiagnosisOrderRequest;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeSourceEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.CurrentUserInfo;
import com.yiling.user.system.bo.HmcUser;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 健康管理中心支付收银台模块
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.web.cashier.controller
 * @date: 2023/2/18
 */
@RestController
@RequestMapping("/payment")
@Api(tags = "健康管理中心支付收银台模块")
@Slf4j
public class PaymentController extends BaseController {
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMaService wxMaService;
    @DubboReference
    DictDataApi dictTypeApi;
    @DubboReference(timeout = 60 * 1000)
    PayApi payApi;
    @DubboReference
    MarketOrderApi marketOrderApi;
    @DubboReference
    HmcDiagnosisApi hmcDiagnosisApi;
    @DubboReference
    HmcUserApi hmcUserApi;
    @Autowired
    HttpServletRequest request;


    /**
     * 数据字段支付渠道,typeId
     */
    private final Long PAY_CHANNEL_TYPE_ID = 73l;


    /**
     * 设置支付倒计时间
     *
     * @param vo
     * @param startTime
     */
    private void setPayOrderRemainTime(ReceiptDeskPageVO vo, Long startTime) {

        Calendar nowTime = Calendar.getInstance();
        //
        nowTime.add(Calendar.MINUTE, -30);
        long endTime = nowTime.getTime().getTime();

        if (startTime <= endTime) {
            vo.setRemainTime("0分钟0秒");
            return;
        }
        int m = (int) (((startTime - endTime) % 86400000) % 3600000) / 60000;
        int s = (int) ((((startTime - endTime) % 86400000) % 3600000) % 60000) / 1000;
        Long h = ((startTime - endTime) % 86400000) / 3600000;
        Long d = (startTime - endTime) / 86400000;
        StringBuilder builder = new StringBuilder();

        if (d > 0) {
            builder.append(d).append("天");
        }
        if (h > 0) {
            builder.append(h).append("小时");
        }
        builder.append(m + "分钟" + s + "秒");
        vo.setRemainTime(builder.toString());
    }

    /**
     * 获取在线支付方式
     *
     * @return
     */
    private List<ReceiptDeskPageVO.PaymentChannelVO> getPaymentChannelVOList(PayChannelEnum payChannelEnum) {
        // 查询在线支付渠道类型
        List<DictDataBO> dictDataBOList = dictTypeApi.getEnabledByTypeIdList(PAY_CHANNEL_TYPE_ID);

        if (CollectionUtil.isEmpty(dictDataBOList)) {

            return ListUtil.empty();
        }

        List<PaySourceEnum> paySourceList = payChannelEnum.getPaySourceList();

        if (CollectionUtil.isEmpty(paySourceList)) {

            return ListUtil.empty();
        }

        dictDataBOList = dictDataBOList.stream().filter(t -> t.getStatus() == 1) // 字典已开启
                // 过滤出当前支付渠道支付的支付方式
                .filter(t -> paySourceList.contains(PaySourceEnum.getBySource(t.getValue()))).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(dictDataBOList)) {
            return ListUtil.empty();
        }
        return dictDataBOList.stream().map(e -> {
            ReceiptDeskPageVO.PaymentChannelVO channelVO = new ReceiptDeskPageVO.PaymentChannelVO();
            channelVO.setCode(e.getValue());
            channelVO.setName(e.getLabel());
            channelVO.setUrl(e.getDescription());
            return channelVO;

        }).collect(Collectors.toList());
    }


    @ApiOperation(value = "收银台")
    @PostMapping("/receiptDeskOrderList")
    @ApiResponses({ @ApiResponse(code = 20129, message = "交易已取消,请重新下单!") })
    public Result<ReceiptDeskPageVO> receiptDeskOrderList(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {

        List<PayOrderDTO> resultList = payApi.listPayOrdersByPayId(form.getPayId());

        if (CollectionUtil.isEmpty(resultList)) {

            return Result.failed("未查询到在线支付订单！");
        }

        // 当前用户Id
        List<Long> userIdList = resultList.stream().map(PayOrderDTO::getUserId).distinct().collect(Collectors.toList());
        if (!userIdList.contains(staffInfo.getCurrentUserId())) {

            return Result.failed("未找到相关.订单信息");
        }

        // 支付金额
        BigDecimal payMoney = resultList.stream().map(e -> e.getGoodsAmount()).reduce(BigDecimal::add).get();
        // 订单Ids
        List<Long> orderIds = resultList.stream().map(e -> e.getAppOrderId()).collect(Collectors.toList());
        // 支付订单号
        List<String> orderNoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());

        Integer tradeType = resultList.stream().findFirst().isPresent() ? resultList.stream().findFirst().get().getTradeType() : TradeTypeEnum.PAY.getCode();

        // 支付渠道
        PayChannelEnum payChannelEnum = PayChannelEnum.getByCode(form.getPayWay());

        if (payChannelEnum == null) {

            return Result.failed("支付渠道不存在!");
        }

        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setOrderNoList(orderNoList);
        vo.setOrderIdList(orderIds);
        vo.setTradeType(TradeTypeEnum.getByCode(tradeType).getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setIsPaySuccess(Boolean.FALSE);
        vo.setPaymentChannelList(getPaymentChannelVOList(payChannelEnum));

        Date createTime = new Date();
        Boolean isOrderPaid = false;

        if (TradeTypeEnum.PAY == TradeTypeEnum.getByCode(tradeType) || TradeTypeEnum.PRESCRIPTION == TradeTypeEnum.getByCode(tradeType)) {
            // 查询订单信息
            List<MarketOrderDTO> marketOrderDTOS = marketOrderApi.queryByIdList(orderIds);
            // 订单是否已取消
            Boolean isCancel = marketOrderDTOS.stream().filter(e -> HmcOrderStatusEnum.CANCELED == HmcOrderStatusEnum.getByCode(e.getOrderStatus())).findAny().isPresent();

            if (isCancel) {
                return Result.failed(PaymentErrorCode.ORDER_PAID_CANCEL);
            }

            // 查询订单创建时间
            createTime = Optional.ofNullable(marketOrderDTOS).map(t -> t.stream().findFirst().get().getCreateTime()).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NO_NOT_EXISTS));
            // 订单是否已经支付
            isOrderPaid = marketOrderDTOS.stream().filter(e -> HmcPaymentStatusEnum.PAYED == HmcPaymentStatusEnum.getByCode(e.getPaymentStatus())).findAny().isPresent();

        } else if (TradeTypeEnum.INQUIRY == TradeTypeEnum.getByCode(tradeType)) {

            HmcDiagnosisOrderRequest request = new HmcDiagnosisOrderRequest();
            request.setDiagnosisRecordIdList(orderIds);

            // 查询订单信息
            List<HmcDiagnosisRecordDTO> diagnosisRecordDTO = hmcDiagnosisApi.getDiagnosisOrderByIdList(request);
            // 订单是否已取消
            Boolean isCancel = diagnosisRecordDTO.stream().filter(e -> HmcOrderStatusEnum.CANCELED == HmcOrderStatusEnum.getByCode(e.getStatus())).findAny().isPresent();

            if (isCancel) {
                return Result.failed(PaymentErrorCode.ORDER_PAID_CANCEL);
            }
            // 查询订单创建时间
            createTime = Optional.ofNullable(diagnosisRecordDTO).map(t -> t.stream().findFirst().get().getCreateTime()).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NO_NOT_EXISTS));
        }

        long startTime = createTime.getTime();
        // 设置支付倒计时间
        this.setPayOrderRemainTime(vo, startTime);

        // 在线支付订单,是否支付成功
        Boolean hasPayOrder = resultList.stream().filter(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus())).findAny().isPresent();

        if (hasPayOrder || isOrderPaid) {

            vo.setIsPaySuccess(Boolean.TRUE);
        }

        return Result.success(vo);
    }


    @ApiOperation(value = "在线支付择支付方式")
    @PostMapping("/pay")
    @ApiResponses({ @ApiResponse(code = 20114, message = "订单已支付成功!"), @ApiResponse(code = 20128, message = "交易已取消,请重新发起支付!"), @ApiResponse(code = 20130, message = "用户授权异常,未获取到用户公众号信息!") })
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Map<String, Object>> pay(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid OnlinePayForm form) {

        log.debug("CurrentUserInfo info:{}", staffInfo);

        CreatePayTradeRequest createPayTradeRequest = PojoUtils.map(form, CreatePayTradeRequest.class);
        createPayTradeRequest.setOpUserId(staffInfo.getCurrentUserId());
        createPayTradeRequest.setUserIp(IPUtils.getIp(request));
        createPayTradeRequest.setPayWay(form.getPayWay());
        createPayTradeRequest.setPaySource(form.getPaySource());
        createPayTradeRequest.setTradeSource(TradeSourceEnum.HMC.getCode());

        PayChannelEnum payChannelEnum = PayChannelEnum.getByCode(form.getPayWay());

        if (ObjectUtil.isNull(payChannelEnum)) {

            return Result.failed("支付渠道不存在!");
        }

        PaySourceEnum paySourceEnum = PaySourceEnum.getBySource(form.getPaySource());

        if (ObjectUtil.isNull(paySourceEnum)) {

            return Result.failed("支付来源不存在!");
        }

        if (!payChannelEnum.getPaySourceList().contains(paySourceEnum)) {

            return Result.failed("支付来源和支付渠道不匹配!");
        }

        // 如果是微信支付,需要添加openId,appId
        if (PaySourceEnum.YEE_PAY_WECHAT == PaySourceEnum.getBySource(form.getPaySource())) {
            HmcUser hmcUser = hmcUserApi.getByIdAndAppId(staffInfo.getCurrentUserId(), wxMpService.getWxMpConfigStorage().getAppId());
            // 微信公众号用户Id
            String openId = Optional.ofNullable(hmcUser).map(t -> t.getMiniProgramOpenId()).orElseThrow(() -> new BusinessException(PaymentErrorCode.USER_AUTHORIZATION_ERROR));
            createPayTradeRequest.setOpenId(openId);
            createPayTradeRequest.setAppId(wxMpService.getWxMpConfigStorage().getAppId());

            // 如果是交行微信支付
        } else if (PaySourceEnum.BOCOM_PAY_WECHAT == PaySourceEnum.getBySource(form.getPaySource())) {

            HmcUser hmcUser = hmcUserApi.getByIdAndAppId(staffInfo.getCurrentUserId(), wxMaService.getWxMaConfig().getAppid());
            String openId = Optional.ofNullable(hmcUser).map(t -> t.getMiniProgramOpenId()).orElseThrow(() -> new BusinessException(PaymentErrorCode.USER_AUTHORIZATION_ERROR));
            createPayTradeRequest.setOpenId(openId);
            createPayTradeRequest.setAppId(wxMaService.getWxMaConfig().getAppid());

        } else if (PaySourceEnum.BOCOM_PAY_ALIPAY == PaySourceEnum.getBySource(form.getPaySource())) {
            HmcUser hmcUser = hmcUserApi.getByIdAndAppId(staffInfo.getCurrentUserId(), wxMaService.getWxMaConfig().getAppid());
            String openId = Optional.ofNullable(hmcUser).map(t -> t.getMiniProgramOpenId()).orElseThrow(() -> new BusinessException(PaymentErrorCode.USER_AUTHORIZATION_ERROR));
            createPayTradeRequest.setOpenId(openId);
            createPayTradeRequest.setAppId(wxMaService.getWxMaConfig().getAppid());
        }

        // 查询在线支付渠道类型
        List<DictDataBO> dictDataBOList = dictTypeApi.getEnabledByTypeIdList(PAY_CHANNEL_TYPE_ID);

        if (CollectionUtil.isEmpty(dictDataBOList)) {

            return Result.failed("无可用在线支付方式");
        }
        if (dictDataBOList.stream().anyMatch(t -> form.getPaySource().equals(t.getValue()))) {
            return payApi.createPayTrade(createPayTradeRequest);
        }
        return Result.failed("请选择正确的支付方式!");
    }


    @ApiOperation(value = "根据支付凭据查询订单是否支付成功")
    @GetMapping("/query")
    public Result<QueryResultVO> queryOrder(@ApiParam(value = "银行商户交易单号:例如(微信,支付宝)", required = true) @RequestParam("bankOrderId") String bankOrderId) {
        Pair<Boolean, List<PayOrderDTO>> pair = payApi.selectOrderByBankOrderId(bankOrderId);

        List<PayOrderDTO> payOrderDTOList = pair.getValue();
        PayOrderDTO payOrderDTO = CollectionUtil.isEmpty(payOrderDTOList) ? null : payOrderDTOList.stream().findFirst().get();

        QueryResultVO vo = new QueryResultVO();

        vo.setIsPaySuccess(pair.getKey());
        vo.setPaymentAmount(payOrderDTOList.stream().map(t -> t.getGoodsAmount()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        vo.setOrderIdList(payOrderDTOList.stream().map(t -> t.getAppOrderId()).collect(Collectors.toList()));
        vo.setOrderNoList(payOrderDTOList.stream().map(t -> t.getAppOrderNo()).collect(Collectors.toList()));
        vo.setOrderPlatform(Optional.ofNullable(payOrderDTO).map(t -> t.getOrderPlatform()).orElse(""));
        vo.setTradeType(Optional.ofNullable(payOrderDTO).map(t -> t.getTradeType()).orElse(0));

        return Result.success(vo);
    }


    /**
     * 市场销售订单支付
     *
     * @param staffInfo 用户登录信息
     * @param form 提交form数据
     * @return
     */
    private Result<String> toPayMarketOrder(CurrentUserInfo staffInfo, OrderPayAmountListForm form) {

        List<Long> orderIdList = form.getList().stream().map(t -> t.getOrderId()).collect(Collectors.toList());
        // 查询订单信息
        List<MarketOrderDTO> marketOrderDTOS = marketOrderApi.queryByIdList(orderIdList);

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        Map<Long, MarketOrderDTO> orderMap = marketOrderDTOS.stream().collect(Collectors.toMap(MarketOrderDTO::getId, o -> o, (k1, k2) -> k1));
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();

        for (OrderPayAmountForm appPayAmountForm : form.getList()) {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(staffInfo.getCurrentUserId());
            request.setAppOrderId(appPayAmountForm.getOrderId());
            MarketOrderDTO order = Optional.ofNullable(orderMap.get(appPayAmountForm.getOrderId())).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS));
            request.setAppOrderNo(order.getOrderNo());
            request.setAmount(order.getOrderTotalAmount());
            request.setSellerEid(order.getEid());
            appOrderList.add(request);
        }

        payOrderRequest.setTradeType(TradeTypeEnum.PAY);
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(staffInfo.getCurrentUserId());

        return payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
    }


    /**
     * 处理问诊订单
     *
     * @param staffInfo
     * @param form
     * @return
     */
    private Result<String> toPayInquiryOrder(CurrentUserInfo staffInfo, OrderPayAmountListForm form) {

        List<Long> orderIdList = form.getList().stream().map(t -> t.getOrderId()).collect(Collectors.toList());

        HmcDiagnosisOrderRequest hmcRequest = new HmcDiagnosisOrderRequest();
        hmcRequest.setDiagnosisRecordIdList(orderIdList);

        // 查询订单信息
        List<HmcDiagnosisRecordDTO> marketOrderDTOS = hmcDiagnosisApi.getDiagnosisOrderByIdList(hmcRequest);

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        Map<Integer, HmcDiagnosisRecordDTO> orderMap = marketOrderDTOS.stream().collect(Collectors.toMap(HmcDiagnosisRecordDTO::getDiagnosisRecordId, o -> o, (k1, k2) -> k1));
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();

        for (OrderPayAmountForm appPayAmountForm : form.getList()) {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(staffInfo.getCurrentUserId());
            request.setAppOrderId(appPayAmountForm.getOrderId());
            HmcDiagnosisRecordDTO order = Optional.ofNullable(orderMap.get(appPayAmountForm.getOrderId())).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS));
            request.setAppOrderNo(order.getOrderNo());
            request.setAmount(order.getDiagnosisRecordPrice());
            request.setSellerEid(0l);
            appOrderList.add(request);
        }

        payOrderRequest.setTradeType(TradeTypeEnum.INQUIRY);
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(staffInfo.getCurrentUserId());

        return payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
    }


    /**
     * 处理处方订单
     *
     * @param staffInfo
     * @param form
     * @return
     */
    private Result<String> toPayPrescriptionOrder(CurrentUserInfo staffInfo, OrderPayAmountListForm form) {

        List<Long> orderIdList = form.getList().stream().map(t -> t.getOrderId()).collect(Collectors.toList());

        // 查询订单信息
        List<MarketOrderDTO> marketOrderDTOS = marketOrderApi.queryByIdList(orderIdList);

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        Map<Long, MarketOrderDTO> orderMap = marketOrderDTOS.stream().collect(Collectors.toMap(MarketOrderDTO::getId, o -> o, (k1, k2) -> k1));
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();

        for (OrderPayAmountForm appPayAmountForm : form.getList()) {
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(staffInfo.getCurrentUserId());
            request.setAppOrderId(appPayAmountForm.getOrderId());
            MarketOrderDTO order = Optional.ofNullable(orderMap.get(appPayAmountForm.getOrderId())).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS));
            request.setAppOrderNo(order.getOrderNo());
            request.setAmount(order.getOrderTotalAmount());
            request.setSellerEid(order.getEid());
            appOrderList.add(request);
        }

        payOrderRequest.setTradeType(TradeTypeEnum.PRESCRIPTION);
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(staffInfo.getCurrentUserId());

        return payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
    }


    @ApiOperation(value = "去支付")
    @PostMapping("/toPay")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<String> toPayBatch(@CurrentUser CurrentUserInfo staffInfo, @Valid @RequestBody OrderPayAmountListForm form) {

        EnumSet<TradeTypeEnum> canPayTradeTypeEnum = EnumSet.of(TradeTypeEnum.PAY, TradeTypeEnum.INQUIRY, TradeTypeEnum.PRESCRIPTION);

        if (!canPayTradeTypeEnum.contains(TradeTypeEnum.getByCode(form.getTradeType()))) {

            return Result.failed(OrderErrorCode.ORDER_CATEGORY_NOT_PAY.getCode(), "交易类型错误");
        }

        if (TradeTypeEnum.PAY == TradeTypeEnum.getByCode(form.getTradeType())) {

            return toPayMarketOrder(staffInfo, form);
        }

        if (TradeTypeEnum.INQUIRY == TradeTypeEnum.getByCode(form.getTradeType())) {

            return toPayInquiryOrder(staffInfo, form);
        }

        if (TradeTypeEnum.PRESCRIPTION == TradeTypeEnum.getByCode(form.getTradeType())) {

            return toPayPrescriptionOrder(staffInfo, form);
        }

        return Result.failed("交易类型错误");
    }


    @ApiOperation(value = "取消在线预支付交易记录")
    @PostMapping("/cancel")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> cancelPayOrder(@CurrentUser CurrentUserInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {

        return payApi.cancelPayOrder(form.getPayId());
    }


}
