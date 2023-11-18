package com.yiling.payment.web.receiptdesk.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PasswordUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.PresaleOrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.PresaleOrderDTO;
import com.yiling.order.order.enums.OrderCategoryEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.order.enums.PreSaleActivityTypeEnum;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.payment.web.receiptdesk.form.GetUrlLinkForm;
import com.yiling.payment.web.receiptdesk.form.GetWechatOpenIdForm;
import com.yiling.payment.web.receiptdesk.form.OnlinePayForm;
import com.yiling.payment.web.receiptdesk.form.OrderPayAmountForm;
import com.yiling.payment.web.receiptdesk.form.OrderPayAmountListForm;
import com.yiling.payment.web.receiptdesk.form.QueryReceiptDeskOrderListForm;
import com.yiling.payment.web.receiptdesk.vo.GetWechatOpenIdVO;
import com.yiling.payment.web.receiptdesk.vo.ReceiptDeskPageVO;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.web.cashier.controller
 * @date: 2021/10/28
 */
@RestController
@RequestMapping("/payment")
@Api(tags = "在线支付收银台模块")
@Slf4j
public class PaymentController extends BaseController {

    @Value("${payment.weChat.appId}")
    private String appId;
    @Value("${payment.weChat.appSecret}")
    private String appSecret;
    @Value("${payment.weChat.grantType}")
    private String grantType;
    @Value("${spring.profiles.active}")
    private String env;
    @Value("${payment.weChat.codeUrl}")
    private String codeUrl;
    @Value("${payment.weChat.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${payment.weChat.urlLink}")
    private String urlLinkUrl;
    @Value("${payment.weChat.accessTokenPwd}")
    private String accessTokenPwd;
    @Value("${payment.weChat.prdAccessTokenUrl}")
    private String prdAccessTokenUrl;

    @DubboReference
    DictDataApi dictTypeApi;
    @DubboReference
    PayApi payApi;
    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PresaleOrderApi presaleOrderApi;

    @Autowired
    HttpServletRequest request;
    @Autowired
    RedisService redisService;


    /**
     * 数据字段支付渠道,typeId
     */
    private final Long PAY_CHANNEL_TYPE_ID = 73l;

