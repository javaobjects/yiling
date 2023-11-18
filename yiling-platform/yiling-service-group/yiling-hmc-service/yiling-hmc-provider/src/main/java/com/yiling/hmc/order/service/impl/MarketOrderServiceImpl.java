package com.yiling.hmc.order.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.PaymentStatusEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisDistributedLock;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.thread.SpringAsyncConfig;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.hmc.address.entity.AddressDO;
import com.yiling.hmc.address.service.AddressService;
import com.yiling.hmc.config.WxTemplateConfig;
import com.yiling.hmc.order.context.CreateMarketOrderContext;
import com.yiling.hmc.order.dao.MarketOrderMapper;
import com.yiling.hmc.order.dto.AdminMarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.dto.request.MarketOrderDeliveryRequest;
import com.yiling.hmc.order.dto.request.MarketOrderSaveRequest;
import com.yiling.hmc.order.dto.request.MarketPrescriptionOrderDeliveryRequest;
import com.yiling.hmc.order.dto.request.MarketPrescriptionOrderReceiveRequest;
import com.yiling.hmc.order.dto.request.QueryAdminMarkerOrderPageRequest;
import com.yiling.hmc.order.dto.request.QueryMarketOrderPageRequest;
import com.yiling.hmc.order.dto.request.UpdateMarketOrderRequest;
import com.yiling.hmc.order.entity.MarketOrderAddressDO;
import com.yiling.hmc.order.entity.MarketOrderDO;
import com.yiling.hmc.order.entity.MarketOrderDetailDO;
import com.yiling.hmc.order.enums.HmcCreateSourceEnum;
import com.yiling.hmc.order.enums.HmcMarketOrderTypeEnum;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.order.service.MarketOrderAddressService;
import com.yiling.hmc.order.service.MarketOrderDetailService;
import com.yiling.hmc.order.service.MarketOrderService;
import com.yiling.hmc.remind.dto.request.MedsRemindSubscribeRequest;
import com.yiling.hmc.remind.entity.MedsRemindSubscribeDO;
import com.yiling.hmc.remind.enums.HmcRemindSubscribeEnum;
import com.yiling.hmc.remind.service.MedsRemindSubscribeService;
import com.yiling.hmc.tencent.api.TencentIMApi;
import com.yiling.hmc.tencent.dto.request.MsgBodyDTO;
import com.yiling.hmc.tencent.dto.request.MsgContentDTO;
import com.yiling.hmc.tencent.dto.request.SendGroupMsgRequest;
import com.yiling.hmc.wechat.dto.request.HmcDiagnosisOrderPaySuccessNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderPayNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderRefundNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderSubmitRequest;
import com.yiling.hmc.wechat.dto.request.PrescriptionOrderSubmitRequest;
import com.yiling.hmc.wechat.enums.HmcDiagnosisMsgTypeEnum;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.api.HmcPrescriptionApi;
import com.yiling.ih.patient.dto.HmcDiagnosisRecordDetailDTO;
import com.yiling.ih.patient.dto.HmcMarketPrescriptionOrderDTO;
import com.yiling.ih.patient.dto.request.DiagnosisOrderPaySuccessNotifyRequest;
import com.yiling.ih.patient.dto.request.HmcCancelPrescriptionOrderRequest;
import com.yiling.ih.patient.dto.request.HmcCreateMarketPrescriptionOrderRequest;
import com.yiling.ih.patient.dto.request.HmcDiagnosisOrderDetailRequest;
import com.yiling.ih.patient.dto.request.HmcPrescriptionOrderDeliverRequest;
import com.yiling.ih.patient.dto.request.HmcPrescriptionOrderPaySuccessNotifyRequest;
import com.yiling.ih.patient.dto.request.HmcPrescriptionOrderReceiveRequest;
import com.yiling.ih.patient.dto.request.HmcPrescriptionOrderRefundNotifyRequest;
import com.yiling.order.order.api.NoApi;
import com.yiling.order.order.enums.NoEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.CreatePayOrderRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.bo.HmcUser;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;

/**
 * <p>
 * HMC订单表 服务实现类
 * </p>
 *
 * @author fan.shen
 * @date 2023-02-16
 */
@Slf4j
@Service
public class MarketOrderServiceImpl extends BaseServiceImpl<MarketOrderMapper, MarketOrderDO> implements MarketOrderService {

    @Value("${env.name}")
    private String envName;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    protected NoApi noApi;

    @DubboReference
    GoodsApi goodsApi;

    @Autowired
    MarketOrderDetailService orderDetailService;

    @DubboReference
    PayApi payApi;

    @Autowired
    AddressService addressService;

    @Autowired
    MarketOrderAddressService orderAddressService;

    @Autowired
    RedisDistributedLock redisDistributedLock;

    @Autowired
    RedisService redisService;

    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Lazy
    @Autowired
    MarketOrderServiceImpl _this;

    @Autowired
    WxTemplateConfig templateConfig;

    @Autowired
    WxMaService wxMaService;

    @DubboReference
    HmcUserApi hmcUserApi;

    @Autowired
    MedsRemindSubscribeService subscribeService;

    @Autowired
    private SpringAsyncConfig springAsyncConfig;

    @DubboReference
    DictApi dictApi;

    @DubboReference
    HmcPrescriptionApi prescriptionApi;

    @DubboReference
    HmcDiagnosisApi diagnosisApi;

    @DubboReference
    TencentIMApi tencentIMApi;

    @Override
    public MarketOrderDTO createOrder(MarketOrderSubmitRequest request) {
        CreateMarketOrderContext marketOrderContext = _this.createMarketOrder(request);
        log.info("service.createOrder result:{}", JSONUtil.toJsonStr(marketOrderContext));
        if (Objects.nonNull(marketOrderContext.getCreateOrderMq())) {
            log.info("准备发送创建市场订单mq....参数:{}, mqId:{}", JSONUtil.toJsonStr(marketOrderContext.getCreateOrderMq()), marketOrderContext.getCreateOrderMq().getId());
            mqMessageSendApi.send(marketOrderContext.getCreateOrderMq());
        }
        return marketOrderContext.getOrderDTO();
    }

