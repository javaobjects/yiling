package com.yiling.sales.assistant.app.payment.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.payment.api.PaymentApi;
import com.yiling.mall.payment.dto.request.PaymentRequest;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentMethodEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.order.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.PayChannelEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.TradeSourceEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.sales.assistant.app.payment.form.GetWechatOpenIdForm;
import com.yiling.sales.assistant.app.payment.form.OnlinePayForm;
import com.yiling.sales.assistant.app.payment.form.QueryOnlineDeskOrderListForm;
import com.yiling.sales.assistant.app.payment.form.QueryReceiptDeskOrderListForm;
import com.yiling.sales.assistant.app.payment.form.SelectOrderPaymentMethodForm;
import com.yiling.sales.assistant.app.payment.vo.GetWechatOpenIdVO;
import com.yiling.sales.assistant.app.payment.vo.OnlineReceiptDeskPageVO;
import com.yiling.sales.assistant.app.payment.vo.OrderDiscountAmountVO;
import com.yiling.sales.assistant.app.payment.vo.OrderListItemVO;
import com.yiling.sales.assistant.app.payment.vo.PaymentMethodVO;
import com.yiling.sales.assistant.app.payment.vo.PaymentTypeVO;
import com.yiling.sales.assistant.app.payment.vo.ReceiptDeskPageVO;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.PaymentMethodDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付模块
 *
 * @author: zhigang.guo
 * @date: 2021/9/22
 */
@RestController
@RequestMapping("/payment")
@Api(tags="支付模块接口")
@Slf4j
public class PaymentController extends BaseController {
    @Value("${payment.weChat.appId}")
    private String appId;

    @Value("${payment.weChat.appSecret}")
    private String appSecret;

    @Value("${payment.weChat.grantType}")
    private String grantType;

    @Value("${payment.weChat.codeUrl}")
    private String codeUrl;

    @DubboReference
    OrderApi              orderApi;
    @DubboReference
    PaymentApi            paymentApi;
    @DubboReference
    PaymentMethodApi      paymentMethodApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    DictDataApi           dictTypeApi;
    @DubboReference
    PayApi                 payApi;

    /**
     * 数据字段支付渠道,typeId
     */
    private final Long PAY_CHANNEL_TYPE_ID = 73l;

