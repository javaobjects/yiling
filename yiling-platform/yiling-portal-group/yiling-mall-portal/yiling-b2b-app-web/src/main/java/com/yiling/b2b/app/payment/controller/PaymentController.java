package com.yiling.b2b.app.payment.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yiling.b2b.app.payment.form.GetWechatOpenIdForm;
import com.yiling.b2b.app.payment.form.OnlinePayForm;
import com.yiling.b2b.app.payment.form.QueryReceiptDeskOrderListForm;
import com.yiling.b2b.app.payment.vo.GetWechatOpenIdVO;
import com.yiling.b2b.app.payment.vo.ReceiptDeskPageVO;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.annotations.UserAccessAuthentication;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.IPUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.PaySourceEnum;
import com.yiling.payment.enums.TradeSourceEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.PayOrderDTO;
import com.yiling.payment.pay.dto.request.CreatePayTradeRequest;
import com.yiling.user.enterprise.api.PaymentMethodApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.comparator.CompareUtil;
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

	@Value("${payment.weChat.codeUrl}")
	private String codeUrl;


    @DubboReference
    PaymentMethodApi paymentMethodApi;
    @DubboReference
    DictDataApi      dictTypeApi;
    @DubboReference
    PayApi           payApi;
    @DubboReference
    OrderApi        orderApi;
    
    @Autowired
    HttpServletRequest request;

    /**
     * 数据字段支付渠道,typeId
     */
    private final Long PAY_CHANNEL_TYPE_ID = 73l;

    /**
     * 交易订单收银台
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> payOrder(List<PayOrderDTO> resultList, Long currentEid) {
        List<Long> sellerEids = resultList.stream().map(PayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{}",currentEid,buyerEids);
            return Result.failed("未找到相关订单信息");
        }
      /*  List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(sellerEids.get(0),currentEid, PlatformEnum.B2B);
        if (CollUtil.isEmpty(paymentMethodDTOList)
                || !paymentMethodDTOList.stream().filter(e -> PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE ).findAny().isPresent()) {
            log.error("payOrder..",paymentMethodDTOList);
            return Result.failed("企业不支持在线支付的方式,请确认!");
        }*/
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        OrderDTO orderOne = orderApi.selectByOrderNo(orderDtoList.get(0));
        if (orderOne == null) {
            log.info("payOrder..currentEid.{}..buyerEid.{}",currentEid,buyerEids);
            return Result.failed("未找到相关订单信息");
        }

        if (CompareUtil.compare(PaymentStatusEnum.PAID.getCode(),orderOne.getPaymentStatus()) == 0) {
            log.info("payOrder..currentEid.{}..buyerEid.{}",currentEid,buyerEids);
            return Result.failed("订单已支付完成，请勿重复支付!");
        }
        if (OrderStatusEnum.CANCELED == OrderStatusEnum.getByCode(orderOne.getOrderStatus())) {
            log.info("payOrder..currentEid.{}..buyerEid.{}",currentEid,buyerEids);
            return Result.failed("订单已取消,请确认!");
        }
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setContentJson("");
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.PAY.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList());
        Date createTime = orderOne.getCreateTime();
        long startTime = createTime.getTime();
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
    private List<ReceiptDeskPageVO.PaymentChannelVO> getPaymentChannelVOList() {
        // 查询在线支付渠道类型
        List<DictDataBO> dictDataBOList = dictTypeApi.getEnabledByTypeIdList(PAY_CHANNEL_TYPE_ID);
        if (CollectionUtil.isEmpty(dictDataBOList)) {

            return ListUtil.empty();
        }
        dictDataBOList = dictDataBOList.stream().filter(t -> t.getStatus() == 1).collect(Collectors.toList());
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
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> backOrder( List<PayOrderDTO> resultList,Long currentEid) {
        List<Long> sellerEids = resultList.stream().map(PayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{}..buyerEid.{1}",currentEid,buyerEids);
            return Result.failed("未找到相关订单信息");
        }
      /*  List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(sellerEids.get(0),currentEid, PlatformEnum.B2B);
        if (CollUtil.isEmpty(paymentMethodDTOList)
                || !paymentMethodDTOList.stream().filter(e -> PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE ).findAny().isPresent()) {
            log.error("payOrder..",paymentMethodDTOList);
            return Result.failed("企业不支持在线支付的方式,请确认!");
        }*/
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips(resultList.get(0).getContent());
        vo.setContentJson("");
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.BACK.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList());
        return Result.success(vo);
    }

    /**
     * 会员购买
     * @param resultList
     * @param currentEid
     * @return
     */
    private Result<ReceiptDeskPageVO> memberOrder( List<PayOrderDTO> resultList,Long currentEid) {

        List<Long> sellerEids = resultList.stream().map(PayOrderDTO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> buyerEids = resultList.stream().map(PayOrderDTO::getBuyerEid).distinct().collect(Collectors.toList());
        if (!buyerEids.contains(currentEid)) {
            log.error("payOrder..currentEid.{0}..buyerEid.{1}",currentEid,buyerEids);
            return Result.failed("未找到相关订单信息");
        }
      /*  List<PaymentMethodDTO> paymentMethodDTOList = paymentMethodApi.listByEidAndCustomerEid(sellerEids.get(0),currentEid, PlatformEnum.B2B);
        if (CollUtil.isEmpty(paymentMethodDTOList)
                || !paymentMethodDTOList.stream().filter(e -> PaymentMethodEnum.getByCode(e.getCode()) == PaymentMethodEnum.ONLINE ).findAny().isPresent()) {
            log.error("payOrder..",paymentMethodDTOList);
            return Result.failed("企业不支持在线支付的方式,请确认!");
        }*/
        List<String> orderDtoList = resultList.stream().map(e -> e.getAppOrderNo()).collect(Collectors.toList());
        BigDecimal payMoney = resultList.stream().map(e -> e.getPayAmount()).reduce(BigDecimal::add).get();
        ReceiptDeskPageVO vo = new ReceiptDeskPageVO();
        vo.setPaymentAmount(payMoney);
        vo.setTips("");
        vo.setContentJson(resultList.get(0).getContent());
        vo.setOrderNoList(orderDtoList);
        vo.setTradeType(TradeTypeEnum.MEMBER.getCode());
        vo.setPayId(resultList.get(0).getPayId());
        vo.setPaymentChannelList(getPaymentChannelVOList());
        return Result.success(vo);

    }

    @ApiOperation(value = "收营台订单列表-已作废(迁移到支付平台接口文档)")
    @PostMapping("/receiptDeskOrderList")
    @UserAccessAuthentication(checkEnterpriseAuthStatus = true, checkEnterpriseStatus = true, checkUserEnterpriseRelation = true, checkUserStatus = true)
    @Deprecated
    public Result<ReceiptDeskPageVO> receiptDeskOrderList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryReceiptDeskOrderListForm form, HttpServletRequest request) {
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
                return this.payOrder(resultList,staffInfo.getCurrentEid());
            case BACK:
                return  this.backOrder(resultList,staffInfo.getCurrentEid());
            case MEMBER:
                return  this.memberOrder(resultList,staffInfo.getCurrentEid());
            case DEPOSIT:
            case BALANCE:
                default: return Result.failed("未查询到在线支付订单");
        }
		
    }

    @ApiOperation(value = "在线支付择支付方式-已作废(迁移到支付平台接口文档)")
    @PostMapping("/pay")
    @UserAccessAuthentication
    @Deprecated
    public Result<Map<String,Object>> pay(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid OnlinePayForm form) {
		//适配移动端老版本
		if (ObjectUtil.equal(form.getPaySource(),"app")||ObjectUtil.equal(form.getPayWay(),"app")){
			form.setPayWay("yeePay");
			form.setPaySource("yeePayBank");
		}
    	if (ObjectUtil.equal(form.getPaySource(), PaySourceEnum.YEE_PAY_WECHAT.getSource())&&StrUtil.isEmpty(form.getOpenId())){
			return Result.failed("微信支付时openId不能为空");
		}
        CreatePayTradeRequest createPayTradeRequest = PojoUtils.map(form,CreatePayTradeRequest.class);
        createPayTradeRequest.setOpUserId(staffInfo.getCurrentUserId());
        createPayTradeRequest.setUserIp(IPUtils.getIp(request));
        createPayTradeRequest.setTradeSource(TradeSourceEnum.B2B_APP.getCode());
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

       return payApi.createPayTrade(createPayTradeRequest);
    }

    @ApiOperation(value = "查询订单是否支付成功-已作废(迁移到支付平台接口文档)")
    @PostMapping("/query")
    @UserAccessAuthentication
    @Deprecated
    public Result<Boolean> queryOrder(@CurrentUser CurrentStaffInfo staffInfo, @ApiParam(value = "交易单号", required = true) @RequestParam("payNo") String payNo) {

        return payApi.orderQueryByPayNo(payNo);
    }

    @ApiOperation(value = "获取微信openId-已作废(迁移到支付平台接口文档)")
    @PostMapping("/getWechatOpenId")
    @Deprecated
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