    @Override
    public MarketOrderDTO createPrescriptionOrder(PrescriptionOrderSubmitRequest request) {
        CreateMarketOrderContext marketOrderContext = _this.createPrescriptionOrderHandler(request);
        log.info("service.createPrescriptionOrder result:{}", JSONUtil.toJsonStr(marketOrderContext));
        if (Objects.nonNull(marketOrderContext.getCreateOrderMq())) {
            log.info("准备发送创建市场订单mq....参数:{}, mqId:{}", JSONUtil.toJsonStr(marketOrderContext.getCreateOrderMq()), marketOrderContext.getCreateOrderMq().getId());
            mqMessageSendApi.send(marketOrderContext.getCreateOrderMq());
        }
        return marketOrderContext.getOrderDTO();
    }

    @GlobalTransactional
    public CreateMarketOrderContext createPrescriptionOrderHandler(PrescriptionOrderSubmitRequest request) {
        // 校验同一个处方 -> 是否已经存在处方订单，如果存在 && 待支付，则调用支付接口，获取支付ticket，返回
        MarketOrderDO checkIsExists = this.getByPrescriptionId(request.getPrescriptionId());
        log.info("checkIsExists 结果:{}", JSONUtil.toJsonStr(checkIsExists));
        if (Objects.nonNull(checkIsExists) && HmcOrderStatusEnum.UN_PAY.getCode().equals(checkIsExists.getOrderStatus()) && PaymentStatusEnum.UNPAID.getCode().equals(checkIsExists.getPaymentStatus())) {
            log.info("根据处方id获取到订单，直接发起支付，入参：{}", request);
            // 创建支付订单
            CreatePayOrderRequest payOrderRequest = buildPayOrderRequest(checkIsExists, TradeTypeEnum.PRESCRIPTION);
            Result<String> payOrder = payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
            log.info("创建支付订单入参：{},返回参数:{}", JSONUtil.toJsonStr(payOrderRequest), JSONUtil.toJsonStr(payOrder));

            MarketOrderDTO marketOrderDTO = PojoUtils.map(checkIsExists, MarketOrderDTO.class);
            marketOrderDTO.setPayTicket(payOrder.getData());

            CreateMarketOrderContext marketOrderContext = new CreateMarketOrderContext();
            marketOrderContext.setOrderDTO(marketOrderDTO);
            log.info("根据处方id获取到订单，直接发起支付，返参：{}", request);
            return marketOrderContext;
        }

        // 1、创建订单
        MarketOrderDO marketOrderDO = buildMarketPrescriptionOrderDO(request);
        this.save(marketOrderDO);

        // 2、保存收货地址信息
        MarketOrderAddressDO orderAddressDO = buildMarketPrescriptionOrderAddress(marketOrderDO, request);
        orderAddressService.save(orderAddressDO);

        // 3、调用IH接口创建处方订单
        callIHToCreatePrescriptionOrder(request, marketOrderDO, orderAddressDO);

        // 4、创建支付订单
        CreatePayOrderRequest payOrderRequest = buildPayOrderRequest(marketOrderDO, TradeTypeEnum.PRESCRIPTION);
        Result<String> payOrder = payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
        log.info("创建支付订单入参：{},返回参数:{}", JSONUtil.toJsonStr(payOrderRequest), JSONUtil.toJsonStr(payOrder));

        // 5、准备延迟消息
        MqMessageBO createOrderMq = new MqMessageBO(Constants.TOPIC_HMC_CREATE_MARKET_PRE_ORDER, "", marketOrderDO.getOrderNo(), MqDelayLevel.THIRTY_MINUTES);
        createOrderMq = mqMessageSendApi.prepare(createOrderMq);

        MarketOrderDTO marketOrderDTO = PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
        marketOrderDTO.setPayTicket(payOrder.getData());

        CreateMarketOrderContext marketOrderContext = new CreateMarketOrderContext();
        marketOrderContext.setOrderDTO(marketOrderDTO);
        marketOrderContext.setCreateOrderMq(createOrderMq);
        return marketOrderContext;
    }

    /**
     * 调用IH接口创建处方订单
     *
     * @param request
     */
    public void callIHToCreatePrescriptionOrder(PrescriptionOrderSubmitRequest request, MarketOrderDO marketOrderDO, MarketOrderAddressDO orderAddressDO) {
        HmcCreateMarketPrescriptionOrderRequest cratePreOrderRequest = new HmcCreateMarketPrescriptionOrderRequest();
        cratePreOrderRequest.setPrescriptionId(request.getPrescriptionId());
        cratePreOrderRequest.setPrescriptionPrice(String.valueOf(request.getPrescriptionPrice()));
        cratePreOrderRequest.setAddressId(request.getAddressId().intValue());
        cratePreOrderRequest.setAddressName(orderAddressDO.getName());
        cratePreOrderRequest.setAddressMobile(orderAddressDO.getMobile());
        cratePreOrderRequest.setAddressArea(orderAddressDO.getAddress());
        cratePreOrderRequest.setDeliveryPharmacyId(request.getIhEid().intValue());
        cratePreOrderRequest.setNote(request.getRemark());
        cratePreOrderRequest.setFromUserId(request.getOpUserId().intValue());

        HmcMarketPrescriptionOrderDTO prescriptionOrder = prescriptionApi.createPrescriptionOrder(cratePreOrderRequest);

        if (Objects.isNull(prescriptionOrder)) {
            throw new BusinessException(HmcOrderErrorCode.MARKET_ORDER_PRE_ERROR);
        }

        // 保存IH返回参数
        marketOrderDO.setIhOrderId(Long.valueOf(prescriptionOrder.getOrderId()));
        marketOrderDO.setIhPrescriptionNo(prescriptionOrder.getPrescriptionNo());
        marketOrderDO.setIhPrescriptionOrderNo(prescriptionOrder.getPrescriptionOrderNo());
        this.updateById(marketOrderDO);

    }