    @ApiOperation(value="POP订单收营台订单列表")
    @PostMapping("/receiptdesk/orderList")
    public Result<ReceiptDeskPageVO> receiptDeskOrderList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form) {
        if (!staffInfo.getYilingFlag()) {
            return Result.failed("非以岭内部人员，无此操作权限!");
        }
        // 订单列表
        List<OrderDTO> orderDTOList= orderApi.listByOrderNos(form.getOrderNos());
        if (CollUtil.isEmpty(orderDTOList)) {
            return Result.failed("未找到相关订单信息");
        }
        List<Long> sellerEids=orderDTOList.stream().map(OrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        ReceiptDeskPageVO receiptDeskPageVO=new ReceiptDeskPageVO();
        receiptDeskPageVO.setOrderNum(orderDTOList.size());
        receiptDeskPageVO.setTotalAmount(orderDTOList.stream().map(OrderDTO::getTotalAmount).reduce(BigDecimal::add).get());
        receiptDeskPageVO.setSelectedAmount(BigDecimal.ZERO);
        receiptDeskPageVO.setDiscountAmount(BigDecimal.ZERO);
        receiptDeskPageVO.setPaymentAmount(orderDTOList.stream().map(OrderDTO::getPaymentAmount).reduce(BigDecimal::add).get());

        List<OrderListItemVO> orderListVO=CollUtil.newArrayList();
        orderDTOList.forEach(orderInfo -> {
            OrderListItemVO orderListItemVO= new OrderListItemVO();
            orderListItemVO.setOrderId(orderInfo.getId());
            orderListItemVO.setOrderNo(orderInfo.getOrderNo());
            orderListItemVO.setDistributorEid(orderInfo.getDistributorEid());
            orderListItemVO.setDistributorEname(orderInfo.getDistributorEname());
            orderListItemVO.setTotalAmount(orderInfo.getTotalAmount());
            orderListItemVO.setDiscountAmount(BigDecimal.ZERO);
            orderListItemVO.setPaymentAmount(orderInfo.getTotalAmount());
            orderListItemVO.setContractNumber(orderInfo.getContractNumber());
            List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(orderInfo.getSellerEid(), orderInfo.getBuyerEid(), PlatformEnum.POP);
            orderListItemVO.setSelected(orderInfo.getPaymentMethod() != null && CompareUtil.compare(orderInfo.getPaymentMethod(), 0) != 0);
            if (CollUtil.isEmpty(paymentMethodDTOList)) {
                orderListItemVO.setPaymentTypeList(ListUtil.empty());
                orderListItemVO.setSelectEnabled(false);
            } else {
                Map<Integer, List<PaymentMethodDTO>> customerPaymentMethodDTOMap=paymentMethodDTOList.stream().collect(Collectors.groupingBy(PaymentMethodDTO::getType));

                List<PaymentTypeVO> paymentTypeVOList=CollUtil.newArrayList();
                for (Integer type : customerPaymentMethodDTOMap.keySet()) {
                    PaymentTypeVO paymentTypeVO = new PaymentTypeVO();
                    paymentTypeVO.setId(type);

                    List<PaymentMethodVO> paymentMethodVOList = CollUtil.newArrayList();
                    paymentMethodDTOList.forEach(paymentMethod -> {
                        PaymentMethodVO paymentMethodVO = new PaymentMethodVO();
                        paymentMethodVO.setId(paymentMethod.getCode());
                        paymentMethodVO.setName(paymentMethod.getName());
                        paymentMethodVO.setEnabled(true);
                        paymentMethodVOList.add(paymentMethodVO);
                    });
                    paymentTypeVO.setPaymentMethodList(paymentMethodVOList);

                    paymentTypeVOList.add(paymentTypeVO);
                }
                orderListItemVO.setPaymentTypeList(paymentTypeVOList);
            }

            orderListVO.add(orderListItemVO);
        });
        receiptDeskPageVO.setOrderList(orderListVO);

        // 设置selectEnabled字段
        receiptDeskPageVO.getOrderList().forEach(orderInfo -> {
            if (orderInfo.getSelectEnabled() == null) {
                List<PaymentMethodVO> orderPaymentMethodList=orderInfo.getPaymentTypeList().stream().map(PaymentTypeVO::getPaymentMethodList).flatMap(Collection::stream).collect(Collectors.toList());
                Optional optional=orderPaymentMethodList.stream().filter(e -> e.getEnabled()).findAny();
                orderInfo.setSelectEnabled(optional.isPresent());
            }
        });

        return Result.success(receiptDeskPageVO);
    }



    @ApiOperation(value="POP订单选择订单支付方式")
    @PostMapping("/select/orderPaymentMethod")
    public Result<OrderDiscountAmountVO> selectOrderPaymentMethod(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SelectOrderPaymentMethodForm form) {
        Long orderId=form.getOrderId();
        OrderDTO orderDTO=orderApi.getOrderInfo(orderId);
        if (orderDTO == null) {
            return Result.failed("订单信息不存在");
        }

        // 判断订单是否已支付
        if (orderDTO.getPaymentMethod() != 0) {
            return Result.failed("该订单已支付过，请勿重复支付");
        }

        // 校验支付方式
        List<PaymentMethodDTO> paymentMethodDTOList=paymentMethodApi.listByEidAndCustomerEid(orderDTO.getSellerEid(), orderDTO.getBuyerEid(), PlatformEnum.POP);
        if (CollUtil.isEmpty(paymentMethodDTOList)) {
            throw new BusinessException(PaymentErrorCode.NO_PAYMENT_METHOD);
        }
        List<Long> paymentMethodIds=paymentMethodDTOList.stream().map(PaymentMethodDTO::getCode).collect(Collectors.toList());
        if (!paymentMethodIds.contains(form.getPaymentMethodId())) {
            throw new BusinessException(PaymentErrorCode.PAYMENT_METHOD_UNUSABLE);
        }

        Long sellerEid=orderDTO.getSellerEid();

        PaymentRequest.OrderPaymentRequest request=new PaymentRequest.OrderPaymentRequest();
        request.setOrderId(orderId);
        request.setPaymentMethodId(form.getPaymentMethodId());
        // 计算现折金额
        BigDecimal orderCashDiscountAmount=paymentApi.calculateOrderCashDiscountAmount(request);
        BigDecimal paymentAmount = NumberUtil.sub(orderDTO.getTotalAmount(),orderCashDiscountAmount);

        PaymentMethodEnum paymentMethodEnum=PaymentMethodEnum.getByCode(form.getPaymentMethodId());
        if (paymentMethodEnum == PaymentMethodEnum.PAYMENT_DAYS) {
            // 账期可用额度
            BigDecimal paymentDaysAvailableAmount=paymentDaysAccountApi.getAvailableAmountByCustomerEid(sellerEid, orderDTO.getBuyerEid());
            // 订单应付金额
            BigDecimal orderPaymentAmount=NumberUtil.sub(orderDTO.getTotalAmount(), orderDTO.getFreightAmount(), orderCashDiscountAmount);
            if (paymentDaysAvailableAmount.compareTo(orderPaymentAmount) == -1) {
                return Result.failed("账期剩余额度：¥ " + paymentDaysAvailableAmount + "，您已超出限额，请更换支付方式或申请临时额度");
            }
        }

        return Result.success(new OrderDiscountAmountVO(orderId, orderCashDiscountAmount,paymentAmount));
    }




    /**
     * 交易订单收银台
     * @param resultList
     * @return
     */
    private Result<OnlineReceiptDeskPageVO> payOrder(List<PayOrderDTO> resultList) {
        List<Long> sellerEids = resultList.stream().map(PayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
      /*  List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(sellerEids.get(0),currentEid, PlatformEnum.B2B);
        if (CollUtil.isEmpty(paymentMethodDTOList)
                || !paymentMethodDTOList.stream().filter(e -> PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE ).findAny().isPresent()) {
            log.error("payOrder..",paymentMethodDTOList);
            return Result.failed("企业不支持在线支付的方式,请确认!");
        }*/
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        OrderDTO orderOne = orderApi.selectByOrderNo(orderDtoList.get(0));
        if (orderOne == null) {
            log.info("payOrder....buyerEid.{}",buyerEids);
            return Result.failed("未找到相关订单信息");
        }

        if (CompareUtil.compare(PaymentStatusEnum.PAID.getCode(),orderOne.getPaymentStatus()) == 0) {
            log.info("payOrder..currentEid.{}..buyerEid.{1}",buyerEids);
            return Result.failed("订单已支付完成，请勿重复支付!");
        }
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            log.info("payOrder..buyerEid.{}",buyerEids);
            return Result.failed("订单已取消,请确认!");
        }
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        OnlineReceiptDeskPageVO vo = new OnlineReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.PAY.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList(PayChannelEnum.YEEPAY));
        Date customerConfirmTime = orderOne.getCustomerConfirmTime();
        long startTime = customerConfirmTime.getTime();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, -15);
        long endTime = nowTime.getTime().getTime();
        if (startTime <= endTime) {
            vo.setRemainTime("0分钟0秒");
        } else {
            int m = (int) (((startTime - endTime) % 86400000) % 3600000) / 60000;
            int s = (int) ((((startTime - endTime) % 86400000) % 3600000) % 60000) / 1000;
            vo.setRemainTime( m + "分钟" + s + "秒");
        }
        return Result.success(vo);
    }

    /**
     * 获取在线支付方式
     * @return
     */
    private List<OnlineReceiptDeskPageVO.PaymentChannelVO> getPaymentChannelVOList(PayChannelEnum payChannelEnum) {
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
            OnlineReceiptDeskPageVO.PaymentChannelVO channelVO = new OnlineReceiptDeskPageVO.PaymentChannelVO();
            channelVO.setCode(e.getValue());
            channelVO.setName(e.getLabel());
            channelVO.setUrl(e.getDescription());
            return channelVO;

        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "b2b在线支付收营台列表")
    @PostMapping("/receiptDeskOrderList")
    public Result<OnlineReceiptDeskPageVO> receiptDeskOrderList(@RequestBody @Valid QueryOnlineDeskOrderListForm form) {
        List<PayOrderDTO> resultList =  payApi.listPayOrdersByPayId(form.getPayId());

        if (CollectionUtil.isEmpty(resultList)) {
            return Result.failed("未查询到在线支付订单！");
        }
        // 在线支付订单,是否支付成功
        Boolean hasPayOrder = resultList.stream().filter(e -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(e.getAppOrderStatus())
        ).findAny().isPresent();

        if (hasPayOrder) {
            return Result.failed("订单已支付成功,请勿重复支付!");
        }

        switch (TradeTypeEnum.getByCode(resultList.get(0).getTradeType())) {
            case PAY:
                return this.payOrder(resultList);
            default: return Result.failed("未查询到在线支付订单");
        }
    }

    @ApiOperation(value = "在线支付选择支付渠道")
    @PostMapping("/select/channel")
    public Result<Map<String,Object>> selectChannel(@RequestBody @Valid OnlinePayForm form, HttpServletRequest request) {
        //适配移动端老版本
        if (ObjectUtil.equal(form.getPaySource(),"app")||ObjectUtil.equal(form.getPayWay(),"app")){
            form.setPayWay("yeePay");
            form.setPaySource("yeePayBank");
        }
        if (ObjectUtil.equal(form.getPaySource(), PaySourceEnum.YEE_PAY_WECHAT.getSource())&& StrUtil.isEmpty(form.getOpenId())){
            return Result.failed("微信支付时openId不能为空");
        }
        CreatePayTradeRequest createPayTradeRequest = PojoUtils.map(form,CreatePayTradeRequest.class);
        createPayTradeRequest.setOpUserId(0l);
        createPayTradeRequest.setUserIp(IPUtils.getIp(request));
        createPayTradeRequest.setTradeSource(TradeSourceEnum.SA_APP.getCode());
        createPayTradeRequest.setAppId(appId);

        // 查询在线支付渠道类型
        List<DictDataBO> dictDataBOList = dictTypeApi.getEnabledByTypeIdList(PAY_CHANNEL_TYPE_ID);
        if (CollectionUtil.isEmpty(dictDataBOList)) {

            return Result.failed("无可用在线支付方式");
        }
        dictDataBOList = dictDataBOList.stream().filter(t -> t.getStatus() == 1).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(dictDataBOList)) {

            return Result.failed("无可用在线支付方式");
        }
        Boolean checkPay =  dictDataBOList.stream().anyMatch(e -> e.getValue().equals(form.getPaySource()));
        if (!checkPay) {
            return Result.failed("当前支付方式不支持");
        }

        return payApi.createPayTrade(createPayTradeRequest);
    }

    @ApiOperation(value = "查询订单是否支付成功")
    @PostMapping("/query")
    public Result<Boolean> queryOrder(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "交易单号", required = true) @RequestParam("payNo") String payNo) {

        return payApi.orderQueryByPayNo(payNo);
    }

    @ApiOperation(value = "获取微信openId")
    @PostMapping("/getWechatOpenId")
    public Result<GetWechatOpenIdVO> getWechatOpenId(@Valid @RequestBody GetWechatOpenIdForm form) {

        // 请求参数
        String params = "appid=" + appId + "&secret=" + appSecret + "&js_code=" + form.getCode() + "&grant_type=" + grantType;
        // 发送请求
        HttpRequest request = HttpUtil.createGet(codeUrl+"?"+params);
        HttpResponse response = request.execute();
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(response.body());

        // 用户的唯一标识（openid）
        String openId = json.getString("openid");
        GetWechatOpenIdVO vo = new GetWechatOpenIdVO();
        vo.setOpenId(openId);
        return Result.success(vo);
    }
}