    /**
     * 交易订单收银台
     *
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> payOrder(List<PayOrderDTO> resultList, Long currentEid,TradeTypeEnum tradeTypeEnum) {
        List<Long> sellerEids = resultList.stream().map(PayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关.订单信息");
        }
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        OrderDTO orderOne = orderApi.selectByOrderNo(orderDtoList.get(0));
        if (orderOne == null) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关订单信息");
        }
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("订单已取消,请重新发起支付");
        }
        if (CompareUtil.compare(PaymentStatusEnum.PAID.getCode(), orderOne.getPaymentStatus()) == 0) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("订单已支付完成，请勿重复支付!");
        }
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setContentJson("");
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(tradeTypeEnum.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList(PayChannelEnum.YEEPAY));
        Date createTime = orderOne.getCreateTime();
        long startTime = createTime.getTime();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, -15);
        long endTime = nowTime.getTime().getTime();
        // 设置支付倒计时间
        this.setPayOrderRemainTime(vo,startTime,endTime);

        return Result.success(vo);
    }


    /**
     * 设置支付倒计时间
     * @param vo
     * @param startTime
     * @param endTime
     */
    private void setPayOrderRemainTime(ReceiptDeskPageVO vo,Long startTime,Long endTime) {
        if (startTime <= endTime) {
            vo.setRemainTime("0分钟0秒");
            return;
        }
        int m = (int) (((startTime - endTime) % 86400000) % 3600000) / 60000;
        int s = (int) ((((startTime - endTime) % 86400000) % 3600000) % 60000) / 1000;
        Long h = ((startTime - endTime) % 86400000) / 3600000;
        Long d =   (startTime - endTime) / 86400000 ;
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
     * 预售订单支付收银台
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> payPresaleOrder(List<PayOrderDTO> resultList, Long currentEid,TradeTypeEnum tradeTypeEnum) {
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关.订单信息");
        }

        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        PresaleOrderDTO orderOne = presaleOrderApi.getOrderInfo(resultList.stream().findFirst().get().getAppOrderId());
        if (orderOne == null) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关订单信息");
        }
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("订单已取消,请重新发起支付");
        }
        if (CompareUtil.compare(PaymentStatusEnum.PAID.getCode(), orderOne.getPaymentStatus()) == 0) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("订单已支付完成，请勿重复支付!");
        }

        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setContentJson("");
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(tradeTypeEnum.getCode());
        vo.setPayId(resultList.stream().findFirst().get().getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList(PayChannelEnum.YEEPAY));

        // 尾款支付,基本校验以及设置倒计时
        if (TradeTypeEnum.BALANCE == tradeTypeEnum) {
            // 拦截尾款是否已经支付成功,防止重复支付
            if (orderOne.getIsPayBalance() == 1) {
                log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
                return Result.failed("订单已支付完尾款,请勿重复支付!");
            }
            long nowTime = new Date().getTime();
            Long endTime = orderOne.getBalanceEndTime().getTime();
            this.setPayOrderRemainTime(vo,endTime,nowTime);
        }

        // 设置定金支付,基本校验以及设置倒计时
        if (TradeTypeEnum.DEPOSIT == tradeTypeEnum) {
            // 拦截定金是否已经支付成功,防止重复支付
            if (orderOne.getIsPayDeposit() == 1) {
                log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
                return Result.failed("订单已支付完尾款,请勿重复支付!");
            }
            Date createTime = orderOne.getCreateTime();
            long startTime = createTime.getTime();
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, -15);
            long endTime = nowTime.getTime().getTime();
            // 设置支付倒计时间
            this.setPayOrderRemainTime(vo,startTime,endTime);
        }

        return Result.success(vo);
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
                .filter(t -> paySourceList.contains(PaySourceEnum.getBySource(t.getValue())))
                .collect(Collectors.toList());


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

    /**
     * 在线还款
     *
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> backOrder(List<PayOrderDTO> resultList, Long currentEid) {
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关订单信息");
        }
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setContentJson("");
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.BACK.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList(PayChannelEnum.YEEPAY));
        return Result.success(vo);
    }

    /**
     * 会员购买
     *
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> memberOrder(List<PayOrderDTO> resultList, Long currentEid) {

        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{}", currentEid, buyerEids);
            return Result.failed("未找到相关订单信息");
        }
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips("");
        vo.setContentJson(resultList.get(0).getContent());
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.MEMBER.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList(PayChannelEnum.YEEPAY));
        return Result.success(vo);

    }

    @ApiOperation(value = "以岭收银台")
    @PostMapping("/receiptDeskOrderList")
    public Result<ReceiptDeskPageVO> receiptDeskOrderList(@RequestBody @Valid QueryReceiptDeskOrderListForm form) {
        List<PayOrderDTO> resultList = payApi.listPayOrdersByPayId(form.getPayId());
        if (CollectionUtil.isEmpty(resultList)) {
            return Result.failed("未查询到在线支付订单！");
        }
        // 在线支付订单,是否支付成功
        Boolean hasPayOrder = resultList.stream().filter(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus())).findAny().isPresent();
        // 当前登录企业Eid
        Long currentEid = resultList.stream().findFirst().get().getBuyerEid();
        if (hasPayOrder) {
            return Result.failed("订单已支付成功,请勿重复支付!");
        }
        switch (TradeTypeEnum.getByCode(resultList.get(0).getTradeType())) {
            case PAY:
                return this.payOrder(resultList, currentEid,TradeTypeEnum.PAY);
            case DEPOSIT:
                return this.payPresaleOrder(resultList, currentEid,TradeTypeEnum.DEPOSIT);
            case BACK:
                return this.backOrder(resultList, currentEid);
            case MEMBER:
                return this.memberOrder(resultList, currentEid);
            case BALANCE:
                return this.payPresaleOrder(resultList, currentEid,TradeTypeEnum.BALANCE);
            default:
                return Result.failed("未查询到在线支付订单");
        }

    }

    @ApiOperation(value = "在线支付择支付方式")
    @PostMapping("/pay")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Map<String, Object>> pay(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OnlinePayForm form) {
        if (ObjectUtil.equal(form.getPaySource(), PaySourceEnum.YEE_PAY_WECHAT.getSource()) && StrUtil.isEmpty(form.getOpenId())) {
            return Result.failed("微信支付时openId不能为空");
        }
        CreatePayTradeRequest createPayTradeRequest = PojoUtils.map(form, CreatePayTradeRequest.class);
        createPayTradeRequest.setOpUserId(staffInfo.getCurrentUserId());
        createPayTradeRequest.setUserIp(IPUtils.getIp(request));
        createPayTradeRequest.setPayWay(form.getPayWay());
        createPayTradeRequest.setPaySource(form.getPaySource());
        createPayTradeRequest.setTradeSource(form.getTradeSource());
        createPayTradeRequest.setAppId(appId);
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

    @ApiOperation(value = "查询订单是否支付成功")
    @GetMapping("/query")
    @UserAccessAuthentication
    public Result<Boolean> queryOrder(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "交易单号", required = true) @RequestParam("payNo") String payNo) {
        return payApi.orderQueryByPayNo(payNo);
    }

    @ApiOperation(value = "获取微信openId")
    @PostMapping("/getWechatOpenId")
    public Result<GetWechatOpenIdVO> getWechatOpenId(@Valid @RequestBody GetWechatOpenIdForm form) {
        // 请求参数
        String params = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + form.getCode() + "&grant_type=" + grantType;
        // 发送请求
        HttpRequest request = HttpUtil.createGet(codeUrl + "?" + params);
        HttpResponse response = request.execute();
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response.body());

        // 用户的唯一标识（openid）
        String openId = json.getString("openid");
        GetWechatOpenIdVO vo = new GetWechatOpenIdVO();
        vo.setOpenId(openId);
        return Result.success(vo);
    }


    /**
     * 跳转支付请求支付系统,创建支付交易单据
     * @param userId 登录用户Id
     * @param form  创建form表单数据
     * @param orderList 订单集合
     * @param contentBuilder 文本内容
     * @return
     */
    private Result<String> toCreatePayOrder(Long userId,OrderPayAmountListForm form,List<PresaleOrderDTO> orderList,StringBuilder contentBuilder) {

        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        Map<Long, OrderDTO> orderMap = orderList.stream().collect(Collectors.toMap(OrderDTO::getId, o -> o, (k1, k2) -> k1));
        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();
        Map<Long,PresaleOrderDTO> presaleOrderDTOMap = orderList.stream().collect(Collectors.toMap(PresaleOrderDTO::getId, Function.identity()));

        for (OrderPayAmountForm appPayAmountForm : form.getList()) {
            PresaleOrderDTO presaleOrderDTO = presaleOrderDTOMap.get(appPayAmountForm.getOrderId());
            CreatePayOrderRequest.appOrderRequest request = new CreatePayOrderRequest.appOrderRequest();
            request.setUserId(userId);
            request.setAppOrderId(appPayAmountForm.getOrderId());
            OrderDTO order = Optional.ofNullable(orderMap.get(appPayAmountForm.getOrderId())).orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS));
            request.setAppOrderNo(order.getOrderNo());
            request.setAmount(order.getPaymentAmount());
            if (TradeTypeEnum.DEPOSIT == TradeTypeEnum.getByCode(form.getTradeType())) {
                request.setAmount(presaleOrderDTO.getDepositAmount());
            }
            if (TradeTypeEnum.BALANCE == TradeTypeEnum.getByCode(form.getTradeType())) {
                request.setAmount(presaleOrderDTO.getBalanceAmount());
            }
            request.setBuyerEid(order.getBuyerEid());
            request.setSellerEid(order.getSellerEid());
            appOrderList.add(request);
        }

        payOrderRequest.setTradeType(TradeTypeEnum.getByCode(form.getTradeType()));
        payOrderRequest.setContent(contentBuilder.toString());
        payOrderRequest.setAppOrderList(appOrderList);
        payOrderRequest.setOpUserId(userId);

        return payApi.createPayOrder(payOrderRequest);
    }


    /**
     * 付全款支付订单
     * @param staffInfo 用户登录信息
     * @param form 提交form数据
     * @return
     */
    private Result<String> toFullPay(CurrentStaffInfo staffInfo,OrderPayAmountListForm form) {

        StringBuilder builder = new StringBuilder();
        builder.append("订单包含");
        builder.append(form.getList().size() + "笔在线支付 ");

        List<Long> ids = form.getList().stream().map(order -> order.getOrderId()).collect(Collectors.toList());

        List<PresaleOrderDTO> orderList = PojoUtils.map(orderApi.listByIds(ids),PresaleOrderDTO.class);

        if (CollectionUtil.isEmpty(orderList)) {
            return Result.failed("未查询到相关的销售订单信息!");
        }

        Boolean existPresaleOrder = orderList.stream().anyMatch(t -> OrderCategoryEnum.PRESALE == OrderCategoryEnum.getByCode(t.getOrderCategory()));

        if (existPresaleOrder) {
            log.info("支付订单信息:{}",orderList);
            return Result.failed("存在预售支付订单，订单类型错误!");
        }

        Boolean isOnlinePayOrder = orderList.stream().allMatch(t -> PaymentMethodEnum.ONLINE == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue()));

        if (!isOnlinePayOrder) {
            log.info("支付订单信息:{}",orderList);
            return Result.failed("订单不是全款支付订单，订单类型错误!");
        }

        Boolean isCanPayOrder = orderList.stream().allMatch(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus()));

        if (!isCanPayOrder) {
            log.info("支付订单信息:{}",orderList);
            return Result.failed("请选择未支付的订单!");
        }

        return this.toCreatePayOrder(staffInfo.getCurrentUserId(),form,orderList,builder);
    }


    /**
     * 订单部分支付
     * @param staffInfo 用户登录信息
     * @param form 提交form数据
     * @return
     */
    private Result<String> toPartPay(CurrentStaffInfo staffInfo,OrderPayAmountListForm form) {

        StringBuilder builder = new StringBuilder();
        builder.append("订单包含");
        builder.append(form.getList().size() + "笔在线支付 ");

        List<Long> ids = form.getList().stream().map(order -> order.getOrderId()).collect(Collectors.toList());
        List<PresaleOrderDTO> orderList = presaleOrderApi.listByOrderIds(ids);

        if (CollectionUtil.isEmpty(orderList)) {

            return Result.failed("未查询到有效的预售订单!");
        }

        // 支付定金，校验定金是否已经支付
        if (TradeTypeEnum.DEPOSIT == TradeTypeEnum.getByCode(form.getTradeType())) {
            Boolean isPayDeposit  = orderList.stream().anyMatch(t -> t.getIsPayDeposit() == 1);
            if (isPayDeposit) {
                return Result.failed("订单已支付定金,请勿重复支付!");
            }
        }

        // 尾款支付,添加基本校验,防止重复支付
        if (TradeTypeEnum.BALANCE == TradeTypeEnum.getByCode(form.getTradeType())) {
            // 不容许支付尾款的
            Boolean isNotAllowPayBalance  = orderList.stream()
                    .filter(t -> PreSaleActivityTypeEnum.DEPOSIT == PreSaleActivityTypeEnum.getByCode(t.getActivityType())) // 定金预售活动
                    .filter(t -> DateUtil.compare(new Date(),t.getBalanceStartTime()) <= 0 || DateUtil.compare(new Date(),t.getBalanceEndTime()) > 0) // 不在时间范围内
                    .findFirst().isPresent();

            if (isNotAllowPayBalance) {
                return Result.failed("尾款订单不在支付时间范围内!");
            }
            // 订单是否全部支付定金
            Boolean isPayDeposit  = orderList.stream().allMatch(t -> t.getIsPayDeposit() == 1);
            if (!isPayDeposit) {
                return Result.failed("订单还未支付定金,请先支付定金!");
            }
        }

        return this.toCreatePayOrder(staffInfo.getCurrentUserId(),form,orderList,builder);
    }


    /**
     * 去还款订单信息
     * @param staffInfo 用户登录信息
     * @param form 提交form数据
     * @return
     */
    private Result<String> toBack(CurrentStaffInfo staffInfo,OrderPayAmountListForm form) {

        StringBuilder builder = new StringBuilder();
        builder.append("共");
        builder.append(form.getList().size() + "笔订单 ");

        List<Long> ids = form.getList().stream().map(order -> order.getOrderId()).collect(Collectors.toList());

        List<PresaleOrderDTO> orderList = PojoUtils.map(orderApi.listByIds(ids),PresaleOrderDTO.class);

        if (CollectionUtil.isEmpty(orderList)) {
            return Result.failed("未查询到相关的还款订单信息!");
        }

        // 校验订单是否为账期还款订单
        orderList = orderList.stream()
                .filter(t -> PaymentMethodEnum.PAYMENT_DAYS == PaymentMethodEnum.getByCode(t.getPaymentMethod().longValue()))
                .filter(t -> PaymentStatusEnum.UNPAID == PaymentStatusEnum.getByCode(t.getPaymentStatus())).collect(Collectors.toList());

        if (CollectionUtil.isEmpty(orderList)) {
            log.info("未查询到相关还款订单信息:{}",form);
            return Result.failed("未查询到相关的还款订单信息!");
        }

        return this.toCreatePayOrder(staffInfo.getCurrentUserId(),form,orderList,builder);
    }


    @ApiOperation(value = "去支付(还款|订单支付|付定金|付尾款)")
    @PostMapping("/toPay")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<String> toPayBatch(@CurrentUser CurrentStaffInfo staffInfo, @Valid @RequestBody OrderPayAmountListForm form) {
        if (TradeTypeEnum.getByCode(form.getTradeType()) == null) {
            return Result.failed(OrderErrorCode.ORDER_CATEGORY_NOT_PAY);
        }

        switch (TradeTypeEnum.getByCode(form.getTradeType())) {
            case PAY:
                return  this.toFullPay(staffInfo,form);
            case DEPOSIT:
            case BALANCE:
                return  this.toPartPay(staffInfo,form);
            case BACK:
                return this.toBack(staffInfo,form);
            default: return Result.failed(OrderErrorCode.ORDER_CATEGORY_NOT_PAY);
        }
    }


    @ApiOperation(value = "取消在线预支付交易记录")
    @PostMapping("/cancel")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    public Result<Void> cancelPayOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {

        return payApi.cancelPayOrder(form.getPayId());
    }

    /**
     * 获取微信UrlLink
     * @param form
     * @return
     */
    @ApiOperation(value = "获取微信UrlLink")
    @PostMapping("/getUrlLink")
    public Result<String> getUrlLink(@RequestBody @Valid GetUrlLinkForm form,HttpServletRequest request) {
        BigDecimal payAmount = payApi.selectOrderAmountByPayId(form.getPayId());
        if (ObjectUtil.isNull(payAmount)){
            return Result.failed("支付订单不存在");
        }
        //获取accessToken
        String accessToken = getAccessToken();

        String[] tokenArr = request.getHeader("Authorization").split(" ");
        if (tokenArr.length<2){
            return Result.failed("token不存在");
        }
        // 发送请求
        HttpRequest sendRequest = HttpUtil.createPost(urlLinkUrl + "?access_token=" + accessToken);

        JSONObject jsonObject = new JSONObject();
        if (Constants.DEBUG_ENV_LIST.contains(env)) {
            jsonObject.put("env_version", "trial");
        }
        //到期失效
        jsonObject.put("is_expire", true);
        //默认值0.小程序 URL Link 失效类型，失效时间：0，失效间隔天数：1
        jsonObject.put("expire_type", 1);
        //到期失效的URL Link的失效间隔天数。生成的到期失效jsonObject = {JSONObject@13098}  size = 6URL Link在该间隔时间到达前有效。最长间隔天数为365天。expire_type 为 1 必填
        jsonObject.put("expire_interval", 1);
        jsonObject.put("query", "payId=" + form.getPayId() + "&payMoney=" + payAmount + "&token=" + tokenArr[1]+"&type=b2b");
        jsonObject.put("path", "/pages/index/index");

        HttpResponse response = sendRequest.body(JSONObject.toJSONString(jsonObject)).execute();

        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response.body());

        String status = json.getString("errmsg");
        if ("ok".equals(status)){
            String urlLink = json.getString("url_link");
            return Result.success(urlLink);
        }else {
            log.error("获取微信urlLink异常，返回参数={}，请求地址={}，请求参数={}",json,urlLinkUrl + "?access_token=" + accessToken,jsonObject);
            return Result.failed("微信支付异常，请稍后重试");
        }

    }

    /**
     * 获取微信AccessToken
     *
     * @param pwd
     * @return
     */
    @ApiOperation(value = "获取微信AccessToken")
    @GetMapping("/getAccessToken")
    public Result<String> getAccessToken(@RequestParam String pwd) {
        String encrypt = PasswordUtils.encrypt(accessTokenPwd, accessTokenPwd);
        if (StrUtil.equals(encrypt,pwd)){
            return Result.success(getAccessToken());
        }else {
            return Result.failed("");
        }
    }

    /**
     * 获取微信accessToken
     *
     * @return
     */
    public String getAccessToken(){
        //如果是开发及测试环境则向生产环境请求，以避免accessToken冲突
        if (Constants.DEBUG_ENV_LIST.contains(env)) {
            String encrypt = PasswordUtils.encrypt(accessTokenPwd, accessTokenPwd);
            // 发送请求
            HttpRequest request = HttpUtil.createGet(prdAccessTokenUrl + "?pwd=" + encrypt);
            HttpResponse response = request.execute();
            // 解析相应内容（转换成json对象）
            JSONObject json;
            try {
                 json = JSONObject.parseObject(response.body());
            } catch (Exception e) {
                log.error("向生产环境获取accessToken失败，原因={}",e.getLocalizedMessage());
                throw new BusinessException(ResultCode.FAILED);
            }
            Integer code = json.getInteger("code");
            if (!ResultCode.SUCCESS.getCode().equals(code)){
                log.error("向生产环境获取accessToken失败，traceId={}",json.getString("traceId"));
                throw new BusinessException(ResultCode.FAILED);
            }
            String responseData = json.getString("data");
            if (StrUtil.isBlank(responseData)){
                log.error("向生产环境获取accessToken为空，traceId={}，token={}",json.getString("traceId"),responseData);
                throw new BusinessException(ResultCode.FAILED);
            }
            return responseData;
        }

        String accessToken = (String)redisService.get("miniProgram:b2b:accessToken");
        if (StrUtil.isBlank(accessToken)){
            // 请求参数
            String params = "appid=" + appId + "&secret=" + appSecret;
            // 发送请求
            HttpRequest request = HttpUtil.createGet(accessTokenUrl + "?grant_type=client_credential&" + params);
            HttpResponse response = request.execute();
            // 解析相应内容（转换成json对象）
            JSONObject json = JSONObject.parseObject(response.body());

            // 用户的唯一标识（openid）
            accessToken = json.getString("access_token");
            if (StrUtil.isBlank(accessToken)){
                log.error("获取微信accessToken异常，返回参数={}",response.body());
                throw new BusinessException(ResultCode.FAILED);
            }
            redisService.set("miniProgram:b2b:accessToken",accessToken,7100);
        }
        return accessToken;
    }
}