    /**
     * 构建处方订单
     *
     * @param request
     * @return
     */
    public MarketOrderDO buildMarketPrescriptionOrderDO(PrescriptionOrderSubmitRequest request) {
        MarketOrderDO marketOrderDO = new MarketOrderDO();
        marketOrderDO.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        marketOrderDO.setMarketOrderType(HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode());
        marketOrderDO.setCreateUser(request.getOpUserId());
        marketOrderDO.setUpdateUser(request.getOpUserId());
        marketOrderDO.setCreateTime(DateUtil.date());
        marketOrderDO.setUpdateTime(DateUtil.date());
        marketOrderDO.setRemark(request.getRemark());


        marketOrderDO.setPaymentMethod(request.getPaymentMethodEnum().getCode());
        marketOrderDO.setPaymentStatus(request.getPaymentStatusEnum().getCode());
        marketOrderDO.setDeliverType(request.getDeliveryType().getCode());
        marketOrderDO.setCreateSource(request.getCreateSource().getCode());
        BigDecimal freightAmount = BigDecimal.ZERO;
        marketOrderDO.setFreightAmount(freightAmount);

        marketOrderDO.setGoodsTotalAmount(request.getPrescriptionPrice());
        // 订单总额 = 运费 + 商品总额
        marketOrderDO.setOrderTotalAmount(request.getPrescriptionPrice().add(freightAmount));
        marketOrderDO.setCreateSource(HmcCreateSourceEnum.HMC_MP.getCode());

        marketOrderDO.setOrderStatus(request.getHmcOrderStatus().getCode());
        marketOrderDO.setPrescriptionId(Long.valueOf(request.getPrescriptionId()));
        marketOrderDO.setEid(request.getEid());
        marketOrderDO.setEname(request.getEname());
        marketOrderDO.setIhEid(request.getIhEid());
        marketOrderDO.setIhEname(request.getIhEname());
        marketOrderDO.setIhPharmacySource(request.getIhPharmacySource());
        marketOrderDO.setPrescriptionType(request.getPrescriptionType());

        return marketOrderDO;
    }

    @GlobalTransactional
    public CreateMarketOrderContext createMarketOrder(MarketOrderSubmitRequest request) {
        // 1、创建订单
        MarketOrderDO marketOrderDO = buildMarketOrderDO(request);
        this.save(marketOrderDO);

        // 2、创建订单明细
        List<MarketOrderDetailDO> marketOrderDetailDOList = buildMarketOrderDetail(request, marketOrderDO);
        orderDetailService.saveBatch(marketOrderDetailDOList);

        // 3、保存收货地址信息
        MarketOrderAddressDO orderAddressDO = buildMarketOrderAddress(marketOrderDO, request);
        orderAddressService.save(orderAddressDO);

        // 4、创建支付订单
        CreatePayOrderRequest payOrderRequest = buildPayOrderRequest(marketOrderDO, TradeTypeEnum.PAY);
        Result<String> payOrder = payApi.createPayOrder(OrderPlatformEnum.HMC, payOrderRequest);
        log.info("创建支付订单入参：{},返回参数:{}", JSONUtil.toJsonStr(payOrderRequest), JSONUtil.toJsonStr(payOrder));

        // 5、准备延迟消息
        MqMessageBO createOrderMq = new MqMessageBO(Constants.TOPIC_HMC_CREATE_MARKET_ORDER, "", marketOrderDO.getOrderNo(), MqDelayLevel.THIRTY_MINUTES);
        createOrderMq = mqMessageSendApi.prepare(createOrderMq);

        MarketOrderDTO marketOrderDTO = PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
        marketOrderDTO.setPayTicket(payOrder.getData());

        CreateMarketOrderContext marketOrderContext = new CreateMarketOrderContext();
        marketOrderContext.setOrderDTO(marketOrderDTO);
        marketOrderContext.setCreateOrderMq(createOrderMq);
        return marketOrderContext;
    }

    /**
     * 构建订单地址信息
     *
     * @param marketOrderDO
     * @param request
     * @return
     */
    private MarketOrderAddressDO buildMarketOrderAddress(MarketOrderDO marketOrderDO, MarketOrderSubmitRequest request) {
        MarketOrderAddressDO orderAddressDO = PojoUtils.map(request, MarketOrderAddressDO.class);
        AddressDO addressDO = addressService.getById(request.getAddressId());
        Assert.notNull(addressDO, "未获取到地址对象");
        orderAddressDO.setOrderId(marketOrderDO.getId());
        orderAddressDO.setName(addressDO.getName());
        orderAddressDO.setMobile(addressDO.getMobile());
        orderAddressDO.setAddressId(request.getAddressId());
        String address = addressDO.getProvinceName() + " " + addressDO.getCityName() + " " + addressDO.getRegionName() + " " + addressDO.getAddress();
        orderAddressDO.setAddress(address);
        return orderAddressDO;
    }

    /**
     * 构建处方订单地址信息
     *
     * @param marketOrderDO
     * @param request
     * @return
     */
    public MarketOrderAddressDO buildMarketPrescriptionOrderAddress(MarketOrderDO marketOrderDO, PrescriptionOrderSubmitRequest request) {
        MarketOrderAddressDO orderAddressDO = PojoUtils.map(request, MarketOrderAddressDO.class);
        AddressDO addressDO = addressService.getById(request.getAddressId());
        Assert.notNull(addressDO, "未获取到地址对象");
        orderAddressDO.setOrderId(marketOrderDO.getId());
        orderAddressDO.setName(addressDO.getName());
        orderAddressDO.setMobile(addressDO.getMobile());
        orderAddressDO.setAddressId(request.getAddressId());
        String address = addressDO.getProvinceName() + " " + addressDO.getCityName() + " " + addressDO.getRegionName() + " " + addressDO.getAddress();
        orderAddressDO.setAddress(address);
        return orderAddressDO;
    }

    @Override
    public MarketOrderDTO queryById(Long orderId) {
        MarketOrderDO marketOrderDO = this.getById(orderId);
        return PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
    }

    @Override
    public List<MarketOrderDTO> queryByIdList(List<Long> orderIdList) {
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MarketOrderDO::getId, orderIdList);
        List<MarketOrderDO> list = this.list(wrapper);
        return PojoUtils.map(list, MarketOrderDTO.class);
    }

    @Override
    public MarketOrderDTO queryByOrderNo(String body) {
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketOrderDO::getOrderNo, body);
        MarketOrderDO marketOrderDO = this.getOne(wrapper);
        return PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
    }

    @Override
    public Page<MarketOrderDTO> queryMarketOrderPage(QueryMarketOrderPageRequest request) {
        LambdaQueryWrapper<MarketOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MarketOrderDO::getCreateUser, request.getOpUserId()).eq(Objects.nonNull(request.getOrderStatus()) && request.getOrderStatus() > 0, MarketOrderDO::getOrderStatus, request.getOrderStatus());
        wrapper.orderByDesc(MarketOrderDO::getCreateTime);
        Page<MarketOrderDO> page = this.page(request.getPage(), wrapper);
        return PojoUtils.map(page, MarketOrderDTO.class);
    }

    @Override
    public Boolean updateMarketOrder(UpdateMarketOrderRequest request) {
        MarketOrderDO marketOrderDO = this.getById(request.getId());
        marketOrderDO.setOrderStatus(request.getOrderStatus());
        marketOrderDO.setUpdateUser(request.getOpUserId());
        boolean b = this.updateById(marketOrderDO);

        // 取消 & 处方订单 -> 通知IH取消
        if (HmcOrderStatusEnum.CANCELED.getCode().equals(request.getOrderStatus()) && HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDO.getMarketOrderType())) {
            HmcCancelPrescriptionOrderRequest cancelPrescriptionOrderRequest = new HmcCancelPrescriptionOrderRequest();
            cancelPrescriptionOrderRequest.setOrderId(marketOrderDO.getIhOrderId().intValue());
            log.info("调用IH接口取消订单，入参:{}", JSONUtil.toJsonStr(cancelPrescriptionOrderRequest));
            prescriptionApi.cancelPrescriptionOrder(cancelPrescriptionOrderRequest);
        }

        // 签收 & 处方订单 -> 通知IH签收
        if (HmcOrderStatusEnum.RECEIVED.getCode().equals(request.getOrderStatus()) && HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDO.getMarketOrderType())) {
            HmcPrescriptionOrderReceiveRequest receiveIHRequest = new HmcPrescriptionOrderReceiveRequest();
            receiveIHRequest.setOrderId(marketOrderDO.getIhOrderId().intValue());
            receiveIHRequest.setFromUserId(request.getOpUserId().intValue());
            log.info("调用IH接口签收订单，入参:{}", JSONUtil.toJsonStr(receiveIHRequest));
            prescriptionApi.prescriptionOrderReceive(receiveIHRequest);
        }

        return b;
    }


    @Override
    public Page<AdminMarketOrderDTO> queryAdminMarketOrderPage(QueryAdminMarkerOrderPageRequest request) {
        return this.baseMapper.queryAdminMarketOrderPage(request.getPage(), request);
    }

    @Override
    public void sendMsg(Long orderId) {
        MarketOrderDO marketOrderDO = this.getById(orderId);
        if (Objects.isNull(marketOrderDO)) {
            return;
        }

        // 获取订阅用户信息，如果已经取消订阅，则取消发送
        MedsRemindSubscribeDO subscribeDO = subscribeService.getByUserIdAndTemplateId(marketOrderDO.getCreateUser(), templateConfig.getBaZiBuShenDeliverMsgTemplate());
        if (Objects.nonNull(subscribeDO) && HmcRemindSubscribeEnum.REJECT.getType().equals(subscribeDO.getSubscribeStatus())) {
            log.info("当前用户已取消订阅，跳过处理，用户id：{}，模板id：{}", marketOrderDO.getCreateUser(), templateConfig.getMedsReminder());
            return;
        }
        HmcUser hmcUser = hmcUserApi.getByIdAndAppId(marketOrderDO.getCreateUser(), wxMaService.getWxMaConfig().getAppid());
        if (Objects.isNull(hmcUser)) {
            log.info("未获取到用户信息，跳过处理，用户id：{}", marketOrderDO.getCreateUser());
            return;
        }
        List<MarketOrderDetailDTO> marketOrderDetailDTOS = orderDetailService.queryByOrderIdList(Arrays.asList(orderId));
        MarketOrderDetailDTO marketOrderDetailDTO = marketOrderDetailDTOS.get(0);
        WxMaSubscribeMessage msg = new WxMaSubscribeMessage();
        try {
            WxMaSubscribeMessage.MsgData thing1 = new WxMaSubscribeMessage.MsgData();
            thing1.setName("thing1");
            String goodsName = marketOrderDetailDTO.getGoodsName();
            if (StrUtil.length(goodsName) > 20) {
                goodsName = StrUtil.sub(goodsName, 0, 16) + "...";
            }
            thing1.setValue(goodsName);

            WxMaSubscribeMessage.MsgData characterString3 = new WxMaSubscribeMessage.MsgData();
            characterString3.setName("character_string3");
            characterString3.setValue(marketOrderDO.getDeliverNo());

            WxMaSubscribeMessage.MsgData thing6 = new WxMaSubscribeMessage.MsgData();
            thing6.setName("thing6");
            DictBO dict = dictApi.getDictByName("hmc_market_order_deliver_company");
            Map<String, String> dictMap = dict.getDataList().stream().collect(Collectors.toMap(DictBO.DictData::getValue, DictBO.DictData::getLabel));
            thing6.setValue(dictMap.get(marketOrderDO.getDeliverCompanyName()));

            WxMaSubscribeMessage.MsgData characterString5 = new WxMaSubscribeMessage.MsgData();
            characterString5.setName("character_string5");
            characterString5.setValue(marketOrderDO.getOrderNo());

            msg.setTemplateId(templateConfig.getBaZiBuShenDeliverMsgTemplate());
            msg.addData(thing1).addData(characterString3).addData(thing6).addData(characterString5);

            msg.setToUser(hmcUser.getMiniProgramOpenId());
            msg.setMiniprogramState(templateConfig.getMiniProgramState());
            msg.setPage("pagesSub/my/order/orderDetail/index?orderId=" + marketOrderDO.getId());
            log.info("[MarketOrderServiceImpl]发送微信订阅消息:{}", JSONUtil.toJsonStr(msg));
            wxMaService.getMsgService().sendSubscribeMsg(msg);
        } catch (WxErrorException e) {
            log.error("[MarketOrderServiceImpl]推送发货通知微信订阅消息报错：{}", ExceptionUtils.getStackTrace(e));
            // 错误代码：43101, 错误信息：用户拒绝接受消息，如果用户之前曾经订阅过，则表示用户取消了订阅关系
            if (e.getError().getErrorCode() == 43101) {
                MedsRemindSubscribeRequest request = new MedsRemindSubscribeRequest();
                request.setOpenId(hmcUser.getMiniProgramOpenId());
                request.setAppId(this.wxMaService.getWxMaConfig().getAppid());
                request.setTemplateId(templateConfig.getBaZiBuShenDeliverMsgTemplate());
                request.setSubscribeStatus(HmcRemindSubscribeEnum.REJECT.getType());
                request.setUserId(hmcUser.getUserId());
                log.info("[MarketOrderServiceImpl]推送消息返回43101，准备构建取消订阅记录,参数:{}", JSONUtil.toJsonStr(request));
                this.subscribeService.saveOrUpdateRemindSub(Collections.singletonList(request));
            }
        }
    }

    @Override
    public Boolean payNotify(MarketOrderPayNotifyRequest payNotifyRequest) {
        log.info("[payNotify]入参:{}", JSONUtil.toJsonStr(payNotifyRequest));
        Assert.notNull(payNotifyRequest.getOrderId(), "订单id（必传）[orderId]");
        Assert.notNull(payNotifyRequest.getThirdPayNo(), "第三方支付单号（必传）[thirdPayNo]");
        Assert.notNull(payNotifyRequest.getThirdPayAmount(), "第三方支付金额（必传）[thirdPayAmount]");
        Assert.notNull(payNotifyRequest.getPayTime(), "支付时间（必传）[payTime]");

        String lockName = payNotifyRequest.getOrderId().toString();
        String lockId = null;
        try {
            lockId = redisDistributedLock.lock2(lockName, 10, 10, TimeUnit.SECONDS);
            MarketOrderDO marketOrderDO = this.getById(payNotifyRequest.getOrderId());
            Assert.notNull(marketOrderDO, "根据orderId未获取到订单信息");

            // 校验订单状态
            if (!marketOrderDO.getPaymentStatus().equals(HmcPaymentStatusEnum.UN_PAY.getCode())) {
                log.error("当前订单支付状态非待支付");
                return Boolean.FALSE;
            }

            // 校验订单金额
            // if (marketOrderDO.getOrderTotalAmount().compareTo(payNotifyRequest.getThirdPayAmount()) != 0) {
            //     log.error("支付回调传入金额不对等");
            //     return Boolean.FALSE;
            // }
            marketOrderDO.setMerTranNo(payNotifyRequest.getMerTranNo());
            marketOrderDO.setThirdPayNo(payNotifyRequest.getThirdPayNo());
            marketOrderDO.setThirdPayAmount(payNotifyRequest.getThirdPayAmount());
            marketOrderDO.setPaymentStatus(HmcPaymentStatusEnum.PAYED.getCode());
            marketOrderDO.setOrderStatus(HmcOrderStatusEnum.UN_DELIVERED.getCode());
            marketOrderDO.setPayTime(payNotifyRequest.getPayTime());

            // 1、更新hmc订单
            this.updateById(marketOrderDO);

            // 2、如果是处方订单 -> 调用IH接口，更新订单状态
            if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDO.getMarketOrderType())) {
                HmcPrescriptionOrderPaySuccessNotifyRequest payNotifyIHRequest = new HmcPrescriptionOrderPaySuccessNotifyRequest();
                payNotifyIHRequest.setOrderId(marketOrderDO.getIhOrderId().intValue());
                payNotifyIHRequest.setHmcOrderNo(payNotifyRequest.getMerTranNo());
                payNotifyIHRequest.setOutOrderNo(payNotifyRequest.getThirdPayNo());
                payNotifyIHRequest.setThirdPartyTranNo(payNotifyRequest.getThirdPartyTranNo());
                prescriptionApi.prescriptionOrderPaySuccess(payNotifyIHRequest);
            }

            return Boolean.TRUE;

        } catch (Exception e) {
            log.error("系统繁忙，请稍后操作：{}", e.getMessage(), e);
            return Boolean.FALSE;
        } finally {
            redisDistributedLock.releaseLock(lockName, lockId);
        }
    }

    @Override
    public Boolean diagnosisOrderPayNotify(HmcDiagnosisOrderPaySuccessNotifyRequest payNotifyRequest) {
        log.info("[diagnosisOrderPayNotify]问诊订单支付成功回调HMC通知入参:{}", JSONUtil.toJsonStr(payNotifyRequest));
        HmcDiagnosisOrderDetailRequest detailRequest = new HmcDiagnosisOrderDetailRequest();
        detailRequest.setDiagnosisRecordId(payNotifyRequest.getDiagnosisRecordId());
        HmcDiagnosisRecordDetailDTO diagnosisOrder = diagnosisApi.getDiagnosisOrderDetailById(detailRequest);
        SendGroupMsgRequest sendMsgToDoctorRequest = buildMsg(diagnosisOrder);
        DiagnosisOrderPaySuccessNotifyRequest notifyRequest = PojoUtils.map(payNotifyRequest, DiagnosisOrderPaySuccessNotifyRequest.class);
        notifyRequest.setGroupId(sendMsgToDoctorRequest.getGroupId());
        log.info("[diagnosisOrderPayNotify]问诊单支付回调IH接口入参:{}", JSONUtil.toJsonStr(notifyRequest));
        Boolean res = diagnosisApi.diagnosisOrderPayNotify(notifyRequest);
        log.info("[diagnosisOrderPayNotify]问诊单支付回调IH接口返参:{}", res);
        if (res) {
            log.info("准备发送消息卡片，构建消息入参:{}", JSONUtil.toJsonStr(sendMsgToDoctorRequest));
            // 发送消息卡片
            tencentIMApi.sendGroupMsg(sendMsgToDoctorRequest);
        }
        return res;
    }

    /**
     * 构建消息体
     *
     * @param diagnosisOrder
     * @return
     */
    public SendGroupMsgRequest buildMsg(HmcDiagnosisRecordDetailDTO diagnosisOrder) {

        String userId = envName + "_" + "HMC_" + diagnosisOrder.getHmcUserId();
        String docId = envName + "_" + "IH_DOC_" + diagnosisOrder.getDoctorId();
        Integer patientId = diagnosisOrder.getPatientId();
        String redisKey = userId + "|" + patientId + "|" + docId;
        if (!redisService.hasKey(redisKey)) {
            throw new BusinessException(HmcOrderErrorCode.IM_GET_REDIS_GROUP_ERROR);
        }
        List<MsgBodyDTO> MsgBody = new ArrayList<>();
        MsgBodyDTO msgBodyDTO = new MsgBodyDTO();
        msgBodyDTO.setMsgType("TIMCustomElem");
        MsgContentDTO MsgContent = new MsgContentDTO();
        Map<String, Object> customData = Maps.newHashMap();
        String patientName = IdcardUtil.hide(diagnosisOrder.getPatientName(), 0, 1);
        // 就诊患者性别 0女 1男
        String gender = diagnosisOrder.getPatientGender() == 1 ? "男" : "女";
        Integer patientAge = diagnosisOrder.getPatientAge();
        String diseaseDescribe = diagnosisOrder.getDiseaseDescribePicture().get(0).getDiseaseDescribe();
        customData.put("diagnosisPatient", patientName + "，" + gender + "," + patientAge + "岁");
        customData.put("symptom", diseaseDescribe);
        customData.put("diagnosisRecordId", diagnosisOrder.getDiagnosisRecordId().toString());
        // 问诊类型，0图文1音频2视频
        Integer type = diagnosisOrder.getType();
        if (type == 0) {
            MsgContent.setData(HmcDiagnosisMsgTypeEnum.DIAGNOSIS_PAY_TIP.getCode());
        } else {
            MsgContent.setData(HmcDiagnosisMsgTypeEnum.DIAGNOSIS_VIDEO_PAY_TIP.getCode());
            Date diagnosisRecordCreateTime = diagnosisOrder.getDiagnosisRecordCreateTime();
            String date = DateUtil.formatDate(diagnosisRecordCreateTime);
            String startTime = DateUtil.format(diagnosisRecordCreateTime, "HH:mm");
            String diagnosisTime = date + " " + startTime;
            customData.put("diagnosisRecordStartTime", diagnosisTime);
        }
        MsgContent.setDesc(JSONUtil.toJsonStr(customData));
        msgBodyDTO.setMsgContent(MsgContent);
        MsgBody.add(msgBodyDTO);

        String group = redisService.get(redisKey).toString();
        SendGroupMsgRequest groupMsgRequest = new SendGroupMsgRequest();
        groupMsgRequest.setGroupId(group.substring(5));
        groupMsgRequest.setFrom_Account(userId);
        groupMsgRequest.setRandom(RandomUtil.randomInt(10000, 1000000));
        groupMsgRequest.setMsgBody(MsgBody);

        return groupMsgRequest;
    }

    @Override
    @GlobalTransactional
    public Boolean prescriptionOrderRefundNotify(MarketOrderRefundNotifyRequest refundNotifyRequest) {
        log.info("[prescriptionOrderRefundNotify]处方订单退款回调入参:{}", JSONUtil.toJsonStr(refundNotifyRequest));
        Assert.notNull(refundNotifyRequest.getOrderId(), "订单id（必传）[orderId]");
        Assert.notNull(refundNotifyRequest.getThirdPayNo(), "第三方支付单号（必传）[thirdPayNo]");
        Assert.notNull(refundNotifyRequest.getThirdPayAmount(), "第三方支付金额（必传）[thirdPayAmount]");
        Assert.notNull(refundNotifyRequest.getRefundTime(), "退款时间（必传）[refundTime]");
        Assert.notNull(refundNotifyRequest.getSysOrderNo(), "交行内部订单号（必传）[sysOrderNo]");

        MarketOrderDO marketOrderDO = this.getById(refundNotifyRequest.getOrderId());
        Assert.notNull(marketOrderDO, "根据orderId未获取到订单信息");

        if (!HmcPaymentStatusEnum.PAYED.getCode().equals(marketOrderDO.getPaymentStatus())) {
            log.info("当前订单未付款，请勿执行退款操作！");
            return Boolean.FALSE;
        }
        if (HmcPaymentStatusEnum.REFUND.getCode().equals(marketOrderDO.getPaymentStatus())) {
            log.info("当前订单已全部退款，请勿重复操作！");
            return Boolean.FALSE;
        }

        // 已全部退款
        marketOrderDO.setPaymentStatus(HmcPaymentStatusEnum.REFUND.getCode());
        // 已取消
        marketOrderDO.setOrderStatus(HmcOrderStatusEnum.CANCELED.getCode());

        // 1、更新hmc订单
        this.updateById(marketOrderDO);

        // 2、调用IH接口 -> 通知退款完成
        HmcPrescriptionOrderRefundNotifyRequest refundNotifyRequest1 = new HmcPrescriptionOrderRefundNotifyRequest();
        refundNotifyRequest1.setIhOrderId(marketOrderDO.getIhOrderId());
        refundNotifyRequest1.setRefundId(refundNotifyRequest.getRefundId());
        refundNotifyRequest1.setRefundTime(refundNotifyRequest.getRefundTime());
        refundNotifyRequest1.setThirdPayNo(refundNotifyRequest.getThirdPayNo());
        refundNotifyRequest1.setThirdPayAmount(refundNotifyRequest.getThirdPayAmount());
        refundNotifyRequest1.setCancelStatus(refundNotifyRequest.getCancelStatus());
        refundNotifyRequest1.setSysOrderNo(refundNotifyRequest.getSysOrderNo());
        log.info("[prescriptionOrderRefundNotify]调用IH处方退款回调入参:{}", JSONUtil.toJsonStr(refundNotifyRequest1));
        boolean refundNotifyResult = prescriptionApi.prescriptionOrderRefundNotify(refundNotifyRequest1);
        if (refundNotifyResult) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 创建支付订单
     *
     * @param marketOrderDO
     * @return
     */
    public CreatePayOrderRequest buildPayOrderRequest(MarketOrderDO marketOrderDO, TradeTypeEnum tradeTypeEnum) {
        CreatePayOrderRequest payOrderRequest = new CreatePayOrderRequest();
        payOrderRequest.setTradeType(tradeTypeEnum);

        List<CreatePayOrderRequest.appOrderRequest> appOrderList = Lists.newArrayList();
        CreatePayOrderRequest.appOrderRequest appOrderRequest = new CreatePayOrderRequest.appOrderRequest();
        appOrderRequest.setAppOrderId(marketOrderDO.getId());
        appOrderRequest.setAppOrderNo(marketOrderDO.getOrderNo());
        appOrderRequest.setAmount(marketOrderDO.getOrderTotalAmount());
        appOrderRequest.setUserId(marketOrderDO.getCreateUser());
        appOrderRequest.setSellerEid(marketOrderDO.getEid());

        appOrderList.add(appOrderRequest);
        payOrderRequest.setAppOrderList(appOrderList);
        return payOrderRequest;
    }

    /**
     * 构建订单明细
     *
     * @param request
     * @param marketOrderDO
     * @return
     */
    private List<MarketOrderDetailDO> buildMarketOrderDetail(MarketOrderSubmitRequest request, MarketOrderDO marketOrderDO) {
        List<MarketOrderDetailDO> orderDetailDOList = Lists.newArrayList();
        MarketOrderDetailDO orderDetailDO = PojoUtils.map(request, MarketOrderDetailDO.class);
        orderDetailDOList.add(orderDetailDO);

        orderDetailDO.setOrderId(marketOrderDO.getId());
        orderDetailDO.setOrderNo(marketOrderDO.getOrderNo());

        GoodsDTO goodsDTO = goodsApi.queryInfo(request.getGoodsId());
        orderDetailDO.setStandardId(goodsDTO.getStandardId());
        orderDetailDO.setSellSpecificationsId(goodsDTO.getSellSpecificationsId());
        orderDetailDO.setGoodsId(goodsDTO.getId());
        orderDetailDO.setGoodsName(goodsDTO.getName());
        orderDetailDO.setGoodsSpecification(goodsDTO.getSellSpecifications());
        orderDetailDO.setGoodsPrice(goodsDTO.getPrice());
        orderDetailDO.setGoodsQuantity(request.getGoodsQuantity());
        orderDetailDO.setGoodsAmount(goodsDTO.getPrice().multiply(BigDecimal.valueOf(request.getGoodsQuantity())));

        return orderDetailDOList;
    }

    /**
     * 构建市场订单
     *
     * @param request
     * @return
     */
    private MarketOrderDO buildMarketOrderDO(MarketOrderSubmitRequest request) {
        MarketOrderDO marketOrderDO = new MarketOrderDO();
        marketOrderDO.setOrderNo(noApi.gen(NoEnum.ORDER_NO));
        marketOrderDO.setMarketOrderType(HmcMarketOrderTypeEnum.BA_ZI_ORDER.getCode());
        marketOrderDO.setOrderStatus(request.getHmcOrderStatus().getCode());
        marketOrderDO.setEid(request.getEid());
        marketOrderDO.setCreateUser(request.getOpUserId());
        marketOrderDO.setUpdateUser(request.getOpUserId());
        marketOrderDO.setCreateTime(DateUtil.date());
        marketOrderDO.setUpdateTime(DateUtil.date());
        marketOrderDO.setActivityId(request.getActivityId());
        marketOrderDO.setDoctorId(request.getDoctorId());
        marketOrderDO.setRemark(request.getRemark());

        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(request.getEid());
        marketOrderDO.setEname(enterpriseDTO.getName());

        marketOrderDO.setPaymentMethod(request.getPaymentMethodEnum().getCode());
        marketOrderDO.setPaymentStatus(request.getPaymentStatusEnum().getCode());
        marketOrderDO.setDeliverType(request.getDeliveryType().getCode());
        marketOrderDO.setCreateSource(request.getCreateSource().getCode());
        BigDecimal freightAmount = BigDecimal.ZERO;
        marketOrderDO.setFreightAmount(freightAmount);

        Integer goodsQuantity = request.getGoodsQuantity();

        GoodsDTO goodsDTO = goodsApi.queryInfo(request.getGoodsId());
        BigDecimal goodsTotalAmount = goodsDTO.getPrice().multiply(BigDecimal.valueOf(goodsQuantity));
        marketOrderDO.setGoodsTotalAmount(goodsTotalAmount);

        // 订单总额 = 运费 + 商品总额
        marketOrderDO.setOrderTotalAmount(goodsTotalAmount.add(freightAmount));
        marketOrderDO.setCreateSource(HmcCreateSourceEnum.HMC_MP.getCode());


        return marketOrderDO;

    }

    @Override
    public void saveRemark(MarketOrderSaveRequest request) {
        MarketOrderDO marketOrderDO = PojoUtils.map(request, MarketOrderDO.class);
        marketOrderDO.setUpdateUser(request.getOpUserId());
        this.updateById(marketOrderDO);
    }

    @Override
    public void orderDelivery(MarketOrderDeliveryRequest request) {
        MarketOrderDO marketOrderDO = this.getById(request.getId());
        // 1.校验订单
        if (null == marketOrderDO) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        // 2.修改发货信息
        marketOrderDO.setDeliverCompanyName(request.getDeliveryCompany());
        marketOrderDO.setDeliverNo(request.getDeliverNo());
        marketOrderDO.setDeliverTime(DateUtil.date());
        // 3.更改订单状态待收货
        marketOrderDO.setOrderStatus(HmcOrderStatusEnum.UN_RECEIVED.getCode());
        marketOrderDO.setUpdateTime(DateUtil.date());
        marketOrderDO.setUpdateUser(request.getOpUserId());
        this.updateById(marketOrderDO);

        // 处方订单发货 -> 调用IH接口发货
        if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(marketOrderDO.getMarketOrderType())) {
            HmcPrescriptionOrderDeliverRequest deliverRequest = new HmcPrescriptionOrderDeliverRequest();
            deliverRequest.setOrderId(marketOrderDO.getIhOrderId().intValue());
            deliverRequest.setPackageCompany(request.getDeliveryCompany());
            deliverRequest.setPackageNumber(request.getDeliverNo());
            deliverRequest.setUserId(request.getOpUserId());
            prescriptionApi.prescriptionOrderDeliver(deliverRequest);
        }

        // 4.小程序异步通知消息
        CompletableFuture.runAsync(() -> this.sendMsg(request.getId()), springAsyncConfig.getAsyncExecutor());
    }

    @Override
    public void prescriptionOrderDelivery(MarketPrescriptionOrderDeliveryRequest request) {
        QueryWrapper<MarketOrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketOrderDO::getIhOrderId, request.getIhOrderId());
        MarketOrderDO marketOrderDO = this.getOne(queryWrapper);
        // 1.校验订单
        if (Objects.isNull(marketOrderDO)) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        if (!HmcOrderStatusEnum.UN_DELIVERED.getCode().equals(marketOrderDO.getOrderStatus())) {
            log.error("当前订单状态非待发货，入参:{},db:{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(marketOrderDO));
            throw new BusinessException(HmcOrderErrorCode.ORDER_STATUS_ERROR);
        }
        // 2.修改发货信息
        marketOrderDO.setDeliverCompanyName(request.getDeliveryCompany());
        marketOrderDO.setDeliverNo(request.getDeliverNo());
        marketOrderDO.setDeliverTime(DateUtil.date());
        // 3.更改订单状态待收货
        marketOrderDO.setOrderStatus(HmcOrderStatusEnum.UN_RECEIVED.getCode());
        marketOrderDO.setUpdateTime(DateUtil.date());
        marketOrderDO.setUpdateUser(request.getOpUserId());
        this.updateById(marketOrderDO);

        // 4.小程序异步通知消息
        CompletableFuture.runAsync(() -> this.sendMsg(marketOrderDO.getId()), springAsyncConfig.getAsyncExecutor());
    }

    @Override
    public void marketOrderAutoReceive(Integer day) {
        //查询待收货订单
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketOrderDO::getOrderStatus, HmcOrderStatusEnum.UN_RECEIVED.getCode())
                .le(MarketOrderDO::getDeliverTime, DateUtil.offsetDay(DateUtil.date(), -day));
        List<MarketOrderDO> marketOrderDOList = this.list(wrapper);
        if (CollectionUtil.isNotEmpty(marketOrderDOList)) {
            for (MarketOrderDO one : marketOrderDOList) {
                one.setOrderStatus(HmcOrderStatusEnum.RECEIVED.getCode());
                one.setReceiveTime(DateUtil.date());
                one.setUpdateTime(DateUtil.date());

                this.updateById(one);

                // 如果是处方订单 -> 通知IH签收
                if (HmcMarketOrderTypeEnum.PRESCRIPTION_ORDER.getCode().equals(one.getMarketOrderType())) {
                    HmcPrescriptionOrderReceiveRequest receiveIHRequest = new HmcPrescriptionOrderReceiveRequest();
                    receiveIHRequest.setOrderId(one.getIhOrderId().intValue());
                    prescriptionApi.prescriptionOrderReceive(receiveIHRequest);
                }
            }
        }

    }

    @Override
    public void prescriptionOrderReceive(MarketPrescriptionOrderReceiveRequest request) {
        QueryWrapper<MarketOrderDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MarketOrderDO::getIhOrderId, request.getIhOrderId());
        MarketOrderDO marketOrderDO = this.getOne(queryWrapper);
        // 1.校验订单
        if (Objects.isNull(marketOrderDO)) {
            throw new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS);
        }
        if (!HmcOrderStatusEnum.UN_RECEIVED.getCode().equals(marketOrderDO.getOrderStatus())) {
            log.error("当前订单状态非待签收，入参:{},db:{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(marketOrderDO));
            throw new BusinessException(HmcOrderErrorCode.ORDER_STATUS_ERROR);
        }
        marketOrderDO.setOrderStatus(HmcOrderStatusEnum.RECEIVED.getCode());
        marketOrderDO.setReceiveTime(DateUtil.date());
        marketOrderDO.setUpdateTime(DateUtil.date());
        this.updateById(marketOrderDO);
    }

    @Override
    public MarketOrderDTO queryPrescriptionOrderByIhOrderId(Integer ihOrderId) {
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketOrderDO::getIhOrderId, ihOrderId);
        MarketOrderDO marketOrderDO = this.getOne(wrapper);
        return PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
    }

    @Override
    public MarketOrderDTO queryPrescriptionOrderByPrescriptionId(Long prescriptionId) {
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketOrderDO::getPrescriptionId, prescriptionId);
        wrapper.lambda().last(" limit 1");
        MarketOrderDO marketOrderDO = this.getOne(wrapper);
        return PojoUtils.map(marketOrderDO, MarketOrderDTO.class);
    }

    @Override
    public MarketOrderDO getByPrescriptionId(Integer prescriptionId) {
        QueryWrapper<MarketOrderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketOrderDO::getPrescriptionId, prescriptionId);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }
}
